<script setup>
import { ref, onMounted } from "vue";
import StatusModal from "./StatusModal.vue";
import { useStoreStatus } from "@/stores/statusStores";
import { useStoreCollab } from "@/stores/collabStore";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useStoreBoard } from "@/stores/boardStore";
import { getStatusDataById } from "@/libs/api/status/fetchUtilStatus.js";
import { useToasterStore } from "@/stores/notificationStores";
import BaseBtn from "@/shared/BaseBtn.vue";
import AddCollab from "./AddCollab.vue";
import CollabDelete from "@/views/CollabDelete.vue";
const route = useRoute();
const router = useRouter();
const showModal = ref(false);
const showDeleteModal = ref(false);
const storeMode = ref("");
const boardStore = useStoreBoard();
const statusStore = useStoreStatus();
const collabStore = useStoreCollab();
const { statuses } = storeToRefs(statusStore);
const { collabs } = storeToRefs(collabStore);
const dataCollab = ref();
const deleteCollab = ref();
const toasterStore = useToasterStore();
const routeId = ref(route.params.id);
const nameBoard = ref();
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
const accessRights = ref("WRITE");
onMounted(async () => {
  const dataBoard = await boardStore.fetchBoards(token.oid);
  const collab = await collabStore.fetchCollabsByBoardId(routeId.value);
  if (collab.length !== 0) {
    collab.forEach((item) => {
      if (item.oid === token.oid) {
        accessRights.value = item.accessRight;
      }
    });
  } else {
    accessRights.value = "WRITE";
  }
  if (collab.status === 403) {
    toasterStore.error({ text: collab.message, setTimeout: 8000 });
    router.push({ name: "board" });
  }
  if (collab.error) {
    toasterStore.error({ text: `error : ${collab.error}` });
    router.push({ name: "board" });
  }
  nameBoard.value = dataBoard[0].name;
  dataCollab.value = collab;
});

const setDataCollab = (value, collab) => {
  showDeleteModal.value = value;
  deleteCollab.value = collab;
};

const deleteCollabFunc = async (collab) => {
  if (collab === deleteCollab.value) {
    const res = await collabStore.deleteCollab(collab.oid, routeId.value);
    if (res.status === 200) {
      toasterStore.success({ text: "Collaborator deleted successfully!" });
    } else {
      toasterStore.error({
        text: "An error occurred while deleting the collaborator.",
      });
    }
  }
};

const setClose = () => {
  showModal.value = false;
};
const setCloseDelete = () => {
  showDeleteModal.value = false;
};
const addCollab = async (newCollab) => {
  const data = await collabStore.addCollab(newCollab, routeId.value);
  if (data) {
    toasterStore.success({ text: "Collaborator added successfully!" });
  } else {
    toasterStore.error({
      text: "An error occurred while adding the collaborator.",
    });
  }
};

const setModal = (value) => {
  showModal.value = value;
};
</script>

