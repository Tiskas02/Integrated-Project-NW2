import { defineStore } from "pinia";
import { ref } from "vue";
import {
  getStatusData,
  addStatus,
  editStatus,
  deleteOneStatus,
  deleteTranferStatus,
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
      if (addedStatus.status === undefined) {
        statuses.value.push(addedStatus);
      }
      return addedStatus;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function updateStatus(id, status) {
    console.log(status);
    try {
      const updatedStatus = await editStatus(id, status); 
      const statusIndex = statuses.value.findIndex(
        (status) => status.id === id
      );
      statuses.value.splice(statusIndex, 1, updatedStatus);
      return updatedStatus;
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function deleteStatus(id) {
    try {
      const res = await deleteOneStatus(id);
      statuses.value.splice(
        statuses.value.findIndex((status) => status.id === id),
        1
      );
      return res.status;
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function tranferStatus(oldId, newId) {
    try {
      const res = await deleteTranferStatus(oldId, newId);
      statuses.value.splice(
        statuses.value.findIndex((status) => status.id === oldId),
        1
      );
      return res.status;
    } catch (error) {
      throw new Error(error.message);
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
