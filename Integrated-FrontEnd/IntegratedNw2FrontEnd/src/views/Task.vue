<script setup>
import { getTaskById, getTaskData, deleteTask } from "../libs/fetchUtil.js";
import { onMounted, ref } from "vue";
import { TaskManagement } from "/src/libs/TaskManagement.js";
import { useRoute, useRouter } from "vue-router";
import router from "../router/router.js";
import Modal from "../components/Modal.vue";
import Delete from "../views/Delete.vue";
const showDetail = ref(false);
const route = useRoute();
const taskManagement = new TaskManagement();
const dataById = ref();
let historyStack = [];
onMounted(async () => {
  taskManagement.setTasks(await getTaskData(import.meta.env.VITE_BASE_URL));
});
const handleDelete = async () => {
  try {
    await deleteTask(dataById.value.id);
    taskManagement.setTasks(await getTaskData(import.meta.env.VITE_BASE_URL));
  } catch (error) {
    console.error("Error deleting task:", error);
  }
};
const onSetDelete = (value) => {
  if (value) {
    handleDelete();
  } else {
    console.log("Deletion cancelled");
  }
};
const setDelete = (del) => {
  showDelete.value = del;
};
const setMode = (mode) => {
  storeMode.value = mode;
};
const setDetail = (set) => {
  showDetail.value = set;
};

async function fetchById(id) {
  dataById.value = await getTaskById(import.meta.env.VITE_BASE_URL, id);
  router.push({ name: "taskDetail", params: { id: id } });
  if (dataById.value.status == "404") {
    alert("The requested task does not exist");
    router.replace({ name: "task" });
    return;
    setDetail(true);
  }
  window.onpopstate = function () {
    const previousState = historyStack.pop();
    if (previousState === true) {
      setDetail(true); // Forward navigation
    } else {
      setDetail(false); // Backward navigation or initial load
    }
  };
  function navigateToDetail(showDetail) {
    historyStack.push(showDetail);
  }
  if (route.params.id) {
    fetchById(route.params.id);
  }
  const task = ref({
    status: "No Status",
    todo: "To Do",
    doing: "Doing",
    done: "Done",
  });

  const getStatusColor = (status) => {
    switch (status) {
      case "No status":
        return "SlateGray";
      case "To do":
        return "Tomato";
      case "Doing":
        return "Orange";
      case "Done":
        return "LimeGreen";
      default:
        return "transparent";
    }
  };
  const convertStatus = (status) => {
    switch (status) {
      case "No status":
        return "No Status";
      case "To do":
        return "To Do";
      case "Doing":
        return "Doing";
      case "Done":
        return "Done";
    }
  };
}
</script>

