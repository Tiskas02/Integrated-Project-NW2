<script setup>
import { ref, onMounted } from "vue";
import { useStoreBoard } from "@/stores/boardStore";
import { useStoreCollab } from "@/stores/collabStore";
import LoadingScreen from "@/shared/LoadingScreen.vue";
import { storeToRefs } from "pinia";
import BoardModal from "@/components/BoardModal.vue";
import { useToasterStore } from "@/stores/notificationStores";
import { useRoute, useRouter } from "vue-router";
import NavBar from "@/shared/NavBar.vue";
import BoardDelete from "./BoardDelete.vue";
import BoardEdit from "@/components/BoardEdit.vue";
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
const isBoardnull = ref(false);
const routeId = ref(route.params.id);
const showDeleteModal = ref(false);
const storeBoard = ref("");
const showEditModal = ref(false);
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
  if (data[0].id === null) {
    isBoardnull.value = true;
  }
  const dataCollab = collabStore.addCollabDataInBoard(data[0].collabIn);
  if (boardStore.boards.length > 0 || data) {
    dataLoaded.value = true;
  } else {
    dataLoaded.value = false;
  }
});

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const updateBoardType = (type) => {
  storeBoardType.value = type;
  isDropdownOpen.value = false;
};
const setModal = () => {
  router.push({ name: "addBoard" });
  showModal.value = true;
};

const setClose = () => {
  showModal.value = false;
};
const setDelete = (id) => {
  boards.value.forEach((board) => {
    if (board.id === id) {
      storeBoard.value = board;
    }
  });
  showDeleteModal.value = true;
};
const setCloseDelete = () => {
  showDeleteModal.value = false;
};
const setEdit = (id) => {
  router.push({ name: "editBoard", params: { editid: id } });
  boards.value.forEach((board) => {
    if (board.id === id) {
      storeBoard.value = board;
    }
  });
  showEditModal.value = true;
};
const setCloseEdit = () => {
  showEditModal.value = false;
};
const editBoard = async (board) => {
  const data = await boardStore.updateBoard(board.id, {
    name: board.name,
  });
  if (data) {
    toasterStore.success({ text: "Board edited successfully!" });
  } else {
    toasterStore.error({
      text: "An error occurred while editing the board.",
    });
  }
};
const deleteBoard = async (boardId) => {
  const res = await boardStore.deleteBoard(boardId);
  if (res === 200) {
    toasterStore.success({ text: "Board deleted successfully!" });
  } else {
    toasterStore.error({ text: "Error deleting board" });
  }
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
const navigateToInvitation = (paramId) => {
  router.push({ name: "invitation", params: { id: paramId } });
};
function formatDate(isoDate) {
  const date = new Date(isoDate);
  return date.toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    timeZone: "UTC",
  });
}
const leaveBoard = async (collabId) => {
  const res = await collabStore.deleteCollab(token.oid, collabId);
  if (res.status === 200) {
    toasterStore.success({ text: "Leave Collab Board successfully!" });
  } else {
    toasterStore.error({ text: "Error Declined Collab Board" });
  }
};
</script>

