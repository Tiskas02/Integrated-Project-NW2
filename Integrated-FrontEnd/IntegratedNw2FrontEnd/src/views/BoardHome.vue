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

const receiveToken = localStorage.getItem("token")
const token = parseJwt(receiveToken)
console.log(token.name)

onMounted(async () => {
  const data = await boardStore.fetchBoards()
  console.log(data)
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
    name: newBoard.name ? newBoard.name : `${token.name} Personal Board`,
  })
  if (data) {
    toasterStore.success({ text: "Board added successfully!" })
    navigateToBoardTasks(data.id)
  } else {
    toasterStore.error({
      text: "An error occurred while adding the board.",
    })
  }
}
const navigateToBoardTasks = (boardId) => {
  router.push({ name: "Task", params: { id: boardId } })
}
</script>

<template>
  <Logo />
  <div class="flex">
    <!-- SideBar -->
    <div class="w-[10%] bg-customNavColor p-4 text-white">
      <button
        class="flex w-full itbkk-button-add itbkk-button-create items-center rounded-lg text-base transition duration-75 hover:bg-gray-100 dark:text-white dark:hover:bg-slate-500 gap-2 p-2 pr-6"
        @click="setModal"
      >
        <img src="../assets/ui/Add New.svg" alt="Add" class="w-[35%]" />
        <p class="text-xl">Board</p>
      </button>
      <hr class="mt-2" />
      <label class="inline-flex items-center cursor-pointer ml-6 mt-4">
        <input type="checkbox" value="" class="sr-only peer" checked />
        <div
          class="relative w-11 h-6 bg-gray-200 rounded-full peer peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-0.5 after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"
        ></div>
      </label>
      <hr class="mt-2" />
      <button class="flex gap-1 mt-4 cursor-pointer transition duration-75 hover:bg-gray-100 dark:text-white dark:hover:bg-slate-500 rounded-lg text-base p-2">
        <img src="../assets/ui/Support.svg" alt="Modify" class="w-[25%]" />
        <p class="text-xl">Status</p>
      </button>
    </div>

    <!-- Main Content -->
    <div>
      <LoadingScreen v-if="!dataLoaded" />
      <div
        class="w-full font-rubik font-medium text-4xl text-black text-center mt-6 mb-4"
      >
        Board List
      </div>

      <div class="itbkk-collab-board w-[100%] flex flex-col p-6 overflow-auto">
        <div class="space-x-4">
          <div
            v-if="boardStore.length <= 0"
            class="w-full border bg-white rounded-b-box"
          >
            <div class="flex justify-center items-center h-full">
              <p class="text-xl font-bold animate-bounce text-slate-500">
                Board is empty
              </p>
            </div>
          </div>

          <div v-else class="overflow-auto rounded-b-box">
            <div class="flex flex-wrap justify-between gap-4">
              <div
                v-for="(board, index) in boards"
                :key="board.id"
                class="w-[30%] bg-white rounded-lg shadow-lg p-4 space-y-4"
              >
                <div
                  class="itbkk-board-name bg-blue-500 text-white font-semibold text-center p-4 rounded-md cursor-pointer hover:bg-yellow-300 hover:text-black hover:duration-200"
                  @click="navigateToBoardTasks(board.id)"
                >
                  {{ board.name }}
                </div>
                <div class="itbkk-personal-item">
                  <div class="flex justify-between items-center">
                    <div
                      class="text-black text-xl p-4 hover:duration-200 hover:text-violet-600 hover:bg-gray-200 rounded-xl cursor-pointer"
                      @click="navigateToBoardTasks(board.id)"
                    >
                      Board No: {{ index + 1 }}
                    </div>
                    <div
                      class="itbkk-button-action whitespace-nowrap flex justify-end gap-4"
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
      <teleport to="#body">
        <BoardModal
          v-if="showModal"
          @close="setClose"
          @newBoard="addBoard"
          class="itbkk-modal-task"
        />
      </teleport>
    </div>
  </div>
</template>

<style scoped></style>
