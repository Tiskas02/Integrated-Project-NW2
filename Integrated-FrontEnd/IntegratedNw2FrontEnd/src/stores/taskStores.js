import { defineStore } from 'pinia';
import {  getTaskData , getTaskById , addTask , deleteItemById , updateTask } from '../libs/api/fetchUtil.js';
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
<<<<<<< HEAD
  

=======

  async function fetchTasksById(id) {
    try {
      tasks.value = [];
      const taskData = await 
    } catch (error) {
      
    }
  }
>>>>>>> win
  
  async function addTask(task) {
    try {
      const addedTask = await addTask(task);
      tasks.value.push(addedTask);
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function deleteTask(taskId) {
    try {
      await removeTask(taskId);
      tasks.value = tasks.value.filter(task => task.id !== taskId);
    } catch (error) {
      throw new Error(error.message);
    }
  }
  
  return {
    tasks,
    fetchTasks,
    addTask,
    deleteTask,

    isLoading
  };
});
