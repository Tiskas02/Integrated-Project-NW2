<script setup>
import { ref, onMounted } from "vue";
import { useStoreBoard } from "@/stores/boardStore";
import { useStoreCollab } from "@/stores/collabStore";
import LoadingScreen from "@/shared/LoadingScreen.vue";
import Logo from "@/shared/Logo.vue";
import BaseBtn from "@/shared/BaseBtn.vue";
import { storeToRefs } from "pinia";
import BoardModal from "@/components/BoardModal.vue";
import { useToasterStore } from "@/stores/notificationStores";
import { useRoute, useRouter } from "vue-router";
import NavBar from "@/shared/NavBar.vue";
const route = useRoute();
const router = useRouter();
const dataLoaded = ref(true);
const boardStore = useStoreBoard();
const collabStore = useStoreCollab();
const toasterStore = useToasterStore();
const { boards } = storeToRefs(boardStore);
const { collabs } = storeToRefs(collabStore);
const showModal = ref(false);
const storeModeNavBar = ref("");
const storeBoardType = ref("Personal");
const isDropdownOpen = ref(false);
const parseJwt = (token) => {
  const base64Url = token.split(".")[1];
  const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );
  return JSON.parse(jsonPayload);
};
const receiveToken = localStorage.getItem("token");
const token = parseJwt(receiveToken);
onMounted(async () => {
  storeModeNavBar.value = "board";
  const data = await boardStore.fetchBoards();
  const dataCollab = collabStore.addCollabDataInBoard(data[0].collabIn);
  console.log(dataCollab);
  // const collab = await collabStore.fetchCollabs(token.oid)
  if (boardStore.boards.length > 0 || data) {
    dataLoaded.value = true;
  } else {
    dataLoaded.value = false;
  }
});

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

// Function to update the board type and close the dropdown
const updateBoardType = (type) => {
  storeBoardType.value = type;
  isDropdownOpen.value = false; // Close the dropdown after selection
};
const setModal = () => {
  showModal.value = true;
};

const setClose = () => {
  showModal.value = false;
};

const addBoard = async (newBoard) => {
  const data = await boardStore.createBoard({
    name: newBoard.name ? newBoard.name : `${token.name} Personal Board`,
  });
  if (data) {
    toasterStore.success({ text: "Board added successfully!" });
    navigateToBoardTasks(data.id);
  } else {
    toasterStore.error({
      text: "An error occurred while adding the board.",
    });
  }
};
const navigateToBoardTasks = (paramId) => {
  router.push({ name: "Task", params: { id: paramId } });
};
</script>

