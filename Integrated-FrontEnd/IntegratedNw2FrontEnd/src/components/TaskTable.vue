<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useStoreTasks } from "../stores/taskStores.js";
import { useStoreStatus } from "../stores/statusStores.js";
import { getTaskById } from "@/libs/api/task/fetchUtilTask.js";
import { getStatusData } from "@/libs/api/status/fetchUtilStatus.js";
import TaskModal from "../components/TaskModal.vue";
import { useToasterStore } from "@/stores/notificationStores";
const route = useRoute();
const router = useRouter();
const tasksStore = useStoreTasks();
const statusStore = useStoreStatus();
const toasterStore = useToasterStore();
const { tasks } = storeToRefs(tasksStore);
const showDetail = ref(false);
const storeMode = ref("");
const storeTask = ref({});
const storeTasks = ref({});
const storeIndex = ref(0);
const sortOrder = ref("DEFAULT");
const selectFilter = ref([]);
onMounted(async () => {
  const data = await tasksStore.fetchTasks();
  storeTasks.value = data;
});
onMounted(async () => {
  await statusStore.fetchStatus();
});
const fetchDataById = async (id, mode) => {
  storeMode.value = mode;
  storeTask.value = await getTaskById(id);
  statusStore.value = await getStatusData();
  if (storeMode.value === "add") {
    showDetail.value = true;
    router.push({ name: "addTask" });
  } else if (
    Object.keys(storeTask.value).length > 0 &&
    storeMode.value === "edit"
  ) {
    showDetail.value = true;
    router.push({ name: "editTask", params: { id: id } });
  } else if (
    Object.keys(storeTask.value).length > 0 &&
    storeMode.value === "view"
  ) {
    showDetail.value = true;
    router.push({ name: "taskDetail", params: { id: id } });
  } else if (storeMode.value === "delete") {
    showDetail.value = true;
  } else {
    showDetail.value = false;
  }
  if (storeTask.value.status == "404") {
    toasterStore.error({ text: "An error occurred redirect to task home" });
    router.replace({ name: "task" });
    showDetail.value = false;
  }
};

if (route.params.id) {
  fetchDataById(route.params.id, "view");
}

const removeTask = async (id) => {
  const res = await tasksStore.deleteTask(id);
  if (res !== 404) {
    toasterStore.success({ text: "Task deleted successfully!" });
  } else if (res === 404) {
    toasterStore.error({ text: "An error occurred while deleting the task." });
  }
};
const setIndex = (indexes) => {
  storeIndex.value = indexes;
};
const addEditTask = async (newTask) => {
  if (newTask.id === undefined) {
    if (newTask.assignees === null) {
      const data = await tasksStore.createTask({
        assignees: newTask.assignees,
        statusId: newTask.status ? newTask.status : 1,
        title: newTask.title.trim(),
        description: newTask.description ? newTask.description.trim() : newTask.description,
      });
      if (data.id) {
        toasterStore.success({ text: "Task added successfully!" });
      } else if (!data.id) {
        toasterStore.error({
          text: "An error occurred while saving the task.",
        });
      }
    } else {
      const data = await tasksStore.createTask({
        assignees: newTask.assignees.trim(),
        statusId: newTask.status ? newTask.status : 1,
        title: newTask.title.trim(),
        description: newTask.description ? newTask.description.trim() : newTask.description,
      });
      if (data.id) {
        toasterStore.success({ text: "Task added successfully!" });
      } else if (!data.id) {
        toasterStore.error({
          text: "An error occurred while saving the task.",
        });
      }
    }
  } else {
    if (newTask.assignees === null) {
      const dataEdit = await tasksStore.updateTask(newTask.id, {
        id: newTask.id,
        assignees: newTask.assignees,
        statusId: newTask.status,
        title: newTask.title.trim(),
        description: newTask.description ? newTask.description.trim() : newTask.description,
      });
      if (dataEdit.id) {
        toasterStore.success({ text: "Task Updated successfully!" });
      } else if (dataEdit.id === undefined) {
        toasterStore.error({
          text: "An error occurred while saving the task.",
        });
      }
    } else {
      const dataEdit = await tasksStore.updateTask(newTask.id, {
        id: newTask.id,
        assignees: newTask.assignees.trim(),
        statusId: newTask.status,
        title: newTask.title.trim(),
        description: newTask.description ? newTask.description.trim() : newTask.description,
      });
      if (dataEdit.id) {
        toasterStore.success({ text: "Task Updated successfully!" });
      } else if (!dataEdit.id) {
        toasterStore.error({
          text: "An error occurred while saving the task.",
        });
      }
    }
  }
};
const setDetail = (value, id, mode) => {
  showDetail.value = value;
  storeMode.value = mode;
  if (storeMode.value === "add") {
    router.push({ name: "addTask" });
  } else if (storeMode.value === "edit") {
    router.push({ name: "editTask", params: { id: id } });
  } else if (id !== null && storeMode.value !== "edit") {
    router.push({ name: "taskDetail", params: { id: id } });
  }
};

