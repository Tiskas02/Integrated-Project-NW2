import { defineStore } from "pinia";
import { ref } from "vue";
import {
  getBoardData,
  addBoard,
  updateBoardVisibility,
  getBoardDataByCollabId,
  editBoard,
  deleteBoardById,
} from "@/libs/api/board/fetchUtilBoard";

export const useStoreBoard = defineStore("boards", () => {
  const boards = ref([]);
  const nameCollab = ref([]);

  async function fetchBoards() {
    try {
      const noData = "No data";
      boards.value = [];
      const boardData = await getBoardData();
      boardData.forEach((board) => {
        boards.value.push(board);
      });
      if (boards.value.length === 0) {
        return noData;
      } else {
        return boards.value;
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  }
  async function deleteBoard(boardId) {
    try {
      const res = await deleteBoardById(boardId);
      boards.value.splice(
        boards.value.findIndex((board) => board.id === boardId),
        1
      );
      return res;
    } catch (error) {
      throw new Error(error.message);
    }
  }
  async function updateBoard(boardId,board) {
    try {
      const updatedBoard = await editBoard(boardId, board);
      const boardIndex = boards.value.findIndex((board) => board.id === boardId);
      boards.value[boardIndex] = updatedBoard;
      
      // boards.value[boardIndex] = {
      //   ...updatedBoard,
      //   status: {
      //     id: updatedBoard.status.statusId,
      //     name: updatedBoard.status.statusName,
      //   },
      // };
      return updatedBoard;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  async function fetchBoardsByCollabId(boardId, collabId) {
    try {
      const noData = "No data";
      nameCollab.value = [];
      const boardData = await getBoardDataByCollabId(boardId, collabId);
      nameCollab.value.push(boardData);
      if (nameCollab.value.length === 0) {
        return noData;
      } else {
        return nameCollab.value;
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

  async function updateVisibility(id, visibility) {
    try {
      const updatedBoard = await updateBoardVisibility(id, visibility);
      const index = boards.value.findIndex((board) => board.id === id);
      boards.value[index] = {
        ...boards.value[index],
        visibilities: updatedBoard.visibility,
        collabIn: [...boards.value[index].collabIn],
      };
      return updatedBoard;
    } catch (error) {
      throw new Error(error.message);
    }
  }

  function matchUserBoard(id) {
    const matchedBoard = boards.value.find((board) => board.boards.id === id);

    if (matchedBoard) {
      return matchedBoard;
    } else {
      return "Board not found";
    }
  }

  return {
    boards,
    fetchBoards,
    createBoard,
    matchUserBoard,
    updateVisibility,
    fetchBoardsByCollabId,
    deleteBoard,
    updateBoard
  };
});
