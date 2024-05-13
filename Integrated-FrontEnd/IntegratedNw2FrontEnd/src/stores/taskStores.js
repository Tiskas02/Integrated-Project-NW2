import { defineStore } from 'pinia';
import {  getTaskData , getTaskById , addTask , deleteItemById , editTask } from '../libs/api/task/fetchUtilTask.js';
import { ref } from 'vue';
export const useStoreTasks = defineStore('tasks', () => {
  const tasks = ref([]);
  const isLoading = ref(false);



async function fetchTasks() {
    try {
      tasks.value = [];
      const taskData = await getTaskData();
      taskData.forEach((task) => {
        tasks.value.push(task);
      });
      if (tasks.value.length > 0) {
        isLoading.value = true;
      }
    } catch (error) {
      console.error('Error fetching data:', error);
      console.log(error);
    }
  }
  
  async function createTask(task) {
    try {
      const addedTask = await addTask(task);
      console.log(addedTask);
      tasks.value.push(addedTask);
      return addedTask;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function deleteTask(taskId) {
    try {
      await deleteItemById(taskId);
      // tasks.value.map((task) => {console.log(task);})
      const deleted = tasks.value.splice(
        tasks.value.findIndex((task) => task.taskId === taskId),
        1
      );
      
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function updateTask(taskId, task ){
    try {
      const updatedTask = await editTask(taskId, task)
      console.log(updatedTask);
      const taskIndex = tasks.value.findIndex((task) => task.taskId === taskId)
      console.log(taskIndex);
      tasks.value[taskIndex] = updatedTask
    } catch (error) {
      throw new Error(error.message)
    }
  }
  
  return {
    tasks,
    fetchTasks,
    createTask,
    deleteTask,
    updateTask
  };
});
