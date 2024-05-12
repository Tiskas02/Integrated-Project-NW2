import { defineStore } from 'pinia';
import { getTaskData } from '../libs/api/fetchUtil.js';
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
  

  
  
  return {
    tasks,
    fetchTasks,
    isLoading
  };
});
