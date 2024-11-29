import { defineStore } from "pinia";
import { ref } from "vue";
import {
  addTask,
  deleteItemById,
  editTask,
  getTaskDataInBoardId,
  getBoardByBoardId,
} from "../libs/api/task/fetchUtilTask.js";

export const useStoreTasks = defineStore("tasks", () => {
  const tasks = ref([]);
  async function fetchTasks(id, collabId) {
    try {
      tasks.value = [];
      const taskData = await getTaskDataInBoardId(id, collabId);
      tasks.value = taskData;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  }

  async function createTask(task, id) {
    try {
      const addedTask = await addTask(task, id);
      tasks.value.push(addedTask);
      return addedTask;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function deleteTask(routeId, id) {
    try {
      const res = await deleteItemById(routeId, id);
      tasks.value.splice(
        tasks.value.findIndex((task) => task.id === id),
        1
      );
      return res;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function updateTask(routeId, id, task) {
    try {
      const updatedTask = await editTask(routeId, id, task);
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
      console.log(tasks.value);
    } else if (sortOrder === "ASC") {
      tasks.value.sort((a, b) => b.status.name.localeCompare(a.status.name));
      console.log(tasks.value);
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
