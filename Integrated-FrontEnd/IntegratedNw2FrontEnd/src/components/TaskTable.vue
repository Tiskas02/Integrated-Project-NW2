<script setup>
import { ref, onMounted, computed, Teleport, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useStoreBoard } from "@/stores/boardStore";
import { useStoreTasks } from "../stores/taskStores.js";
import { useStoreStatus } from "../stores/statusStores.js";
import { getTaskById } from "@/libs/api/task/fetchUtilTask.js";
import { getStatusData } from "@/libs/api/status/fetchUtilStatus.js";
import TaskModal from "../components/TaskModal.vue";
import { useToasterStore } from "@/stores/notificationStores";
import Boardvisibility from "@/views/BoardVisibility.vue";
import { useStoreCollab } from "@/stores/collabStore";
const route = useRoute();
const router = useRouter();
const boardStore = useStoreBoard();
const tasksStore = useStoreTasks();
const statusStore = useStoreStatus();
const toasterStore = useToasterStore();
const collabStore = useStoreCollab();
const { boards } = storeToRefs(boardStore);
const { nameCollab } = storeToRefs(boardStore);
const { tasks } = storeToRefs(tasksStore);
const { collabs } = storeToRefs(collabStore);
const showDetail = ref(false);
const storeMode = ref("");
const storeTask = ref({});
const storeTasks = ref({});
const storeIndex = ref(0);
const sortOrder = ref("DEFAULT");
const selectFilter = ref([]);
const routerId = ref(route.params.id);
const allStatus = ref([]);
const storeChangeVisibility = ref();
const nameboard = ref();
const storeVisibility = ref();
const showVisibility = ref(false);
const checkToggle = ref(false);
const boardsData = ref();
const defaultStatus = ref({
  statusId: null,
});
const accessRights = ref("WRITE");
const parseJwt = (token) => {
  if (!token) return null;
  const base64Url = token?.split(".")[1];
  const base64 = base64Url?.replace(/-/g, "+").replace(/_/g, "/");
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );
  return JSON.parse(jsonPayload);
};
const receiveToken = localStorage.getItem("token");
const token = parseJwt(receiveToken);

onMounted(async () => {
  if (token?.oid) {
    const boardData = await boardStore.fetchBoards(token?.oid);
    boardsData.value = boardData[0] ? boardData[0] : null;
    storeVisibility.value =
      boardData[0].visibilities === "PUBLIC" ? true : false;
  }
  const data = await tasksStore.fetchTasks(routerId.value);
  if (data.status === 403) {
    toasterStore.error({ text: data.message, setTimeout: 8000 });
    router.push({ name: "board" });
  }
  if (data.error) {
    toasterStore.error({ text: `error : ${data.error}` });
    router.push({ name: "board" });
  }
  nameboard.value = data[0]?.board?.name
    ? data[0]?.board?.name
    : boardsData.value.name;
    
  storeTasks.value = data;
  const collab = await collabStore.fetchCollabsByBoardId(routerId.value);
  if (collab.length !== 0) {
    collab.forEach((item) => {
      if (item.oid === token.oid) {
        accessRights.value = item.accessRight;
      }
    });
  } else {
    accessRights.value = "WRITE";
  }
  allStatus.value = await statusStore.fetchStatus(routerId.value);
  const noStatus = allStatus.value.find(
    (status) => status.name === "No Status"
  );
  if (noStatus) {
    defaultStatus.value.statusId = noStatus.id;
  }
});

watch(
  () => storeVisibility.value,
  (newVisibility) => {
    checkToggle.value = newVisibility === "PUBLIC";
  },
  { immediate: true }
);

