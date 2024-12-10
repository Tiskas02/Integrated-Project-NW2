import { defineStore } from "pinia"
import { ref } from "vue"
import { getBoardData, addBoard,updateBoardVisibility,getBoardDataByCollabId } from "@/libs/api/board/fetchUtilBoard"

export const useStoreBoard = defineStore("boards", () => {
  const boards = ref([])
  const nameCollab = ref([])

  async function fetchBoards() {
    try {
      const noData = "No data"
      boards.value = []
      const boardData = await getBoardData()
      boardData.forEach((board) => {
        boards.value.push(board)
      })
      if (boards.value.length === 0) {
        return noData
      } else {
        return boards.value
      }
    } catch (error) {
      console.error("Error fetching data:", error)
    }
  }

  async function fetchBoardsByCollabId(boardId,collabId) {
    try {
      const noData = "No data"
      nameCollab.value = []
      const boardData = await getBoardDataByCollabId(boardId,collabId)
        nameCollab.value.push(boardData)
      if (nameCollab.value.length === 0) {
        return noData
      } else {
        return nameCollab.value
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
      console.log(id);
      
      const updatedBoard = await updateBoardVisibility(id, visibility)
      const index = boards.value.findIndex((board) => board.boards.id === id)
      boards.value[index] = {
        boards: {
          ...boards.value[index].boards,
          visibility: updatedBoard.visibility
        },
        collabIn:[
          ...boards.value[index].collabIn
      ]

      }
      return updatedBoard
    } catch (error) {
      throw new Error(error.message)
    }
  }
  

  function matchUserBoard(id) {
    const matchedBoard = boards.value.find((board) => board.boards.id === id)
    console.log(matchedBoard);
    
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
    updateVisibility,
    fetchBoardsByCollabId
  }
})
