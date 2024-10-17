import { defineStore } from "pinia"
import { ref } from "vue"
import { getAllCollabDataByUserId } from "@/libs/api/collab/fetchUtilCollab"

export const useStoreCollab = defineStore("collabs", () => {
  const collabs = ref([])
    async function fetchCollabs(collabId) {
    try {
      const noData = "No data"
      collabs.value = []
    const collabData = await getAllCollabDataByUserId(collabId)
    console.log(`collabData from fetch: ${JSON.stringify(collabData, null, 2)}`);
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

  return {
    collabs,
    fetchCollabs,
  }
})