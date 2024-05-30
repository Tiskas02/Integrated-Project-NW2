<script setup>
import { ref, onMounted } from "vue";
import StatusModal from "./StatusModal.vue";
import { useStoreStatus } from "@/stores/statusStores";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { getStatusDataById } from "@/libs/api/status/fetchUtilStatus.js";
import { useToasterStore } from "@/stores/notificationStores";
const route = useRoute();
const router = useRouter();
const showModal = ref(false);
const storeMode = ref("");
const statusStore = useStoreStatus();
const { statuses } = storeToRefs(statusStore);
const dataById = ref();
const toasterStore = useToasterStore();
onMounted(async () => {
  await statusStore.fetchStatus();
});
const addOrEditStatus = async (newStatus) => {
  try {
    const name = newStatus.name ? newStatus.name.trim() : newStatus.name;
    const description = newStatus.description ? newStatus.description.trim() : newStatus.description;
    if (newStatus.id === undefined) {
      const check = await statusStore.createStatus({
        name,
        description,
      });
      if (check.id !== undefined) {
        toasterStore.success({ text: "Status added successfully!" });
      } else if (check.errors[0].message){
        toasterStore.error({
          text: `Status Name ${check.errors[0].message} `,
        });
      }else if (check.status === 400) {
        toasterStore.error({
          text: "An error occurred while adding the status.",
        });
      }
    } else {
      const check = await statusStore.updateStatus(newStatus.id, {
        name,
        description,
      });
      if (check.id !== undefined) {
        toasterStore.success({ text: "Status edit successfully!" });
      } else if (check.status === 400) {
        toasterStore.error({
          text: "An error occurred while editing the status.",
        });
      }
    }
  } catch (error) {
    console.error("Error adding/editing status:", error);
  }
};

const deleteOne = async (id) => {
  const status = await statusStore.deleteStatus(id);
  if (status === 200) {
    toasterStore.success({ text: "Status deleted successfully!" });
  } else {
    toasterStore.error({ text: "An error occurred while deleting the task." });
  }
};
const deleteTranfer = async (value) => {
  const status = await statusStore.tranferStatus(value.oldId, value.newId);
  if (status === 200) {
    toasterStore.success({ text: "Status tranfer successfully!" });
  } else {
    toasterStore.error({
      text: "An error occurred while tranfering the task.",
    });
  }
};

const setModal = async (value, mode, id) => {
  showModal.value = value;
  storeMode.value = mode;
  if (storeMode.value === "add") {
    router.push({ name: "addStatus" });
  } else if (storeMode.value === "edit" && id !== null) {
    router.push({ name: "editStatus", params: { id: id } });
  } else {
    dataById.value = statusStore.statuses.find((status) => status.id === id);
  }
};

const fetchById = async (id, mode) => {
  if (!id) {
    return alert("No id provided");
  }
  dataById.value = await getStatusDataById(id);
  storeMode.value = mode;
  if (storeMode.value === "edit" && id !== null) {
    router.push({ name: "editStatus", params: { id: id } });
  }
  if (dataById.value.status == "404") {
    alert("The requested status does not exist");
    router.replace({ name: "status" });
    showModal.value = false;
    return;
  }

  showModal.value = true;
};

if (route.params.id) {
  fetchById(route.params.id, "view");
}

const setClose = (value) => {
  showModal.value = value;
  router.push({ name: "status" });
};
</script>