const fetchDataById = async (routerId, id, mode) => {
  storeMode.value = mode;
  storeTask.value = await getTaskById(routerId, id, token.oid);
  statusStore.value = await getStatusData(routerId, token.oid);
  if (storeMode.value === "add") {
    showDetail.value = true;
    router.push({ name: "addTask" });
  } else if (
    Object.keys(storeTask.value).length > 0 &&
    storeMode.value === "edit"
  ) {
    showDetail.value = true;
    router.push({ name: "editTask", params: { editid: id } });
  } else if (
    Object.keys(storeTask.value).length > 0 &&
    storeMode.value === "view"
  ) {
    showDetail.value = true;
    router.push({ name: "taskDetail", params: { taskid: id } });
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

const removeTask = async (id) => {
  const res = await tasksStore.deleteTask(routerId.value, id);
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
      const data = await tasksStore.createTask(
        {
          assignees: newTask.assignees,
          statusId: newTask.statusId
            ? newTask.statusId
            : defaultStatus.value.statusId,
          title: newTask.title.trim(),
          description: newTask.description
            ? newTask.description.trim()
            : newTask.description,
        },
        routerId.value
      );
      if (data.id) {
        toasterStore.success({ text: "Task added successfully!" });
      } else if (!data.id) {
        toasterStore.error({
          text: "An error occurred while saving the task.",
        });
      }
    } else {
      const data = await tasksStore.createTask(
        {
          assignees: newTask.assignees.trim(),
          statusId: newTask.statusId
            ? newTask.statusId
            : defaultStatus.value.statusId,
          title: newTask.title.trim(),
          description: newTask.description
            ? newTask.description.trim()
            : newTask.description,
        },
        routerId.value
      );
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
      const dataEdit = await tasksStore.updateTask(routerId.value, newTask.id, {
        id: newTask.id,
        assignees: newTask.assignees,
        statusId: newTask.statusId,
        title: newTask.title.trim(),
        description: newTask.descriptio
          ? newTask.description.trim()
          : newTask.description,
      });
      if (dataEdit.id) {
        toasterStore.success({ text: "Task Updated successfully!" });
      } else if (dataEdit.id === undefined) {
        toasterStore.error({
          text: "An error occurred while saving the task.",
        });
      }
    } else {
      const dataEdit = await tasksStore.updateTask(routerId.value, newTask.id, {
        id: newTask.id,
        title: newTask.title.trim(),
        assignees: newTask.assignees.trim(),
        statusId: newTask.statusId,
        description: newTask.description
          ? newTask.description.trim()
          : newTask.description,
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
};
const setCloseVisibility = (value) => {
  storeVisibility.value = !storeVisibility.value;
  showVisibility.value = value;
};
const EditVisibilities = async (value) => {
  const data = await boardStore.updateVisibility(
    routerId.value,
    value.visibilities
  );
  if (data.visibility === value.visibilities) {
    storeVisibility.value = data.visibility === "PUBLIC" ? true : false;
    toasterStore.success({ text: "Visibility updated successfully!" });
  } else {
    toasterStore.error({
      text: "An error occurred while updating visibility.",
    });
  }
};

const setVisibility = async () => {
  const boardData = await boardStore.fetchBoards(token?.oid);
  boardsData.value = boardData[0] ? boardData[0] : null;
  const toVisibility = boardsData.value;
  storeChangeVisibility.value = toVisibility;
  showVisibility.value = true;
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
//filter
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

const colorCache = {};

const generateDynamicColors = (statuses) => {
  const total = statuses.length;
  return statuses.map((_, index) => {
    const hue = Math.floor((360 / total) * index);
    return `hsl(${hue}, 50%, 50%)`;
  });
};

const getColorForStatus = (statusName, statuses) => {
  if (!statusName) return "bg-gray-300";
  if (colorCache[statusName]) return colorCache[statusName];

  const dynamicColors = generateDynamicColors(statuses);

  statuses.forEach((status, index) => {
    colorCache[status.name] = dynamicColors[index];
  });

  return colorCache[statusName];
};
</script>

<template>
  <div class="w-full">
    <div class="w-full flex flex-col justify-start my-6">
      <div class="ml-2">Welcome to ,</div>
      <div
        class="itbkk-board-name font-rubik font-medium text-xl text-slate-800 ml-2 cursor-pointer hover:bg-gradient-to-r from-blue-600 via-green-500 to-indigo-400 hover:inline-block hover:text-transparent hover:bg-clip-text hover:duration-500"
      >
        {{ nameboard }}
      </div>
    </div>
  </div>
  <div
    class="flex justify-center text-2xl font-bold text-gray-900 ml-4 my-4 hover:bg-gradient-to-r from-blue-800 via-blue-400 to-blue-200 hover:text-transparent hover:bg-clip-text hover:duration-500"
  >
    Task Management
  </div>
  <div class="flex flex-col w-full justify-center my-4">
    <div>
      <div class="font-rubik text-sm ml-6 font-semibold">Tools Bar</div>
      <div
        class="flex flex-col tablet:flex-row items-center border w-[90%] tablet:w-[100%] h-fit rounded-lg mx-4"
      >
        <div class="flex flex-row w-full tablet:w-[80%]">
          <div class="flex items-center">
            <div class="w-[30%]">
              <div class="form-control">
                <label class="label cursor-pointer">
                  <span class="label-text mx-2">
                    {{ storeVisibility ? "public" : "private" }}
                  </span>
                  <input
                    type="checkbox"
                    class="itbkk-board-visibility toggle border-blue-500 bg-blue-500 [--tglbg:white] hover:bg-blue-700 checked:bg-blue-700"
                    v-model="storeVisibility"
                    @click="token && accessRights !== 'READ' && setVisibility()"
                    :checked="checkToggle"
                    :disabled="accessRights === 'READ' || !token"
                    :class="{
                      'opacity-50 cursor-not-allowed':
                        accessRights === 'READ' || !token,
                    }"
                  />
                </label>
              </div>
            </div>
          </div>
          <div class="w-[80%] tablet:grow mx-2">
            <div class="my-2 flex gap-2 w-full justify-center">
              <div
                v-if="!(showVisibility || showDetail)"
                class="flex gap-2 items-center justify-center h-[48px] w-full laptop:w-[60%] border border-slate-700 rounded-lg"
              >
                <div
                  class="transition ease-in-out hover:scale-125 duration-300"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    id="Outline"
                    viewBox="0 0 10 24"
                    width="45"
                    height="15"
                  >
                    <path
                      d="M23.707,22.293l-5.969-5.969a10.016,10.016,0,1,0-1.414,1.414l5.969,5.969a1,1,0,0,0,1.414-1.414ZM10,18a8,8,0,1,1,8-8A8.009,8.009,0,0,1,10,18Z"
                      fill="#334155"
                    />
                  </svg>
                </div>
                <div
                  class="dropdown dropdown-bottom dropdown-hover w-full flex h-[48px]  tooltip tooltip-info" data-tip="Filter by Status"
                >
                  <div
                    tabindex="0"
                    role="button"
                    class="overflow-x-auto max-h-[40px] btn w-full pt-2 text-[#334155] border-none text-base rounded-lg bg-transparent hover:bg-transparent"
                  >
                    <div
                      v-for="statuses in selectFilter"
                      class="bg-slate-500 overflow-x text-slate-700 rounded-lg pl-2 min-w-20 min-h-8 flex items-center gap-x-3 justify-around animate-jump"
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
                    class="dropdown-content z-[1] menu p-2 shadow bg-white mt-1 rounded-box w-full"
                  >
                    <li
                      v-for="statuses in statusStore.statuses"
                      :key="statuses.id"
                      @click="updateFilterList(statuses.name)"
                    >
                      <div
                        class="hover:text-white hover:bg-slate-700 hover:shadow-inner"
                      >
                        <input
                          type="checkbox"
                          class="checkbox hover:border-white"
                          :id="statuses.id"
                          :checked="selectFilter.includes(statuses.name)"
                          :value="statuses.id"
                        />
                        <label class="overflow-auto" :for="statuses.name">{{
                          statuses.name
                        }}</label>
                      </div>
                    </li>
                  </ul>
                </div>
                <p
                  class="pr-3 text-slate-700 text-xs transition ease-in-out hover:scale-125 duration-300 hover:text-red-500 hover:font-bold hover:cursor-pointer"
                  @click="ClearStatuses()"
                >
                  Clear
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="w-full tablet:w-fit laptop:w-[18%] flex justify-end">
          <div class="my-2 flex">
            <div
              class="itbkk-button-add btn border-none bg-gradient-to-r from-blue-700 to-blue-400 mx-5 tablet:mx-1"
              :class="{
                'opacity-50 cursor-not-allowed':
                  accessRights === 'READ' || !token,
              }"
              @click="
                token && accessRights !== 'READ' && setDetail(true, null, 'add')
              "
            >
              <svg
                width="20"
                height="20"
                viewBox="0 0 20 20"
                fill="white"
                xmlns="http://www.w3.org/2000/svg"
              >
                <g clip-path="url(#clip0_529_11)">
                  <path
                    d="M11 9H15V11H11V15H9V11H5V9H9V5H11V9ZM10 20C7.34784 20 4.8043 18.9464 2.92893 17.0711C1.05357 15.1957 0 12.6522 0 10C0 7.34784 1.05357 4.8043 2.92893 2.92893C4.8043 1.05357 7.34784 0 10 0C12.6522 0 15.1957 1.05357 17.0711 2.92893C18.9464 4.8043 20 7.34784 20 10C20 12.6522 18.9464 15.1957 17.0711 17.0711C15.1957 18.9464 12.6522 20 10 20ZM10 18C12.1217 18 14.1566 17.1571 15.6569 15.6569C17.1571 14.1566 18 12.1217 18 10C18 7.87827 17.1571 5.84344 15.6569 4.34315C14.1566 2.84285 12.1217 2 10 2C7.87827 2 5.84344 2.84285 4.34315 4.34315C2.84285 5.84344 2 7.87827 2 10C2 12.1217 2.84285 14.1566 4.34315 15.6569C5.84344 17.1571 7.87827 18 10 18Z"
                    fill="white"
                  />
                </g>
                <defs>
                  <clipPath id="clip0_529_11">
                    <rect width="20" height="20" fill="white" />
                  </clipPath>
                </defs>
              </svg>
              <div class="text-white">Add Task</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="h-[800px] tablet:h-[900px] laptop:h-[1000px]">
    <div
      class="w-[390px] tablet:w-[750px] laptop:w-full flex tablet:justify-start laptop:justify-center tablet:mx-4 laptop:mx-4 justify-center"
    >
      <div
        class="shadow-2xl rounded-md w-[360px] tablet:w-[650px] laptop:w-[95%] h-[95%] shadow-yellow-500/10 overflow-auto"
      >
        <div
          class="w-[800px] laptop:w-full h-[600px] tablet:h-[700px] laptop:h-[800px] divide-y divide-gray-200 overflow-auto"
          style="
            background-image: url('https://res.cloudinary.com/dyhavbbzf/image/upload/v1733736424/jcw7sjnihbpmwljizi7p.png');
            background-size: cover;
            background-position: center;
          "
        >
          <div class="bg-slate-800 flex rounded-md">
            <div
              class="w-[10%] m-auto text-start text-md font-bold text-white"
            ></div>
            <div
              class="w-[22%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Title</div>
            </div>

            <div
              class="w-[22%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Assignees</div>
            </div>
            <div
              class="w-[22%] h-14 text-center text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="flex justify-center items-center">
                <div>Status</div>
                <div>
                  <div
                    class="mx-2 cursor-pointer"
                    @click="SortOrder"
                    v-if="sortOrder == 'DEFAULT'"
                  >
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
                    class="mx-2 cursor-pointer"
                    @click="SortOrder"
                    v-else-if="sortOrder == 'ASC'"
                  >
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
                    class="mx-2 cursor-pointer"
                    @click="SortOrder"
                    v-else="sortOrder == 'DESC'"
                  >
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
                </div>
              </div>
            </div>
            <div
              class="w-[22%] h-14 text-left text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Action</div>
            </div>
          </div>
          <div v-if="tasks.length <= 0" class="w-full border bg-white h-24">
            <div class="flex justify-center items-center h-full">
              <p class="text-xl font-bold text-slate-500">No task</p>
            </div>
          </div>
          <div
            class="w-full tablet:h-[680px] laptop:h-[780px] overflow-auto rounded"
          >
            <div v-for="(task, index) in getFilterTask" :key="task.id">
              <div
                class="itbkk-item bg-white divide-y divide-gray-200 overflow-auto shadow-inner"
              >
                <div
                  class="cursor-pointer hover:text-violet-600 hover:duration-200 bg-slate"
                >
                  <div
                    class="itbkk-item flex hover:shadow-inner hover:bg-slate-50"
                  >
                    <div
                      class="w-[10%] px-6 py-4 whitespace-nowrap"
                      @click="fetchDataById(routerId, task.id, 'view')"
                    >
                      {{ index + 1 }}
                    </div>
                    <div
                      class="itbkk-title w-[22%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="fetchDataById(routerId, task.id, 'view')"
                    >
                      {{ task.title }}
                    </div>
                    <div
                      class="itbkk-assignees w-[22%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="fetchDataById(routerId, task.id, 'view')"
                      :style="{
                        fontStyle: task.assignees ? 'normal' : 'italic',
                      }"
                      :class="task.assignees ? '' : 'text-red-800'"
                    >
                      {{ task.assignees ? task.assignees : "Unassigned" }}
                    </div>
                    <div
                      class="itbkk-status w-[22%] px-6 py-4 whitespace-nowrap flex justify-center overflow-x-auto"
                      @click="fetchDataById(routerId, task.id, 'view')"
                    >
                      <div
                        class="itbkk-status btn btn-outline shadow overflow-x-auto"
                        :style="{
                          backgroundColor: getColorForStatus(
                            task?.status?.name,
                            statusStore.statuses
                          ),
                          color: 'white', // Adjust text color for better contrast
                        }"
                      >
                        {{ task?.status?.name }}
                      </div>
                    </div>
                    <div
                      class="itbkk-button-action w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                    >
                      <div
                        class="itbkk-button-edit text-white btn border-white bg-gradient-to-r from-yellow-500 to-yellow-300"
                        @click="
                          token &&
                            accessRights !== 'READ' &&
                            fetchDataById(routerId, task.id, 'edit')
                        "
                        :class="{
                          'opacity-50 cursor-not-allowed':
                            accessRights === 'READ' || !token,
                        }"
                      >
                        Edit
                      </div>
                      <div
                        class="itbkk-button-delete text-white btn btn-outline border-white bg-gradient-to-r from-red-500 to-red-300"
                        @click="
                          token &&
                            accessRights !== 'READ' &&
                            (fetchDataById(routerId, task.id, 'delete'),
                            setIndex(index))
                        "
                        :class="{
                          'opacity-50 cursor-not-allowed':
                            accessRights === 'READ' || !token,
                        }"
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
    <!-- box -->
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
      <Boardvisibility
        v-if="showVisibility"
        :board="storeChangeVisibility"
        @newBoard="EditVisibilities"
        @close="setCloseVisibility"
      />
    </teleport>
  </div>
</template>

<style scoped></style>