<template>
  <NavBar :mode="storeModeNavBar" />
  <div class="laptop:mx-24">
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
          class="origin-top-right absolute right-0 laptop:right-[110px] mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
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
              @click.prevent="updateBoardType('Collaborator')"
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
          v-if="boards.length <= 0 || isBoardnull === true"
          class="w-full h-[60lvh]"
        >
          <div class="flex justify-center items-center h-full">
            <p class="text-xl font-bold animate-bounce text-slate-500">
              Personal Board is empty
            </p>
          </div>
        </div>
        <div v-else class="flex flex-row flex-wrap justify-start mx-2">
          <div
            v-for="(board, index) in boards"
            :key="board.id"
            class="m-2"
           
          >
            <div
              class="btn border-0 w-[380px] h-60 laptop:w-[350px] rounded-xl shadow-lg p-4 flex flex-col justify-between items-start"
              style="
                background-image: url('https://res.cloudinary.com/dyhavbbzf/image/upload/v1733736378/msdzivjvn8byjiikg2cc.jpg');
                background-size: cover;
                background-position: center;
              "
            >
              <div @click="navigateToBoardTasks(board.id)">
                <h1 class="text-3xl font-bold text-gray-900 text-left">
                  {{ board.name }}
                </h1>
                <p class="text-sm font-medium text-gray-700 text-left">
                  Board Visibility : {{ board.visibilities }}
                </p>
                <p class="text-sm font-medium text-gray-700 text-left">
                  Created : {{ formatDate(board.createdOn) }}
                </p>
              </div>
              <div class="flex space-x-2 w-full">
                <button
                  class="btn bg-gradient-to-r from-blue-700 to-blue-400 border-0 text-white"
                  @click="setEdit(board.id)"
                >
                  Edit
                </button>
                <button
                  class="btn bg-gradient-to-r from-red-700 to-red-400 border-0 text-white"
                  @click="setDelete(board.id)"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <div>
        <div v-if="collabs.length <= 0" class="w-full h-[60lvh]">
          <div class="flex justify-center items-center h-full">
            <p class="text-xl font-bold animate-bounce text-slate-500">
              Collaborator Board is empty
            </p>
          </div>
        </div>
        <div class="flex flex-row flex-wrap justify-start mx-2">
          <div
            v-for="(collab, index) in collabs"
            :key="collab.boardId"
            class="m-2"
            @click="
              collab.status !== 'PENDING' &&
                navigateToBoardTasks(collab.boardId)
            "
          >
            <div
              class="btn border-0 w-[380px] h-60 laptop:w-[350px] rounded-xl shadow-lg p-4 flex flex-col justify-between items-start"
              style="
                background-image: url('https://res.cloudinary.com/dyhavbbzf/image/upload/v1733736425/rggdluwhpkfptb457uhf.png');
                background-size: cover;
                background-position: center;
              "
            >
              <!-- Title -->
              <div>
                <h1 class="text-lg font-bold text-gray-900 text-left">
                  {{ index + 1 }} : {{ collab.name }}
                </h1>
                <p class="text-sm font-medium text-gray-700 text-left">
                  Owner Board : {{ collab.ownerName }}
                </p>
                <p class="text-sm font-medium text-gray-700 text-left">
                  Access Right : {{ collab.accessRight }}
                </p>
                <p
                  class="text-sm font-medium text-gray-700 text-left"
                  :class="{
                    'text-yellow-500': collab.status === 'PENDING',
                    'text-green-500': collab.status === 'ACCEPTED',
                  }"
                >
                  Status : {{ collab.status }}
                </p>
              </div>
              <div class="flex space-x-2 justify-end w-full">
                <div
                  v-if="collab.status === 'PENDING'"
                  class="tooltip tooltip-info"
                  data-tip="go to invitation page "
                >
                  <button
                    class="btn bg-gradient-to-r from-blue-700 to-blue-400 border-0 text-white"
                    @click="navigateToInvitation(collab.boardId)"
                  >
                    Invitation Page
                  </button>
                </div>
                <button
                  class="btn bg-gradient-to-r from-red-700 to-red-400 border-0 text-white"
                  @click="leaveBoard(collab.boardId)"
                >
                  Leave
                </button>
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
      />
    </teleport>
    <teleport to="#body">
      <BoardDelete
        v-if="showDeleteModal"
        @close="setCloseDelete"
        @savedDelete="deleteBoard"
        :board="storeBoard"
      />
    </teleport>
    <teleport to="#body">
      <BoardEdit
        v-if="showEditModal"
        @close="setCloseEdit"
        @newBoard="editBoard"
        :board="storeBoard"
      />
    </teleport>
  </div>
</template>

<style scoped></style>
