<script setup>
import { ref, onMounted } from "vue"
import { useStoreBoard } from "@/stores/boardStore"
import LoadingScreen from "@/shared/LoadingScreen.vue"
import Logo from "@/shared/Logo.vue"
import BaseBtn from "@/shared/BaseBtn.vue"
import { storeToRefs } from "pinia"
import BoardModal from "@/components/BoardModal.vue"
import { useToasterStore } from "@/stores/notificationStores"
import { useRoute, useRouter } from "vue-router"
const route = useRoute()
const router = useRouter()
const dataLoaded = ref(true)
const boardStore = useStoreBoard()
const toasterStore = useToasterStore()
const { boards } = storeToRefs(boardStore)
const showModal = ref(false)
const parseJwt = (token) => {
  const base64Url = token.split(".")[1]
  const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/")
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2)
      })
      .join("")
  )
  return JSON.parse(jsonPayload)
}

onMounted(async () => {
  const data = await boardStore.fetchBoards()
  if (boardStore.boards.length > 0 || data) {
    dataLoaded.value = true
  } else {
    dataLoaded.value = false
  }
})

const setModal = () => {
  showModal.value = true
}

const setClose = () => {
  showModal.value = false
}

const addBoard = async (newBoard) => {
  const data = await boardStore.createBoard({
    name: newBoard.name,
  })
  if (data) {
    toasterStore.success({ text: "Board added successfully!" })
    navigateToBoardTasks(data.boardId)
  } else {
    toasterStore.error({
      text: "An error occurred while adding the board.",
    })
  }
}
const navigateToBoardTasks = (boardId) => {
  console.log(boardId , `${boardId} boardId`);
  router.push({ name: "Task", params: { id: boardId } })
}
</script>

<template>
  <div>
    <LoadingScreen v-if="!dataLoaded" />
    <Logo>
      <template #image>
        <img src="/icon.png" alt="icon" class="w-[10%] m-2" />
      </template>
      <template #text>
        <div class="font-chivo font-medium text-xl text-white mx-1">
          IT-Bangmod Kradan Kanban
        </div>
      </template>
    </Logo>
    <div
      class="w-full font-rubik font-medium text-4xl text-white text-center my-6"
    >
      Board List
    </div>
    <div class="flex justify-end mx-10" @click="setModal">
      <BaseBtn>
        <template #default>
          <button class="itbkk-button-create p-4">Create personal board</button>
        </template>
      </BaseBtn>
    </div>
    <div class="w-full flex justify-center mt-6">
      <div class="shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/30">
        <div class="min-w-full divide-y divide-gray-200 overflow-auto">
          <div class="#4793AF bg-slate-800 flex rounded-md overflow-auto">
            <div
              class="w-[10%] m-auto text-start text-md font-bold text-white uppercase overflow-auto"
            ></div>
            <div
              class="w-[22%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">No</div>
            </div>
            <div
              class="w-[22%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Name</div>
            </div>
            <div
              class="w-[22%] h-14 text-center text-md font-bold text-white uppercase flex justify-center items-center"
            ></div>
            <div
              class="w-[22%] h-14 text-left text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Action</div>
            </div>
          </div>
          <div
            v-if="boardStore.length <= 0"
            class="w-full border bg-white h-[60lvh] rounded-b-box"
          >
            <div class="flex justify-center items-center h-full">
              <p class="text-xl font-bold animate-bounce text-slate-500">
                Board is empty
              </p>
            </div>
          </div>
          <div v-else class="w-full h-[500px] overflow-auto rounded-b-box">
            <div v-for="(board, index) in boards" :key="board.boardId">
              <div
                class="bg-white divide-y divide-gray-200 overflow-auto shadow-inner"
              >
                <div
                  class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 bg-slate"
                >
                  <div class="flex hover:shadow-inner hover:bg-slate-50">
                    <div
                      class="w-[30%] px-6 py-4 whitespace-nowrap text-center"
                      @click="navigateToBoardTasks(board.boardId)"
                    >
                      {{ index + 1 }}
                    </div>
                    <div
                      class="itbkk-title w-[30%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="navigateToBoardTasks(board.boardId)"
                    >
                      {{ board.name }}
                    </div>
                    <div
                      class="itbkk-assignees w-[30%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="navigateToBoardTasks(board.boardId)"
                    ></div>
                    <div
                      class="itbkk-button-action w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                    >
                      <div
                        class="itbkk-button-edit btn btn-outline btn-warning"
                        @click=""
                      >
                        Edit
                      </div>
                      <div
                        class="itbkk-button-delete btn btn-outline btn-error"
                        @click=""
                      >
                        Delete
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <teleport to="#body">
      <BoardModal v-if="showModal" @close="setClose" @newBoard="addBoard" />
    </teleport>
  </div>
</template>

<style scoped></style>