<template>
  <div class="w-full h-screen flex justify-center items-center">
    <div class="w-[95%] h-[90%]">
      <div class="w-full bg-slate-100 h-full rounded-2xl shadow-inner">
        <div class="w-[100%] py-3 flex-row flex items-start">
          <div>
            <div class="font-bold text-3xl text-blue-220 my-3 mx-6">
              IT-Bangmod Kradan Kanban
              <div class="w-[95%] h-[90%]">
                <div
                  class="w-full bg-slate-100 h-full rounded-2xl shadow-inner"
                >
                  <div class="w-[100%] py-3 flex-row flex items-start">
                    <div>
                      <div class="font-bold text-3xl text-blue-220 my-3 mx-6">
                        IT-Bangmod Kradan Kanban
                      </div>
                    </div>
                    <div class="grow"></div>
                    <div class="mr-20 mt-2 flex-row items-center">
                      <!-- Added items-center class -->
                      <div
                        class="btn btn-outline btn-primary"
                        @click="[setMode('add'), (showDetail = true)]"
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
                    <div
                      class="overflow-x-auto shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40"
                    >
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
                        <div
                          v-if="taskManagement.getTask().length === 0"
                          class="w-full border bg-white"
                        >
                          <div class="w-full">
                            <div></div>
                            <div></div>
                            <div class="m-[auto]">NO TASK</div>
                            <div></div>
                          </div>
                        </div>
                        <div
                          v-else
                          class="bg-white divide-y divide-gray-200 overflow-auto"
                        >
                          <div class="w-full max-h-[550px]">
                            <div
                              v-for="task in taskManagement.getTask()"
                              :key="task.taskId"
                              class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50"
                            >
                              <div class="flex">
                                <div
                                  class="w-[10%] px-6 py-4 whitespace-nowrap"
                                  @click="
                                    [
                                      (showDetail = true),
                                      fetchById(task.taskId),
                                      setMode('view'),
                                    ]
                                  "
                                >
                                  {{ task.taskId }}
                                </div>
                                <div
                                  class="w-[22%] itbkk-title px-6 py-4 whitespace-nowrap overflow-x-auto"
                                  @click="
                                    [
                                      (showDetail = true),
                                      fetchById(task.taskId),
                                      setMode('view'),
                                    ]
                                  "
                                >
                                  {{ task.title }}
                                </div>
                                <div
                                  class="w-[22%] itbkk-assignees px-6 py-4 whitespace-nowrap overflow-x-auto"
                                  :class="{ 'text-red-300': !task.assignees }"
                                  :style="{
                                    fontStyle: task.assignees
                                      ? 'normal'
                                      : 'italic',
                                  }"
                                  @click="
                                    [
                                      (showDetail = true),
                                      fetchById(task.taskId),
                                      setMode('view'),
                                    ]
                                  "
                                >
                                  {{
                                    task.assignees
                                      ? task.assignees
                                      : "Unassigned"
                                  }}
                                </div>
                                <div
                                  class="w-[22%] itbkk-status px-6 py-4 whitespace-nowrap flex justify-center overflow-x-auto"
                                >
                                  <div
                                    class="btn shadow text-white overflow-x-auto"
                                    @click="
                                      [
                                        (showDetail = true),
                                        fetchById(task.taskId),
                                        setMode('view'),
                                      ]
                                    "
                                    :style="{
                                      backgroundColor: getStatusColor(
                                        task.status
                                      ),
                                    }"
                                  >
                                    {{ convertStatus(task.status) }}
                                  </div>
                                </div>
                                <div
                                  class="w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                                >
                                  <div
                                    class="btn btn-outline btn-warning"
                                    @click="
                                      [
                                        setMode('edit'),
                                        (showDetail = true),
                                        fetchById(task.taskId),
                                      ]
                                    "
                                  >
                                    Edit
                                  </div>
                                  <div
                                    class="btn btn-outline btn-error"
                                    @click="
                                      [
                                        (showDelete = true),
                                        fetchById(task.taskId),
                                      ]
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
                    <teleport to="body" v-if="showDetail">
                      <Modal
                        @setDetail="setDetail"
                        :tasks="dataById"
                        :mode="storeMode"
                      ></Modal>
                    </teleport>
                    <Teleport to="body" v-if="showDelete">
                      <Delete @setDelete="setDelete" :tasks="dataById"></Delete>
                    </Teleport>
                  </div>
                </div>
              </div>
              <div class="grow"></div>
              <div class="mr-20 mt-2 flex-row items-center">
                <!-- Added items-center class -->
                <div class="btn btn-outline btn-primary">
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
              <div
                class="overflow-x-auto shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40"
              >
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
                  <div
                    v-if="taskManagement.getTask().length === 0"
                    class="w-full border bg-white"
                  >
                    <div class="w-full">
                      <div></div>
                      <div></div>
                      <div class="m-[auto]">NO TASK</div>
                      <div></div>
                    </div>
                  </div>
                  <div
                    v-else
                    class="bg-white divide-y divide-gray-200 overflow-auto"
                  >
                    <div class="w-full max-h-[550px]">
                      <div
                        v-for="task in taskManagement.getTask()"
                        :key="task.taskId"
                        class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50"
                        @click="[(showDetail = true), fetchById(task.taskId)]"
                      >
                        <div class="flex">
                          <div class="w-[10%] px-6 py-4 whitespace-nowrap">
                            {{ task.taskId }}
                          </div>
                          <div
                            class="w-[22%] itbkk-title px-6 py-4 whitespace-nowrap overflow-x-auto"
                          >
                            {{ task.title }}
                          </div>
                          <div
                            class="w-[22%] itbkk-assignees px-6 py-4 whitespace-nowrap"
                            :class="{ 'text-red-300': !task.assignees }"
                            :style="{
                              fontStyle: task.assignees ? 'normal' : 'italic',
                            }"
                          >
                            {{ task.assignees ? task.assignees : "Unassigned" }}
                          </div>
                          <div
                            class="w-[22%] itbkk-status px-6 py-4 whitespace-nowrap flex justify-center"
                          >
                            <div
                              class="btn shadow text-white"
                              :style="{
                                backgroundColor: getStatusColor(task.status),
                              }"
                            >
                              {{ convertStatus(task.status) }}
                            </div>
                          </div>
                          <div
                            class="w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                          >
                            <div class="btn btn-outline btn-warning">Edit</div>
                            <div class="btn btn-outline btn-error">Delete</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <teleport to="body" v-if="showDetail">
            <Modal @setDetail="setDetail" :tasks="dataById"></Modal>
          </teleport>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