const setClose = (value) => {
  showDetail.value = value;
  router.push({ name: "task" });
};

const SortOrder = async () => {
  await tasksStore.sortTasksByStatus(sortOrder.value);
  if (sortOrder.value === "DEFAULT") {
    sortOrder.value = "ASC";
  } else if (sortOrder.value === "ASC") {
    sortOrder.value = "DESC";
  } else {
    sortOrder.value = "DEFAULT";
  }
};

const updateFilterList = (filterName) => {
  const filterIndex = selectFilter.value.indexOf(filterName);

  if (filterIndex === -1) {
    selectFilter.value.push(filterName);
  } else {
    selectFilter.value.splice(filterIndex, 1);
  }
};

const getFilterTask = computed(() => {
  return selectFilter.value.length > 0
    ? tasks.value.filter((task) =>
        selectFilter.value.includes(task.status.name)
      )
    : tasks.value;
});

const ClearStatuses = () => {
  selectFilter.value.splice(0, selectFilter.value.length);
};
</script>

<template>
  <div>
    <div class="flex justify-between sm:flex-nowrap mobile:flex-wrap mobile:justify-end tablet:justify-between ">
      <div class="ml-8 my-2 flex gap-2 tablet:w-2/5 mobile:mr-4 mobile:w-full">
        
        <div
          class="flex gap-2 items-center justify-center h-[48px] w-full  border border-[#00BFA5] rounded-lg "
        >
          <div class="transition ease-in-out hover:scale-125 duration-300">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              id="Outline"
              viewBox="0 0 10 24"
              width="45"
              height="15"
            >
              <path
                d="M23.707,22.293l-5.969-5.969a10.016,10.016,0,1,0-1.414,1.414l5.969,5.969a1,1,0,0,0,1.414-1.414ZM10,18a8,8,0,1,1,8-8A8.009,8.009,0,0,1,10,18Z"
                fill="#00BFA5"
              />
            </svg>
          </div>
          <div
            class="dropdown dropdown-bottom dropdown-hover w-full flex h-[48px]"
          >
            <div
              tabindex="0"
              role="button"
              class="overflow-x-auto max-h-[40px] btn w-full pt-2 text-[#00BFA5] border-none text-base rounded-lg bg-transparent hover:bg-transparent "
            >
              <div
                v-for="statuses in selectFilter"
                class="bg-[#00BFA5] overflow-x text-white rounded-lg pl-2 min-w-20 min-h-8 flex items-center gap-x-3 justify-around animate-jump"
              >
                <div>{{ statuses }}</div>
                <div
                  class="pr-3 transition ease-in-out hover:scale-125 duration-300 hover:text-red-500 hover:font-bold hover:cursor-pointer"
                  @click="updateFilterList(statuses)"
                >
                  x
                </div>
              </div>
            </div>
            <ul
              tabindex="0"
              class="dropdown-content z-[1] menu p-2 shadow bg-white mt-1 rounded-box w-full "
            >
              <li
                v-for="statuses in statusStore.statuses"
                :key="statuses.id"
                @click="updateFilterList(statuses.name)"
              >
                <div class="hover:text-white hover:bg-[#00BFA5] hover:shadow-inner ">
                  <input
                    type="checkbox"
                    class="checkbox hover:border-white"
                    :id="statuses.id"
                    :checked="selectFilter.includes(statuses.name)"
                    :value="statuses.id"
                  />
                  <label class="overflow-auto" :for="statuses.name">{{ statuses.name }}</label>
                </div>
              </li>
            </ul>
          </div>
          <p
            class="pr-3 text-[#00BFA5] text-xs transition ease-in-out hover:scale-125 duration-300 hover:text-red-500 hover:font-bold hover:cursor-pointer"
            @click="ClearStatuses()"
          >
            Clear
          </p>
        </div>
      </div>
      <div
          class="ml-8 my-2 mr-3 btn btn-outline"
          @click="SortOrder"
          v-if="sortOrder == 'DEFAULT'"
        >
          Sort by Status Name
          <svg
            xmlns="http://www.w3.org/2000/svg"
            id="Layer_1"
            data-name="Layer 1"
            viewBox="0 0 24 24"
            width="15"
            height="15"
          >
            <path
              d="M22,23c0,.553-.447,1-1,1h-4.112c-.686,0-1.318-.373-1.65-.973s-.312-1.333,.051-1.913c.03-.049,4.553-5.114,4.553-5.114h-3.841c-.553,0-1-.447-1-1s.447-1,1-1h4.112c.686,0,1.317,.372,1.649,.972s.313,1.333-.051,1.915c-.03,.048-.064,.094-.103,.136l-4.449,4.978h3.841c.553,0,1,.447,1,1Zm0-19.5v5.5c0,.553-.447,1-1,1s-1-.447-1-1v-2h-3v2c0,.553-.447,1-1,1s-1-.447-1-1V3.5c0-1.93,1.57-3.5,3.5-3.5s3.5,1.57,3.5,3.5Zm-2,1.5v-1.5c0-.827-.673-1.5-1.5-1.5s-1.5,.673-1.5,1.5v1.5h3Zm-9.707,12.707l-3.293,3.293V1c0-.553-.447-1-1-1s-1,.447-1,1V21l-3.293-3.293c-.391-.391-1.023-.391-1.414,0s-.391,1.023,0,1.414l4.293,4.293c.39,.39,.902,.585,1.414,.585s1.024-.195,1.414-.585l4.293-4.293c.391-.391,.391-1.023,0-1.414s-1.023-.391-1.414,0Z"
              fill="#B0BEC5"
            />
          </svg>
        </div>
        <div
          class="ml-8 my-2 mr-3 btn btn-outline btn-primary"
          @click="SortOrder"
          v-else-if="sortOrder == 'ASC'"
        >
          Sort by Status Name
          <svg
            xmlns="http://www.w3.org/2000/svg"
            id="Layer_1"
            data-name="Layer 1"
            viewBox="0 0 24 24"
            width="15"
            height="15"
          >
            <path
              d="M22,23c0,.553-.447,1-1,1h-4.112c-.686,0-1.318-.373-1.65-.973s-.312-1.333,.051-1.913c.03-.049,4.553-5.114,4.553-5.114h-3.841c-.553,0-1-.447-1-1s.447-1,1-1h4.112c.686,0,1.317,.372,1.649,.972s.313,1.333-.051,1.915c-.03,.048-.064,.094-.103,.136l-4.449,4.978h3.841c.553,0,1,.447,1,1Zm0-19.5v5.5c0,.553-.447,1-1,1s-1-.447-1-1v-2h-3v2c0,.553-.447,1-1,1s-1-.447-1-1V3.5c0-1.93,1.57-3.5,3.5-3.5s3.5,1.57,3.5,3.5Zm-2,1.5v-1.5c0-.827-.673-1.5-1.5-1.5s-1.5,.673-1.5,1.5v1.5h3Zm-9.707,12.707l-3.293,3.293V1c0-.553-.447-1-1-1s-1,.447-1,1V21l-3.293-3.293c-.391-.391-1.023-.391-1.414,0s-.391,1.023,0,1.414l4.293,4.293c.39,.39,.902,.585,1.414,.585s1.024-.195,1.414-.585l4.293-4.293c.391-.391,.391-1.023,0-1.414s-1.023-.391-1.414,0Z"
              fill="#304FFE"
            />
          </svg>
        </div>
        <div
          class="ml-8 my-2 mr-3 btn btn-outline btn-secondary"
          @click="SortOrder"
          v-else="sortOrder == 'DESC'"
        >
          Sort by Status Name
          <svg
            xmlns="http://www.w3.org/2000/svg"
            id="Layer_1"
            data-name="Layer 1"
            viewBox="0 0 24 24"
            width="15"
            height="15"
          >
            <path
              d="M15,1c0-.553,.447-1,1-1h4.112c.686,0,1.317,.372,1.649,.972,.332,.599,.313,1.332-.049,1.913-.03,.049-4.554,5.115-4.554,5.115h3.841c.553,0,1,.447,1,1s-.447,1-1,1h-4.112c-.686,0-1.317-.372-1.649-.972-.332-.599-.313-1.332,.049-1.913,.03-.049,4.554-5.115,4.554-5.115h-3.841c-.553,0-1-.447-1-1Zm7,16.5v5.5c0,.553-.447,1-1,1s-1-.447-1-1v-2h-3v2c0,.553-.447,1-1,1s-1-.447-1-1v-5.5c0-1.93,1.57-3.5,3.5-3.5s3.5,1.57,3.5,3.5Zm-2,0c0-.827-.673-1.5-1.5-1.5s-1.5,.673-1.5,1.5v1.5h3v-1.5Zm-9.707,.207l-3.293,3.293V1c0-.553-.447-1-1-1s-1,.447-1,1V21l-3.293-3.293c-.391-.391-1.023-.391-1.414,0s-.391,1.023,0,1.414l4.293,4.293c.39,.39,.902,.585,1.414,.585s1.024-.195,1.414-.585l4.293-4.293c.391-.391,.391-1.023,0-1.414s-1.023-.391-1.414,0Z"
              fill="#D50000"
            />
          </svg>
        </div>
      <div class="my-2 flex">
        <div
          class="itbkk-button-add btn btn-outline btn-primary tablet:mr-12 ml-3 md:mr-20 lg:mr-12  tablet:ml-8 mobile:mr-4"
          @click="setDetail(true, null, 'add')"
        >
          <svg
            width="20"
            height="20"
            viewBox="0 0 20 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <g clip-path="url(#clip0_529_11)">
              <path
                d="M11 9H15V11H11V15H9V11H5V9H9V5H11V9ZM10 20C7.34784 20 4.8043 18.9464 2.92893 17.0711C1.05357 15.1957 0 12.6522 0 10C0 7.34784 1.05357 4.8043 2.92893 2.92893C4.8043 1.05357 7.34784 0 10 0C12.6522 0 15.1957 1.05357 17.0711 2.92893C18.9464 4.8043 20 7.34784 20 10C20 12.6522 18.9464 15.1957 17.0711 17.0711C15.1957 18.9464 12.6522 20 10 20ZM10 18C12.1217 18 14.1566 17.1571 15.6569 15.6569C17.1571 14.1566 18 12.1217 18 10C18 7.87827 17.1571 5.84344 15.6569 4.34315C14.1566 2.84285 12.1217 2 10 2C7.87827 2 5.84344 2.84285 4.34315 4.34315C2.84285 5.84344 2 7.87827 2 10C2 12.1217 2.84285 14.1566 4.34315 15.6569C5.84344 17.1571 7.87827 18 10 18Z"
                fill="#7C5ED2"
              />
            </g>
            <defs>
              <clipPath id="clip0_529_11">
                <rect width="20" height="20" fill="white" />
              </clipPath>
            </defs>
          </svg>
          Add Task
        </div>
      </div>
    </div>

    <div class="w-full flex justify-center">
      <div class="shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40">
        <div class="min-w-full divide-y divide-gray-200 overflow-auto">
          <div class="#4793AF bg-slate-600 flex rounded-md overflow-auto">
            <div
              class="w-[10%] px-6 py-3 text-left text-md font-bold text-white uppercase overflow-auto"
            ></div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase overflow-auto"
            >
              Title
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase overflow-auto"
            >
              Assignees
            </div>
            <div
              class="w-[22%] px-6 py-3 text-center text-md font-bold text-white uppercase overflow-auto"
            >
              Status
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase overflow-auto"
            >
              Action
            </div>
          </div>
          <div v-if="tasks.length <= 0" class="w-full border bg-white h-24">
            <div class="flex justify-center items-center h-full">
              <p class="text-xl font-bold animate-bounce text-slate-500">
                No task
              </p>
            </div>
          </div>
          <div class="w-full mobile:h-[330px] tablet:h-[400px] md:h-[380px] lg:h-[500px] xl:h-[550px] 2xl:h-[600px] overflow-auto rounded ">
            <div v-for="(task, index) in getFilterTask" :key="task.id">
              <div class="bg-white divide-y divide-gray-200 overflow-auto">
                <div
                  class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 bg-slate"
                >
                  <div class="flex hover:shadow-inner hover:bg-slate-50">
                    <div
                      class="w-[10%] px-6 py-4 whitespace-nowrap"
                      @click="fetchDataById(task.id, 'view')"
                    >
                      {{ index + 1 }}
                    </div>
                    <div
                      class="itbkk-title w-[22%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="fetchDataById(task.id, 'view')"
                    >
                      {{ task.title }}
                    </div>
                    <div
                      class="itbkk-assignees w-[22%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="fetchDataById(task.id, 'view')"
                      :style="{
                        fontStyle: task.assignees ? 'normal' : 'italic',
                      }"
                    >
                      {{ task.assignees ? task.assignees : "Unassigned" }}
                    </div>
                    <div
                      class="w-[22%] px-6 py-4 whitespace-nowrap flex justify-center overflow-x-auto"
                      @click="fetchDataById(task.id, 'view')"
                    >
                      <div
                        class="itbkk-status btn btn-outline shadow overflow-x-auto"
                      >
                        {{ task?.status.name }}
                      </div>
                    </div>
                    <div
                      class="itbkk-button-action w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                    >
                      <div
                        class="itbkk-button-edit btn btn-outline btn-warning"
                        @click="fetchDataById(task.id, 'edit')"
                      >
                        Edit
                      </div>
                      <div
                        class="itbkk-button-delete btn btn-outline btn-error"
                        @click="
                          fetchDataById(task.id, 'delete'), setIndex(index)
                        "
                      >
                        Delete
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <teleport to="#body">
      <TaskModal
        v-if="showDetail"
        @close="setClose"
        :mode="storeMode"
        :task="storeTask"
        @newTask="addEditTask"
        :index="storeIndex"
        @saveDelete="removeTask"
      />
    </teleport>
  </div>
</template>

<style scoped></style>
