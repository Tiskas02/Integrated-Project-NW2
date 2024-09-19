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
  async function fetchStatus(id) {
    try {
      statuses.value = [];
      const statusData = await getStatusData(id);
      statuses.value = statusData ?? [];
      return statuses.value;
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
    }
  }
  async function createStatus(newStatus,id) {
    try {
      const addedStatus = await addStatus(newStatus,id);    
      if (addedStatus.status === undefined) {
        statuses.value.push(addedStatus);
      }
      return addedStatus;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function updateStatus(id, status,routeId) {
    try {
      const updatedStatus = await editStatus(id, status,routeId);
      console.log(updateStatus);
      
      const statusIndex = statuses.value.findIndex(
        (status) => status.id === id
      );
      updatedStatus.name = updatedStatus.statusName;
      delete updatedStatus.statusName;
      // statuses.value.splice(statusIndex, 1, updatedStatus);
      console.log(statuses.value[statusIndex]);
      console.log(updatedStatus);
      statuses.value[statusIndex] = updatedStatus;
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
