<script setup>
import { storeToRefs } from "pinia";
import { useStoreTasks } from "../stores/taskStores.js";
import { useStoreStatus } from "../stores/statusStores.js";
import { ref, onMounted, computed, watch } from "vue";
import TaskModal from "../components/TaskModal.vue";
import { useRoute, useRouter } from "vue-router";
import { getTaskById, getTaskData } from "@/libs/api/task/fetchUtilTask.js";
import LimitTaskStatus from "./LimitTaskStatus.vue";
import { getStatusData } from "@/libs/api/status/fetchUtilStatus.js";

const route = useRoute();
const router = useRouter();
const tasksStore = useStoreTasks();
const statusStore = useStoreStatus();
const { statuses } = storeToRefs(statusStore);
const { isLoading, tasks } = storeToRefs(tasksStore);
const showDetail = ref(false);
const showLimit = ref(false);
const storeMode = ref("");
const storeTask = ref({});
const storeTasks = ref({})
const storeIndex = ref(0);
const storeStatus = ref({});
const filterBy = ref([]);
const sortOrder = ref("DEFAULT");
const selectFilter = ref([]);
//fetch data
onMounted(async () => {
  const data = await tasksStore.fetchTasks();
  storeTasks.value = data
  console.log(tasks.value);
});
onMounted(async () => {
  await statusStore.fetchStatus();
});
//fetch data by id
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
    alert("The requested task does not exist");
    router.replace({ name: "task" });
    showDetail.value = false;
    return;
  }
};

if (route.params.id) {
  fetchDataById(route.params.id, "view");
}