<template>
  <NavBar :mode="storeModeNavBar" />
  <div class=" laptop:mx-24">
    <LoadingScreen v-if="!dataLoaded" />
    <div class="flex m-4">
      <div class="justify-start">
        <div class="flex items-end">Hello ,</div>
        <div class="text-xl font-bold">{{ token.name }}</div>
      </div>
      <div class="grow"></div>
      <div class="flex flex-row items-end">
        <div
          class="btn bg-gradient-to-r from-blue-700 to-blue-400 border-0 text-white"
          @click="setModal"
        >
          Create Personal board
        </div>
      </div>
    </div>
    <div class="flex justify-end mx-4">
      <div class="inline-block text-left">
        <!-- Dropdown button -->
        <div>
          <button
            type="button"
            @click="toggleDropdown"
            class="inline-flex justify-center w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            id="menu-button"
            aria-expanded="true"
            aria-haspopup="true"
          >
            {{ storeBoardType === "Personal" ? "Personal" : "Collaborator" }}
            <!-- Chevron Icon -->
            <svg
              class="-mr-1 ml-2 h-5 w-5"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
              aria-hidden="true"
            >
              <path
                fill-rule="evenodd"
                d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                clip-rule="evenodd"
              />
            </svg>
          </button>
        </div>

        <!-- Dropdown menu -->
        <div
          v-if="isDropdownOpen"
          class="origin-top-right absolute right-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
          role="menu"
          aria-orientation="vertical"
          aria-labelledby="menu-button"
          tabindex="-1"
        >
          <div class="py-1" role="none">
            <!-- Personal option -->
            <a
              href="#"
              @click.prevent="updateBoardType('Personal')"
              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900"
              role="menuitem"
              tabindex="-1"
              id="menu-item-0"
            >
              Personal
            </a>
            <!-- Collaborator option -->
            <a
              href="#"
              @click.prevent="updateBoardType('Collaberator')"
              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900"
              role="menuitem"
              tabindex="-1"
              id="menu-item-1"
            >
              Collaborator
            </a>
          </div>
        </div>
      </div>
    </div>
    <div class="px-4 text-xl font-bold mb-4">{{ storeBoardType }} Board</div>
    <div v-if="storeBoardType === 'Personal'">
      <div>
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
        <div class="flex flex-row flex-wrap justify-start mx-2">
          <div
            v-for="(board, index) in boards"
            :key="board.boards.id"
            class="m-2"
            @click="navigateToBoardTasks(board.boards.id)"
          >
            <div
              class="btn border-0 w-44 h-40 bg-slate-200 rounded-xl shadow-lg p-4 flex flex-col justify-between items-start"
            >
              <!-- Title -->
              <div>
                <h1 class="text-lg font-bold text-gray-900 text-left">
                  {{ index + 1 }} : {{ board.boards.name }}
                </h1>
                <p class="text-sm font-medium text-gray-700">
                  Visibility : {{ board.boards.visibility }}
                </p>
              </div>
              <!-- Button -->
              <!-- <button
                class="btn border-0 bg-gradient-to-r from-green-400 to-green-200 text-gray-900 text-sm font-medium shadow-md hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-400"
              >
                
              </button> -->
              <div class="flex space-x-2 ">
                <button class="btn bg-gradient-to-r from-blue-700 to-blue-400 border-0 text-white">Edit</button>
                <button class="btn bg-gradient-to-r from-red-700 to-red-400 border-0 text-white">Delete</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <div>
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
        <div class="flex flex-row flex-wrap justify-start mx-2">
          <div
            v-for="(collab, index) in collabs" :key="collabs.boardId"
            class="m-2"
            @click="navigateToBoardTasks(collab.boardId)"
          >
            <div
              class="btn border-0 w-44 h-[13rem] bg-slate-200 rounded-xl shadow-lg p-4 flex flex-col justify-between items-start"
            >
              <!-- Title -->
              <div>
                <h1 class="text-lg font-bold text-gray-900 text-left">
                  {{ index + 1 }} : {{ collab.name }}
                </h1>
                <p class="text-sm font-medium text-gray-700 text-left">
                  Owner Board : <br/>{{ collab.ownerName }}
                </p>
                <p class="text-sm font-medium text-gray-700 text-left">
                  Access Right : <br/>{{ collab.accessRight }}
                </p>
              </div>
              <!-- Button -->
              <!-- <button
                class="btn border-0 bg-gradient-to-r from-green-400 to-green-200 text-gray-900 text-sm font-medium shadow-md hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-400"
              >
                
              </button> -->
              <div class="flex space-x-2 justify-end w-full">
                <button class="btn bg-gradient-to-r from-red-700 to-red-400 border-0 text-white">Leave</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- <div class="w-full flex justify-center mt-6">
      <div class="shadow-2xl rounded-md w-[95%] h-[50%] shadow-blue-500/30">
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
          <div class="w-full h-[300px] overflow-auto rounded-b-box">
            <div v-for="(board, index) in boards" :key="board.boards.id">
              <div
                class="bg-white divide-y divide-gray-200 overflow-auto shadow-inner"
              >
                <div
                  class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 bg-slate"
                >
                  <div class="flex hover:shadow-inner hover:bg-slate-50">
                    <div
                      class="w-[30%] px-6 py-4 whitespace-nowrap text-center"
                      @click="navigateToBoardTasks(board.boards.id)"
                    >
                      {{ index + 1 }}
                    </div>
                    <div
                      class="itbkk-title w-[30%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="navigateToBoardTasks(board.boards.id)"
                    >
                      {{ board.boards.name }}
                    </div>
                    <div
                      class="itbkk-assignees w-[30%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="navigateToBoardTasks(board.boards.id)"
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
    </div> -->
    <!-- collabboard -->
    <!-- <div
      class="w-full font-rubik font-medium text-4xl text-white text-center my-6"
    >
      Collab board
    </div>
    <div class="w-full flex justify-center mt-6">
      <div class="shadow-2xl rounded-md w-[95%] h-[50%] shadow-blue-500/30">
        <div class="min-w-full divide-y divide-gray-200 overflow-auto">
          <div class="#4793AF bg-slate-800 flex rounded-md overflow-auto">
            <div
              class="w-[10%] m-auto text-start text-md font-bold text-white uppercase overflow-auto"
            ></div>
            <div
              class="w-[18%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">No</div>
            </div>
            <div
              class="w-[18%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Name</div>
            </div>
            <div
              class="w-[18%] h-14 text-center text-md font-bold text-white uppercase flex justify-center items-center"
            >
              Owner
            </div>
            <div
              class="w-[18%] h-14 text-center text-md font-bold text-white uppercase flex justify-center items-center"
            >
              Acess Right
            </div>
            <div
              class="w-[18%] h-14 text-left text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Action</div>
            </div>
          </div>
          <div
            v-if="collabStore.length <= 0"
            class="w-full border bg-white h-[60lvh] rounded-b-box"
          >
            <div class="flex justify-center items-center h-full">
              <p class="text-xl font-bold animate-bounce text-slate-500">
                Board is empty
              </p>
            </div>
          </div>
          <div v-else class="w-full h-[250px] overflow-auto rounded-b-box">
            <div v-for="(collab, index) in collabs" :key="collabs.boardId">
              <div
                class="bg-white divide-y divide-gray-200 overflow-auto shadow-inner"
              >
                <div
                  class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 bg-slate"
                >
                  <div class="flex hover:shadow-inner hover:bg-slate-50">
                    <div
                      class="w-[30%] px-6 py-4 whitespace-nowrap text-center"
                      @click="navigateToBoardTasks(collab.boardId)"
                    >
                      {{ index + 1 }}
                    </div>
                    <div
                      class="itbkk-title w-[30%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click="navigateToBoardTasks(collab.boardId)"
                    >
                      {{ collab.name }}
                    </div>
                    <div
                      class="itbkk-assignees w-[15%] px-1 py-4 whitespace-nowrap overflow-x-auto"
                      @click="navigateToBoardTasks(collab.boardId)"
                    >
                      {{ collab.ownerName }}
                    </div>
                    <div
                      class="itbkk-assignees w-[15%] px-6 py-4 whitespace-nowrap overflow-x-auto text-center"
                      @click="navigateToBoardTasks(collab.boardId)"
                    >
                      {{ collab.accessRight }}
                    </div>

                    <div
                      class="itbkk-button-action w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                    >
                      <div
                        class="itbkk-button-delete btn btn-outline btn-error"
                        @click=""
                      >
                        Leave
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div> -->
    <teleport to="#body">
      <BoardModal
        v-if="showModal"
        @close="setClose"
        @newBoard="addBoard"
        class="itbkk-modal-task"
      />
    </teleport>
  </div>
</template>

<style scoped></style>
