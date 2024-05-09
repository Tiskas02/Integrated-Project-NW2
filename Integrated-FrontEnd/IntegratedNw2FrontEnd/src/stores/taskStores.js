import { defineStore } from 'pinia';
import { getTaskData } from '../libs/api/fetchUtil.js';
import { ref } from 'vue';
export const useStoreTasks = defineStore('tasks', () => {
  const tasks = ref([]);
  const isLoading = ref(false);

  function convertStatus(status) {
    switch (status) {
      case "NO_STATUS":
        return "No Status";
      case "TO_DO":
        return "To Do";
      case "DOING":
        return "Doing";
      case "DONE":
        return "Done";
    }
}

async function fetchTasks() {
    try {
      tasks.value = [];
      const taskData = await getTaskData();
      taskData.forEach((task) => {
        task.status = convertStatus(task.status);
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