const removeTask = async (id) => {
  await tasksStore.deleteTask(id);
};
const setIndex = (indexes) => {
  storeIndex.value = indexes;
};
const addEditTask = async (newTask) => {
  console.log(newTask.id);
  if (newTask.id === undefined) {
    if (newTask.assignees === null) {
      await tasksStore.createTask({
        assignees: newTask.assignees,
        statusId: newTask.status,
        title: newTask.title.trim(),
        description: newTask.description.trim(),
      });
    } else {
      await tasksStore.createTask({
        assignees: newTask.assignees.trim(),
        statusId: newTask.status,
        title: newTask.title.trim(),
        description: newTask.description.trim(),
      });
    }
  } else {
    if (newTask.assignees === null) {
      await tasksStore.updateTask(newTask.id, {
        id: newTask.id,
        assignees: newTask.assignees,
        statusId: newTask.status,
        title: newTask.title.trim(),
        description: newTask.description,
      });
    } else {
      await tasksStore.updateTask(newTask.id, {
        id: newTask.id,
        assignees: newTask.assignees.trim(),
        statusId: newTask.status,
        title: newTask.title.trim(),
        description: newTask.description.trim(),
      });
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
const setLimit = (value) => {
  showLimit.value = value;
  console.log(showLimit.value);
  if (showLimit.value == true) {
    console.log("asf");
    router.push({ name: "limit" });
  }
};
const setClose = (value) => {
  showDetail.value = value;
  showLimit.value = value;
  router.push({ name: "task" });
};

const SortOrder = async () => {
  await tasksStore.sortTasksByStatus(sortOrder.value) 
  if (sortOrder.value === "DEFAULT") {
    console.log(sortOrder.value);
    sortOrder.value = "ASC";
  } else if (sortOrder.value === "ASC") {
    console.log(sortOrder.value);
    sortOrder.value = "DESC";
  } else {
    console.log(sortOrder.value);
    sortOrder.value = "DEFAULT";
  }
};

const statusFilterList = (itemName) => {
  const index = selectFilter.value.findIndex((item) => {
    return item === itemName
  })
  console.log(index)
  if (index === -1) {
    selectFilter.value.push(itemName)
  } else {
    selectFilter.value.splice(index, 1)
  }
  console.log(selectFilter.value)
}

const getFilterTask = computed(() => {
  return selectFilter.value.length > 0
    ? tasks.value.filter((task) =>
        selectFilter.value.includes(task.status.name)
      )
    : tasks.value
})

const ClearStatuses = () => {
  selectFilter.value.splice(0, selectFilter.value.length)
  console.log(selectFilter.value)
}
</script>

<template>
  <div>
    <div class="flex justify-between">
      <div class="my-2 flex gap-2 w-4/5">
        <div class="ml-10 btn btn-outline btn-accent">
          <router-link :to="{ name: 'status' }">Manage Status</router-link>
        </div>
          <div class="ml-3 btn btn-outline btn-accent" @click="SortOrder" v-if="sortOrder == 'DEFAULT'">
            Sort by
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="24"
              viewBox="0 0 24 24"
              width="24"
            >
              <path d="M0 0h24v24H0z" fill="none" />
              <path
                d="M3 18h6v-2H3v2zM3 6v2h18V6H3zm0 7h12v-2H3v2z"
                fill="#00BFA5"
              />
            </svg>
          </div>
          <div class="ml-3 btn btn-outline btn-accent" @click="SortOrder" v-else-if="sortOrder == 'ASC'">
            Sort by
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="24"
              viewBox="0 0 24 24"
              width="24"
            >
              <path d="M0 0h24v24H0z" fill="none" />
              <path
                d="M3 18h6v-2H3v2zM3 6v2h18V6H3zm0 7h12v-2H3v2z"
                fill="#00BFA5"
              />
            </svg>
          </div>
          <div class="ml-3 btn btn-outline btn-accent" @click="SortOrder" v-else="sortOrder == 'DESC'">
            Sort by
            <svg
              xmlns="http://www.w3.org/2000/svg"
              height="24"
              viewBox="0 0 24 24"
              width="24"
            >
              <path d="M0 0h24v24H0z" fill="none" />
              <path
                d="M3 18h6v-2H3v2zM3 6v2h18V6H3zm0 7h12v-2H3v2z"
                fill="#00BFA5"
              />
            </svg>
          </div>
          <div
          class="flex gap-2 items-center justify-center h-[48px] w-full border border-[#00BFA5] rounded-lg"
        >
          <img :src="SearchIcon" width="35px" class="pl-3" />
          <div
            class="dropdown dropdown-bottom w-full flex border-l border-l-[#00BFA5] h-[48px]"
          >
            <div
              tabindex="0"
              role="button"
              class="overflow-x-auto max-h-[40px] btn w-full text-[#00BFA5] border-none text-base rounded-lg bg-transparent hover:bg-transparent"
            >
              <div
              v-for="statuses in selectFilter"
                class="bg-[#00BFA5] text-white rounded-lg pl-2 min-w-20 min-h-8 flex items-center gap-x-3 justify-around"
              >
                <div>{{ statuses }}</div>
                <div class="pr-3" @click="statusFilterList(statuses)">X</div>
              </div>
            </div>
            <ul
              tabindex="0"
              class="dropdown-content z-[1] menu p-2 shadow bg-base-100 mt-2 rounded-box w-full"
            >
              <li
              v-for="statuses in statusStore.statuses"
                :key="statuses.id"
                @click="statusFilterList(statuses.name)"
              >
                <div class="hover:text-white hover:bg-[#00BFA5]">
                  <input
                    type="checkbox"
                    class="checkbox hover:border-white"
                    :id="statuses.id"
                    :checked="selectFilter.includes(statuses.name)"
                    :value="statuses.id"
                  />
                  <label :for="statuses.name">{{ statuses.name }}</label>
                </div>
              </li>
            </ul>
          </div>
          <p
            class="pr-3 text-[#00BFA5] cursor-pointer"
            @click="ClearStatuses()"
          >
            X
          </p>
        </div>
      </div>
      <div class="my-2 flex">
        <!-- Add Task-->
        <div class="enable-limit" @click="setLimit(true)">
          <button class="btn btn-info">Limits</button>
        </div>
        <div
          class="itbkk-button-add btn btn-outline btn-primary mr-12 ml-3"
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
          add task
        </div>
        <!-- Added ml-2 class for margin-left -->
      </div>
    </div>

    <div class="w-full flex justify-center">
      <div class="shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40">
        <div class="min-w-full divide-y divide-gray-200">
          <div class="#4793AF bg-slate-600 flex">
            <div
              class="w-[10%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            ></div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Title
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Assignees
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Status
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
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
          <!-- Edit Task -->
          <div class="w-full h-[350px] overflow-auto">
            <div v-for="(task, index) in getFilterTask" :key="task.id">
              <div class="bg-white divide-y divide-gray-200 overflow-auto">
                <div class="">
                  <div
                    class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50"
                  >
                    <div class="flex">
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
      <teleport to="#body">
        <LimitTaskStatus
          v-if="showLimit"
          @limit="setLimit"
          :mode="storeMode"
          @close="setClose"
        />
      </teleport>
    </div>
  </div>
</template>

<style scoped></style>
