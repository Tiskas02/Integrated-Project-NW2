import { defineStore } from "pinia";
import { ref } from "vue";
import {
  getTaskData,
  addTask,
  deleteItemById,
  editTask,
} from "../libs/api/task/fetchUtilTask.js";

export const useStoreTasks = defineStore("tasks", () => {
  const tasks = ref([]);
  async function fetchTasks() {
    try {
      tasks.value = [];
      const taskData = await getTaskData();
      taskData.forEach((task) => {
        tasks.value.push(task);
      });
    } catch (error) {
      console.error("Error fetching data:", error);
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
      const res = await deleteItemById(id);
      tasks.value.splice(
        tasks.value.findIndex((task) => task.id === id),
        1
      );
      return res;
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function updateTask(id, task) {
    try {
      const updatedTask = await editTask(id, task);
      const taskIndex = tasks.value.findIndex((task) => task.id === id);
      tasks.value[taskIndex] = updatedTask;
      return updatedTask;
    } catch (error) {
      throw new Error(error.message);
    }
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
