import { defineStore } from "pinia";
import { ref } from "vue";
import {
  getStatusData,
  addStatus,
  editStatus,
} from "@/libs/api/status/fetchUtilStatus";

export const useStoreStatus = defineStore("status", () => {
  const statuses = ref([]);
  async function fetchStatus() {
    try {
      statuses.value = [];
      const statusData = await getStatusData();
      statuses.value = statusData ?? [];
      return statuses.value;
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
    }
  }
  async function createStatus(newStatus) {
    try {
      const addedStatus = await addStatus(newStatus);
      statuses.value.push(addedStatus);
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function updateStatus(statusId, status) {
    try {
      const updatedStatus = await editStatus(statusId, status); // Call editStatus function
      const statusIndex = statuses.value.findIndex(
        (status) => status.statusId === statusId
      );
      statuses.value.splice(statusIndex, 1, updatedStatus);
    } catch (error) {
      throw new Error(error.message);
    }
  }

  return {
    statuses,
    fetchStatus,
    createStatus,
    updateStatus,
  };
});
