import { defineStore } from "pinia"
import { ref } from "vue"
import { getAllCollabDataByUserId,getCollabDataByBoardId,addCollabData,deleteCollabUtil } from "@/libs/api/collab/fetchUtilCollab"

export const useStoreCollab = defineStore("collabs", () => {
  const collabs = ref([])
  async function fetchCollabs(collabId) {
    try {
      const noData = "No data"
      collabs.value = []
    const collabData = await getAllCollabDataByUserId(collabId)
    collabData.boards.forEach((collab) => {
        collabs.value.push(collab);
    });
      if (collabs.value.length === 0) {
        return noData
      } else {
        return collabs.value
      }
    } catch (error) {
      console.error("Error fetching data:", error)
    }
  }
  function addCollabDataInBoard(collab) {
    collabs.value = []
    collab.forEach((item) => {
      collabs.value.push(item);
    });
    return collabs.value
  }
  async function fetchCollabsByBoardId(boardId) {
    try {
      const noData = "No data"
      collabs.value = []
    const collabData = await getCollabDataByBoardId(boardId)
    collabData.forEach((collab) => {
        collabs.value.push(collab);
    });
      if (collabs.value.length === 0) {
        return noData
      } else {
        return collabs.value
      }
    } catch (error) {
      console.error("Error fetching data:", error)
    }
  }
  async function addCollab(collab,routeId) {
    try {
      const newCollab = await addCollabData(collab,routeId)
      collabs.value.push(newCollab)
      return newCollab
    } catch (error) {
      console.error("Error fetching data:", error)
    }
  }
  async function deleteCollab(collabId,routeId) {
    try {
      const res = await deleteCollabUtil(collabId,routeId)
      collabs.value.splice(
        collabs.value.findIndex((collab) => collab.oid === collabId),
        1
      )
      return res
    } catch (error) {
      throw new Error(error.message)
    }
  }


  return {
    collabs,
    fetchCollabs,
    fetchCollabsByBoardId,
    addCollab,
    deleteCollab,
    addCollabDataInBoard
  }
})