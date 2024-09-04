import { defineStore } from "pinia";
import { ref } from "vue";
import { getBoardData } from "@/libs/api/board/fetchUtilBoard";

export const useStoreBoard = defineStore("boards", () => {
    const boards = ref([]);
    async function fetchBoards() {
      try {
        boards.value = [];
        const boardData = await getBoardData();
        boardData.forEach((board) => {
          boards.value.push(board);
        });
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
    return {
      boards,
      fetchBoards,
    };
});