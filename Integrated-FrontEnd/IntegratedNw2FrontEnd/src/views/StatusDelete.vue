<script setup>
import { shouldDeleteOrTransferStatus } from "@/libs/api/status/fetchUtilStatus";
import { defineProps, defineEmits, ref, watch, onMounted } from "vue";
import { useStoreStatus } from "@/stores/statusStores";
import { useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import BaseBtn from "@/shared/BaseBtn.vue";
const route = useRoute();
const routerId = ref(route.params.id);
const statusStore = useStoreStatus();
const { statuses } = storeToRefs(statusStore);
const emit = defineEmits(["close", "sentDelete", "sentTranfer"]);
const props = defineProps({
  status: {
    type: Object,
    default: {
      id: undefined,
      name: "",
      description: "",
    },
  },
});
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
const oldId = ref(props.status.id);
const newId = ref(null);
const shouldDeleteOrTransfer = ref(false);

onMounted(async () => {
  const deleted = await shouldDeleteOrTransferStatus(
    props.status.id,
    routerId.value
  );
  if (deleted === true) {
    shouldDeleteOrTransfer.value = true;
  } else {
    shouldDeleteOrTransfer.value = false;
  }
});
const selectedStatus = ref(props.status.name);
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};
const updateStatus = (statusName) => {
  selectedStatus.value = statusName;
  statuses.value.forEach((status) => {
    if (status.name === statusName) {
      newId.value = status.id;
    }
  });
  isDropdownOpen.value = false; // Close the dropdown after selection
};
console.log("status", props.status);
console.log("statuses", statuses);
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-16 tablet:pt-20"
    >
      <div class="tablet:w-[40%] m-[auto] max-h-[80%]">
        <div
          class="flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-b-xl tablet:rounded-xl shadow-xl"
        >
          <div class="text-xl font-semibold text-red-800">
            {{ shouldDeleteOrTransfer ? "Transfer Status" : "Delete Status" }}
          </div>
          <div class="border-b my-3"></div>
          <div class="break-all itbkk-message text-white">
            Do you want to delete the "{{ status.name }}" status
          </div>
          <div v-if="shouldDeleteOrTransfer">
            <div class="flex justify-start items-center">
              <div class="itbkk-message mr-3 text-white my-6">Transfer to</div>
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
                    {{ selectedStatus }}
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
                  class="absolute mt-2 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
                  role="menu"
                  aria-orientation="vertical"
                  aria-labelledby="menu-button"
                  tabindex="-1"
                >
                  <div class="py-1" role="none">
                    <a
                      v-for="status in statuses"
                      :key="status.id"
                      href="#"
                      @click.prevent="updateStatus(status.name)"
                      class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                      role="menuitem"
                      tabindex="-1"
                      :id="`menu-item-${status.id}`"
                    >
                      {{ status.name }}
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="flex justify-end my-4">
            <div v-if="shouldDeleteOrTransfer">
              <BaseBtn
                :disabled="selectedStatus === status.name"
                class="itbkk-button-confirm itbkk-button-ok mx-4 mt-3"
              >
                <template #default>
                  <button
                    @click="
                      () => {
                        $emit('close', false),
                          $emit('sentTranfer', { oldId, newId });
                      }
                    "
                    :disabled="selectedStatus === status.name"
                    :class="{
                      'cursor-not-allowed': selectedStatus === status.name,
                    }"
                  >
                    Transfer
                  </button>
                </template>
              </BaseBtn>
            </div>
            <div v-else>
              <BaseBtn class="itbkk-button-confirm itbkk-button-ok mx-4 mt-3">
                <template #default>
                  <button
                    @click="
                      () => {
                        $emit('close', false), $emit('sentDelete', oldId);
                      }
                    "
                  >
                    Confirm
                  </button>
                </template>
              </BaseBtn>
            </div>

            <BaseBtn class="itbkk-button-cancel">
              <template #cancel>
                <button
                  @click="
                    () => {
                      emit('close', false);
                    }
                  "
                >
                  Cancel
                </button>
              </template>
            </BaseBtn>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
