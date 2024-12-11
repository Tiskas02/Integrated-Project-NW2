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
    }
  }

  async function createStatus(newStatus, id) {
    try {
      const addedStatus = await addStatus(newStatus, id);
      if (addedStatus.status === undefined) {
        statuses.value.push(addedStatus);
      }
      return addedStatus;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function updateStatus(id, status, routeId) {
    try {
      const updatedStatus = await editStatus(id, status, routeId);
      const statusIndex = statuses.value.findIndex(
        (status) => status.id === id
      );
      if (statusIndex !== -1) {
        statuses.value[statusIndex].id = id;
        statuses.value[statusIndex].name = status.name;
        statuses.value[statusIndex].description = status.description;
      }
      return updatedStatus;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function deleteStatus(boardId, id) {
    try {
      const res = await deleteOneStatus(boardId, id);
      statuses.value.splice(
        statuses.value.findIndex((status) => status.id === id),
        1
      );
      return res.status;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function tranferStatus(boardId, oldId, newId) {
    try {
      const res = await deleteTranferStatus(boardId, oldId, newId);
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
