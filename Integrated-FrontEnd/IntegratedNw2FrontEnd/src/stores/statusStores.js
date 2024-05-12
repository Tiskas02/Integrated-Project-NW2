import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getStatusData , addStatus } from '@/libs/api/status/fetchUtilStatus';
export const useStoreStatus = defineStore('status', () => {
    const statuses = ref([]);
    async function fetchStatus() {
        try {
            statuses.value = [];
          const statusData = await getStatusData();
            statusData.forEach((status) => {
            statuses.value.push(status);
          });
        } catch (error) {
          console.error('Error fetching data:', error);
          console.log(error);
        }
      }
      async function createStatus(newStatus) {
        try {
          const addedStatus = await addStatus(newStatus)
          console.log(addedStatus);
          statuses.value.push(addedStatus)
        } catch (error) {
          throw new Error(error.message)
        }
      }
      return {
        statuses,
        fetchStatus,
        createStatus
      }
})