<template>
  <div class="w-full flex flex-row mx-2">
    <div>
      <div class="ml-2">Welcome to ,</div>
      <div
        class="itbkk-board-name font-rubik font-medium text-xl text-slate-800 ml-2 cursor-pointer hover:bg-gradient-to-r from-blue-600 via-green-500 to-indigo-400 hover:inline-block hover:text-transparent hover:bg-clip-text hover:duration-500"
      >
        {{ nameBoard }}
      </div>
    </div>
    <div class="grow"></div>
    <div
      class="itbkk-button-add btn border-none bg-gradient-to-r from-blue-700 to-blue-400 flex tablet:mx-8 mx-4"
      @click="accessRights !== 'READ' && setModal(true, 'add', null)"
      :class="{
        'opacity-50 cursor-not-allowed': accessRights === 'READ',
      }"
    >
      <svg
        width="20"
        height="20"
        viewBox="0 0 20 20"
        fill="#334155"
        xmlns="http://www.w3.org/2000/svg"
      >
        <g clip-path="url(#clip0_529_11)">
          <path
            d="M11 9H15V11H11V15H9V11H5V9H9V5H11V9ZM10 20C7.34784 20 4.8043 18.9464 2.92893 17.0711C1.05357 15.1957 0 12.6522 0 10C0 7.34784 1.05357 4.8043 2.92893 2.92893C4.8043 1.05357 7.34784 0 10 0C12.6522 0 15.1957 1.05357 17.0711 2.92893C18.9464 4.8043 20 7.34784 20 10C20 12.6522 18.9464 15.1957 17.0711 17.0711C15.1957 18.9464 12.6522 20 10 20ZM10 18C12.1217 18 14.1566 17.1571 15.6569 15.6569C17.1571 14.1566 18 12.1217 18 10C18 7.87827 17.1571 5.84344 15.6569 4.34315C14.1566 2.84285 12.1217 2 10 2C7.87827 2 5.84344 2.84285 4.34315 4.34315C2.84285 5.84344 2 7.87827 2 10C2 12.1217 2.84285 14.1566 4.34315 15.6569C5.84344 17.1571 7.87827 18 10 18Z"
            fill="white"
          />
        </g>
        <defs>
          <clipPath id="clip0_529_11">
            <rect width="20" height="20" fill="white" />
          </clipPath>
        </defs>
      </svg>
      <div class="text-white">Add Collaborators</div>
    </div>
  </div>
  <div
    class="flex justify-center text-2xl font-bold text-gray-900 ml-4 my-4 hover:bg-gradient-to-r from-blue-800 via-blue-400 to-blue-200 hover:text-transparent hover:bg-clip-text hover:duration-500"
  >
    Collaborator Management
  </div>
  <div class="w-full flex justify-center">
    <div class="min-w-full divide-y divide-gray-200">
      <div v-if="collabs.length <= 0" class="w-full border-none h-24">
        <div class="flex justify-center items-center h-full z-0">
          <p class="text-xl font-bold text-slate-500">No Collaborator</p>
        </div>
      </div>
      <div class="flex flex-row flex-wrap justify-start mx-2 my-4">
        <div
          v-for="(collab, index) in collabs"
          :key="collabs.boardId"
          class="m-2"
        >
          <div
            class="btn border-0 w-[380px] h-72 laptop:w-[350px] laptop:h-72 rounded-xl shadow-lg p-4 flex flex-col justify-between items-start"
            style="
              background-image: url('https://res.cloudinary.com/dyhavbbzf/image/upload/v1733736380/l3eanxnnefwk07sonuhz.png');
              background-size: cover;
              background-position: center;
            "
          >
            <div>
              <h1 class="text-3xl font-bold text-gray-900 text-left">
                {{ collab.name }}
              </h1>
              <div class="text-sm font-medium text-gray-700 text-left">
                <div class="font-bold">Collab Email</div>
                <div>
                  {{ collab.email }}
                </div>
              </div>
              <div class="text-sm font-medium text-gray-700 text-left">
                <div class="font-bold">Acess Right</div>
                <div class="text">
                  {{ collab.accessRight }}
                </div>
              </div>
              <div class="text-sm font-medium text-gray-700 text-left">
                <div class="font-bold">Status</div>
                <div
                  class="text-lg"
                  :class="{
                    'text-yellow-500': collab.status === 'PENDING',
                    'text-green-500': collab.status === 'ACCEPTED',
                  }"
                >
                  {{ collab.status.toLowerCase() }}
                </div>
              </div>
            </div>
            <div class="flex space-x-2 w-full">
              <button
                class="btn bg-gradient-to-r from-red-700 to-red-400 border-0 text-white"
                @click="accessRights !== 'READ' && setDataCollab(true, collab)"
                :class="{
                  'opacity-50 cursor-not-allowed': accessRights === 'READ',
                }"
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <teleport to="#body">
    <AddCollab
      v-if="showModal"
      @close="setClose"
      @newCollab="addCollab"
      class="itbkk-modal-task"
    />
  </teleport>
  <teleport to="#body">
    <CollabDelete
      v-if="showDeleteModal"
      @close="setCloseDelete"
      :collab="deleteCollab"
      @savedDelete="deleteCollabFunc"
      class="itbkk-modal-task"
    />
  </teleport>
</template>

<style scoped></style>
