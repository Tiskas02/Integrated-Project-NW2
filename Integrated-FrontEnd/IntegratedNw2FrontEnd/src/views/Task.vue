<script setup>
import { getTaskById, getTaskData } from "../libs/fetchUtil.js";
import { onMounted, ref } from "vue";
import { TaskManagement } from "/src/libs/TaskManagement.js";
import { useRoute, useRouter } from "vue-router";
import router from "../router/router.js";
import Modal from "../components/Modal.vue";

const showDetail = ref(false);
const route = useRoute();
const taskManagement = new TaskManagement();
const dataById = ref();
let historyStack = [];
onMounted(async () => {
  taskManagement.setTasks(await getTaskData(import.meta.env.VITE_BASE_URL));
});
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
  }
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
</script>

<template>
  <div class="w-[90%] m-[auto]">
    <div class="w-full">
      <div class="justify-center text-center w-full py-3">
        <h1 class="font-bold text-3xl text-white my-10">
          IT-Bangmod Kradan Kanban
        </h1>
      </div>

      <div class="overflow-x-auto shadow-2xl rounded-lg">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="#4793AF bg-slate-600">
            <tr>
              <th
                scope="col"
                class="px-6 py-3 text-left text-md font-bold text-white uppercase tracking-wider"
              ></th>
              <th
                scope="col"
                class="px-6 py-3 text-left text-md font-bold text-white uppercase tracking-wider"
              >
                Title
              </th>
              <th
                scope="col"
                class="px-6 py-3 text-left text-md font-bold text-white uppercase tracking-wider"
              >
                Assignees
              </th>
              <th
                scope="col"
                class="px-6 py-3 text-left text-md font-bold text-white uppercase tracking-wider"
              >
                Status
              </th>
            </tr>
          </thead>
          <tbody
            v-if="taskManagement.getTask().length === 0"
            class="w-full border bg-white"
          >
            <tr class="w-full">
              <td></td>
              <td></td>
              <td><div class="m-[auto]">NO TASK</div></td>
              <td></td>
            </tr>
          </tbody>
          <tbody v-else class="bg-white divide-y divide-gray-200">
            <tr
              v-for="task in taskManagement.getTask()"
              :key="task.taskId"
              class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50 hover:animate-pulse"
              @click="[(showDetail = true), fetchById(task.taskId)]"
            >
              <td class="px-6 py-4 whitespace-nowrap">{{ task.taskId }}</td>
              <td class="itbkk-title px-6 py-4 whitespace-nowrap">
                {{ task.title }}
              </td>

              <td
                class="itbkk-assignees px-6 py-4 whitespace-nowrap"
                :class="{ 'text-red-300': !task.assignees }"
                :style="{ fontStyle: task.assignees ? 'normal' : 'italic' }"
              >
                {{ task.assignees ? task.assignees : "Unassigned" }}
              </td>
              <td
                class="itbkk-status px-6 py-4 whitespace-nowrap text-white flex justify-center"
              >
                <div
                  class="btn shadow"
                  :style="{ backgroundColor: getStatusColor(task.status) }"
                >
                  {{ convertStatus(task.status) }}
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <teleport to="body" v-if="showDetail">
        <Modal @setDetail="setDetail" :tasks="dataById"></Modal>
      </teleport>
    </div>
  </div>
</template>

<style scoped></style>
