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
onMounted(async () => {
  await boardStore.fetchBoards(token.oid);
  const nameBoardf = boardStore.matchUserBoard(routeId.value);
  console.log(nameBoardf);
  const collab = collabStore.fetchCollabsByBoardId(routeId.value);
  nameBoard.value = nameBoardf.name;
  dataCollab.value = collab;
});
const deleteCollabFunc = async (collab) => {
  if (collab === deleteCollab.value) {
    const res = await collabStore.deleteCollab(collab.oid, routeId.value);    
    if (res.status === 200) {
      toasterStore.success({ text: "Collaberator deleted successfully!" });
    } else {
      toasterStore.error({
        text: "An error occurred while deleting the collaberator.",
      });
    }
  }
};
const setDataCollab = (value,collab) => {
  showDeleteModal.value = value;
  deleteCollab.value = collab;
};
const setClose = () => {
  showModal.value = false;
};
const setCloseDelete = () => {
  showDeleteModal.value = false;
};
const addCollab = async (newCollab) => {
  const data = await collabStore.addCollab(newCollab,routeId.value)
  if (data) {
    toasterStore.success({ text: "Collaberator added successfully!" });
  } else {
    toasterStore.error({
      text: "An error occurred while adding the collaberator.",
    });
  }
};

const setModal = (value) => {
  showModal.value = value;
};

</script>

<template>
  <div class="flex justify-end m-10">
    <BaseBtn>
      <router-link :to="{ name: 'Task' }">
        <template #default>
          <button class="p-4">Manage Task</button>
        </template>
      </router-link>
    </BaseBtn>
  </div>
  <div>
    <div class="w-full flex justify-center my-3">
      <div
        class="font-rubik font-medium text-4xl text-slate-500 ml-2 cursor-pointer hover:bg-gradient-to-r from-blue-600 via-green-500 to-indigo-400 hover:inline-block hover:text-transparent hover:bg-clip-text hover:duration-500"
      >
        Board name : {{ nameBoard }}
      </div>
    </div>
    <div class="w-full flex justify-center my-3">
      <div
        class="font-rubik font-medium text-4xl text-slate-500 ml-2 cursor-pointer hover:bg-gradient-to-r from-blue-600 via-green-500 to-indigo-400 hover:inline-block hover:text-transparent hover:bg-clip-text hover:duration-500"
      >
        Manage Collaborator
      </div>
    </div>
    <div
      class="w-full h-16 flex justify-between sm:flex-nowrap mobile:flex-wrap mobile:justify-end tablet:justify-between"
    >
      <div class="w-[95%] h-full m-auto flex justify-start items-center px-6">
        <div class="font-bold text-slate-700">Tool Bar :</div>
        <div
          class="itbkk-button-add btn btn-outline flex mx-8"
          @click="setModal(true)"
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
                fill="#334155"
              />
            </g>
            <defs>
              <clipPath id="clip0_529_11">
                <rect width="20" height="20" fill="white" />
              </clipPath>
            </defs>
          </svg>
          <div class="text-slate-700">Add Collaberator</div>
        </div>
      </div>
    </div>

    <div class="w-full flex justify-center">
      <div
        class="overflow-x-auto shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40 overflow-y-auto"
      >
        <div class="min-w-full divide-y divide-gray-200">
          <div class="#4793AF bg-slate-800 flex overflow-auto">
            <div
              class="w-[10%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            ></div>
            <div
              class="w-[20%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              No
            </div>
            <div
              class="w-[20%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Name
            </div>
            <div
              class="w-[20%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Email
            </div>
            <div
              class="w-[20%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Acess Right
            </div>
            <div
              class="w-[20%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Action
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
                    >
                      {{ index + 1 }}
                    </div>
                    <div
                      class="itbkk-title w-[30%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                    >
                      {{ collab.name }}
                    </div>
                    <div
                      class="itbkk-assignees w-[15%] px-1 py-4 whitespace-nowrap overflow-x-auto"
                    >
                      {{ collab.email }}
                    </div>
                    <div
                      class="itbkk-assignees w-[15%] px-6 py-4 whitespace-nowrap overflow-x-auto text-center"
                    >
                      {{ collab.accessRight }}
                    </div>

                    <div
                      class="itbkk-button-action w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                    >
                      <div
                        class="itbkk-button-delete btn btn-outline btn-error"
                        @click="setDataCollab(true,collab)"
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
  </div>
</template>

<style scoped></style>
