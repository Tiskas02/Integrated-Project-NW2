import { defineStore } from "pinia";
import { ref } from "vue";
import {
  getAllCollabDataByUserId,
  getCollabDataByBoardId,
  addCollabData,
  deleteCollabUtil,
  sendInvitationEmail,
} from "@/libs/api/collab/fetchUtilCollab";

export const useStoreCollab = defineStore("collabs", () => {
  const collabs = ref([]);
  async function fetchCollabs(collabId) {
    try {
      const noData = "No data";
      collabs.value = [];
      const collabData = await getAllCollabDataByUserId(collabId);
      collabData.boards.forEach((collab) => {
        collabs.value.push(collab);
      });
      console.log(collabs.value);
      if (collabs.value.length === 0) {
        return noData;
      } else {
        return collabs.value;
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  }
  function addCollabDataInBoard(collab) {
    collabs.value = [];
    collab.forEach((item) => {
      collabs.value.push(item);
    });
    return collabs.value;
  }
  async function fetchCollabsByBoardId(boardId) {
    try {
      collabs.value = [];
      const collabData = await getCollabDataByBoardId(boardId);

      if (collabData.error) {
        return collabData;
      } else if (collabData.status === 403) {
        return collabData;
      } else {
        collabData.forEach((collab) => {
          collabs.value.push(collab);
        });
        return collabData;
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  }
  async function addCollab(collab, routeId) {
    try {
      const newCollab = await addCollabData(collab, routeId);
      if (newCollab) {
        console.log("New collab:", newCollab);
        collabs.value.push(newCollab);
      }
      return newCollab;
    } catch (error) {
      console.error("Error adding collaborator:", error);
      return null;
    }
  }

  async function deleteCollab(collabId, routeId) {
    try {
      const res = await deleteCollabUtil(collabId, routeId);
      collabs.value.splice(
        collabs.value.findIndex((collab) => collab.oid === collabId),
        1
      );
      return res;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function sendInvitation(newCollab, boardId) {
    try {
      const response = await sendInvitationEmail(newCollab, boardId);
      return response;
    } catch (error) {
      console.error("Error sending invitation:", error);
      throw new Error(error.message);
    }
  }

  return {
    collabs,
    fetchCollabs,
    fetchCollabsByBoardId,
    addCollab,
    deleteCollab,
    addCollabDataInBoard,
    sendInvitation,
  };
});
