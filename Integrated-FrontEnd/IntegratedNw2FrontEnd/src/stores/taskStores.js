import { defineStore } from "pinia";
import {
  getTaskData,
  addTask,
  deleteItemById,
  editTask,
} from "../libs/api/task/fetchUtilTask.js";
import { ref } from "vue";
import {useToasterStore} from "../stores/notificationStores";


export const useStoreTasks = defineStore("tasks", () => {
  const tasks = ref([]);
  const toasterStore = useToasterStore();
  async function fetchTasks() {
    try {
      tasks.value = [];
      const taskData = await getTaskData();
      taskData.forEach((task) => {
        console.log(task);
        tasks.value.push(task);
      });
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
    }
  }

  async function createTask(task) {
    try {
      const addedTask = await addTask(task);
      tasks.value.push(addedTask);
      return addedTask;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function deleteTask(id) {
    try {
      await deleteItemById(id);
      // tasks.value.map((task) => {console.log(task);})
      const deleted = tasks.value.splice(
        tasks.value.findIndex((task) => task.id === id),
        1
      );
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function updateTask(id, task) {
    try {
      const updatedTask = await editTask(id, task);
      console.log(updatedTask);
      console.log(tasks.value);
      const taskIndex = tasks.value.findIndex((task) => task.id === id);
      console.log(taskIndex);
      tasks.value[taskIndex] = updatedTask;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function findTaskByStatusId(statusId) {
    return tasks.value.filter((task) => task.status === statusId);
  }

  async function sortTasksByStatus(sortOrder) {
    if (sortOrder === "DEFAULT") {
      tasks.value.sort((a, b) => a.status.name.localeCompare(b.status.name));
    } else if (sortOrder === "ASC") {
      tasks.value.sort((a, b) => b.status.name.localeCompare(a.status.name));
    } else {
      tasks.value.sort((a, b) => a.id - b.id);
    }
  }

  return {
    tasks,
    fetchTasks,
    createTask,
    deleteTask,
    updateTask,
    sortTasksByStatus,
  };
});
