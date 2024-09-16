import { defineStore } from "pinia";
import { ref } from "vue";
import { getBoardData,addBoard } from "@/libs/api/board/fetchUtilBoard";

export const useStoreBoard = defineStore("boards", () => {
    const boards = ref([]);
    async function fetchBoards() {
      try {
        const noData = 'No data';
        boards.value = [];
        const boardData = await getBoardData();
        boardData.forEach((board) => {
          boards.value.push(board);
        });
        if (boards.value.length === 0) {
          return noData;
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
    async function createBoard(newBoard) {
      try {
        const addedBoard = await addBoard(newBoard);
        boards.value.push(addedBoard);
        return addedBoard;
      } catch (error) {
        throw new Error(error.message);
      }
    }
    return {
      boards,
      fetchBoards,
      createBoard,
    };
});