import { defineStore } from "pinia"
import { ref } from "vue"
import { getBoardData, addBoard,updateBoardVisibility } from "@/libs/api/board/fetchUtilBoard"

export const useStoreBoard = defineStore("boards", () => {
  const boards = ref([])
  async function fetchBoards() {
    try {
      const noData = "No data"
      boards.value = []
      const boardData = await getBoardData()
      boardData.forEach((board) => {
        boards.value.push(board)
      })
      console.log(boards.value);
      if (boards.value.length === 0) {
        return noData
      } else {
        console.log(boards.value);
        return boards.value
      }
    } catch (error) {
      console.error("Error fetching data:", error)
    }
  }

  async function createBoard(newBoard) {
    try {
      const addedBoard = await addBoard(newBoard)
      boards.value.push(addedBoard)
      console.log(addedBoard);
      
      return addedBoard
    } catch (error) {
      throw new Error(error.message)
    }
  }

  async function updateVisibility(id, visibility) {
    try {
      const updatedBoard = await updateBoardVisibility(id, visibility)
      const index = boards.value.findIndex((board) => board.id === id)
      boards.value[index] = updatedBoard
      return updatedBoard
    } catch (error) {
      throw new Error(error.message)
    }
  }
  function matchUserBoard(id) {
    const matchedBoard = boards.value.find((board) => board.id === id)
    if (matchedBoard) {
      return matchedBoard
    } else {
      return "Board not found"
    }
  }
  
  return {
    boards,
    fetchBoards,
    createBoard,
    matchUserBoard,
    updateVisibility
  }
})