<template>
  <div>
    <div class="flex justify-between">
      <div class=" tablet:mx-12 text-xl mobile:mx-4 font-semibold text-center tablet:pt-4 hover:animate-bounce">Manage Status</div>
      <div>
        <div
          class="itbkk-button-add btn btn-outline btn-primary mr-10 md:mr-20 lg:mr-10 flex"
          @click="setModal(true, 'add', null)"
        >
          <svg
            width="20"
            height="20"
            viewBox="0 0 20 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <g clip-path="url(#clip0_529_11)">
              <path
                d="M11 9H15V11H11V15H9V11H5V9H9V5H11V9ZM10 20C7.34784 20 4.8043 18.9464 2.92893 17.0711C1.05357 15.1957 0 12.6522 0 10C0 7.34784 1.05357 4.8043 2.92893 2.92893C4.8043 1.05357 7.34784 0 10 0C12.6522 0 15.1957 1.05357 17.0711 2.92893C18.9464 4.8043 20 7.34784 20 10C20 12.6522 18.9464 15.1957 17.0711 17.0711C15.1957 18.9464 12.6522 20 10 20ZM10 18C12.1217 18 14.1566 17.1571 15.6569 15.6569C17.1571 14.1566 18 12.1217 18 10C18 7.87827 17.1571 5.84344 15.6569 4.34315C14.1566 2.84285 12.1217 2 10 2C7.87827 2 5.84344 2.84285 4.34315 4.34315C2.84285 5.84344 2 7.87827 2 10C2 12.1217 2.84285 14.1566 4.34315 15.6569C5.84344 17.1571 7.87827 18 10 18Z"
                fill="#7C5ED2"
              />
            </g>
            <defs>
              <clipPath id="clip0_529_11">
                <rect width="20" height="20" fill="white" />
              </clipPath>
            </defs>
          </svg>
          Add Status
        </div>
      </div>
    </div>

    <div class="w-full flex justify-center">
      <div
        class="overflow-x-auto shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40 overflow-y-auto mt-4"
      >
        <div class="min-w-full divide-y divide-gray-200 ">
          <div class="#4793AF bg-slate-600 flex overflow-auto">
            <div
              class="w-[10%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            ></div>
            <div
              class="w-[20%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Name
            </div>
            <div
              class="w-[50%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Description
            </div>

            <div
              class="w-[20%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Action
            </div>
          </div>
          <div v-if="statuses.length <= 0" class="w-full border bg-white h-24">
            <div class="flex justify-center items-center h-full">
              <p class="text-xl font-bold animate-bounce text-slate-500">
                No Status
              </p>
            </div>
          </div>
          <div class="w-full mobile:h-[330px] tablet:h-[400px] sm:h-[300px] md:h-[380px] lg:h-[500px] xl:h-[550px] 2xl:h-[600px] overflow-auto rounded">
            <div v-for="(status, index) in statuses" :key="status.id">
              <div class="bg-white divide-y divide-gray-200 overflow-auto">
                <div class="w-full max-h-[550px]">
                  <div
                    class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50"
                  >
                    <div class="flex hover:shadow-inner hover:bg-slate-50">
                      <div class="w-[10%] px-6 py-4 whitespace-nowrap">
                        {{ index + 1 }}
                      </div>
                      <div
                        class="w-[20%] itbkk-title px-6 py-4 whitespace-nowrap overflow-x-auto"
                      >
                        {{ status.name }}
                      </div>
                      <div
                        class="w-[50%] itbkk-description px-6 py-4 whitespace-nowrap overflow-x-auto break-all italic"
                      >
                        {{
                          status?.description == "" ||
                          status?.description === null
                            ? "No Description Provided"
                            : status?.description
                        }}
                      </div>
                      <div
                        v-if="
                          status.name !== 'Done' && status.name !== 'No Status'
                        "
                        class="w-[20%] px-6 py-4 whitespace-nowrap flex gap-4"
                      >
                        <div
                          class="btn btn-outline btn-warning"
                          @click="fetchById(status.id, 'edit')"
                        >
                          Edit
                        </div>
                        <div
                          class="itbkk-button-delete btn btn-outline btn-error"
                          @click="setModal(true, 'delete', status.id)"
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
    </div>
    <teleport to="#body">
      <StatusModal
        v-if="showModal"
        @close="setClose"
        :statusMode="storeMode"
        :status="dataById"
        @newStatus="addOrEditStatus"
        @deleteId="deleteOne"
        @sentTranferId="deleteTranfer"
      />
    </teleport>
  </div>
</template>

<style scoped></style>
