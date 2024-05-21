import { defineStore } from "pinia";
import { ref } from "vue";
import {
  getStatusData,
  addStatus,
  editStatus,
  deleteOneStatus,
  deleteTranferStatus
} from "@/libs/api/status/fetchUtilStatus";
import { useStoreLimit } from "./limitStores";

export const useStoreStatus = defineStore("status", () => {
  const statuses = ref([]);
  const limitStore = useStoreLimit();
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
      // Check if the status is already in the statuses array 
      if (limitStore.isLimitEnabled && countTasksInStatus(newStatus.status) >= limitStore.maxTasks.value) {
        throw new Error(`The status ${newStatus.status} will have too many tasks. Please make progress and update status of existing tasks first.`);
      }
      const addedStatus = await addStatus(newStatus);
      statuses.value.push(addedStatus);
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function updateStatus(id, status) {
    try {
      // Check if the status is already in the statuses array and get the existing status 
      const existingStatus = statuses.value.find(s => s.id === id);
      if (limitStore.isLimitEnabled && existingStatus.status !== status && countTasksInStatus(status) >= limitStore.maxTasks.value) {
        throw new Error(`The status ${status} will have too many tasks. Please make progress and update status of existing tasks first.`);
      }
      // Call editStatus function
      const updatedStatus = await editStatus(id, status); // Call editStatus function
      const statusIndex = statuses.value.findIndex(
        (status) => status.id === id
      );
      statuses.value.splice(statusIndex, 1, updatedStatus);
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function deleteStatus(id) {
    try {
      const res = await deleteOneStatus(id);
      // statuss.value.map((status) => {console.log(status);})
      statuses.value.splice(
        statuses.value.findIndex((status) => status.id === id),
        1
      );
      return res.status
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function tranferStatus(oldId, newId) {
    try {
      const oldStatusTasks = statuses.value.filter(s => s.status === oldId).length;
      const newStatusTasks = statuses.value.filter(s => s.status === newId).length;

      if (limitStore.isLimitEnabled && (newStatusTasks + oldStatusTasks) > limitStore.maxTasks.value) {
        throw new Error(`Cannot transfer to ${newId} status since it will exceed the limit. Please choose another status to transfer to.`);
      }

      await deleteTranferStatus(oldId, newId);
      // statuss.value.map((status) => {console.log(status);})
      statuses.value.splice(
        statuses.value.findIndex((status) => status.id === oldId),
        1
      );
    } catch (error) {
      throw new Error(error.message);
    }

    function countTasksInStatus(status) {
      return statuses.value.filter(task => task.status === status).length;
    }
  }

  return {
    statuses,
    fetchStatus,
    createStatus,
    updateStatus,
    deleteStatus,
    tranferStatus,
    
  };
});
