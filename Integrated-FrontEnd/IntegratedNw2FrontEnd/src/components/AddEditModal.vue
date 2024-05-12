<script setup>
import {
  getTaskById,
  getTaskData,
  addTask,
  deleteItemById,
  updateTask,
} from "../libs/fetchUtil.js";
import { onMounted, ref, computed, defineEmits } from "vue";
import taskManagement from "../libs/TaskManagement.js";
import { useRoute, useRouter } from "vue-router";
import Modal from "../components/Modal.vue";
import Delete from "../views/Delete.vue";

import useToasterStore from "../stores/notificationStores.js";

const showDetail = ref(false);
const showDelete = ref(false);
const dataById = ref();
const route = useRoute();
const storeMode = ref(route.params.id ? "edit" : "add");
let historyStack = [];
const showDeleteStatus = ref(false);
const myTasks = ref(taskManagement);
const removeId = ref();
const storeIndex = ref(0);
const emit = defineEmits(["statusDelete"]);
const toastStore = useToasterStore();
const router = useRouter();

onMounted(async () => {
  if (route.params.id) {
    dataById.value = await getTaskById(
      import.meta.env.VITE_BASE_URL,
      route.params.id
    );
    if (!dataById.value) router.push({ name: "task" });
  }
});

const updateEdit = async (newEdit) => {
  if (newEdit.taskId === undefined) {
    let convertedStatus = "";
    //backend
    const addedTask = await addTask(import.meta.env.VITE_BASE_URL, {
      assignees: newEdit?.assignees?.trim() ?? "",
      status: newEdit.status,
      title: newEdit?.title?.trim() ?? "",
      description: newEdit?.description?.trim() ?? "",
    });
    console.log(addedTask.taskId);
    switch (addedTask.status) {
      case "NO_STATUS":
        convertedStatus = "No Status";
        break;
      case "TO_DO":
        convertedStatus = "To Do";
        break;
      case "DOING":
        convertedStatus = "Doing";
        break;
      case "DONE":
        convertedStatus = "Done";
        break;
      default:
        convertedStatus = "No Status"; // Handle any other status
        break;
    }
    //frontend
    myTasks.value.addTask({
      taskId: addedTask.taskId,
      title: addedTask.title,
      assignees: addedTask.assignees,
      status: convertedStatus,
      description: addedTask.description,
      createdOn: addedTask.createdOn,
      updatedOn: addedTask.updatedOn,
    });
    router.push({ name: "task" });
  } else {
    const updatedTask = await updateTask(
      import.meta.env.VITE_BASE_URL,
      newEdit.taskId,
      {
        assignees: newEdit.assignees.trim(),
        status: newEdit.status,
        title: newEdit.title.trim(),
        description: newEdit.description.trim(),
      }
    );
    let convertedStatus = "";
    switch (updatedTask.status) {
      case "NO_STATUS":
        convertedStatus = "No Status";
        break;
      case "TO_DO":
        convertedStatus = "To Do";
        break;
      case "DOING":
        convertedStatus = "Doing";
        break;
      case "DONE":
        convertedStatus = "Done";
        break;
      default:
        convertedStatus = "No Status";
        break;
    }
    console.log(convertedStatus);
    myTasks.value.updateTask({
      taskId: updatedTask.taskId,
      assignees: updatedTask.assignees,
      status: convertedStatus,
      title: updatedTask.title,
      description: updatedTask.description,
      createdOn: updatedTask.createdOn,
      updatedOn: updatedTask.updatedOn,
    });
    router.push({ name: "task" });
  }
};
</script>

<template>
  <Modal :tasks="dataById" :mode="storeMode" @saveTask="updateEdit" />
</template>

<style scoped></style>