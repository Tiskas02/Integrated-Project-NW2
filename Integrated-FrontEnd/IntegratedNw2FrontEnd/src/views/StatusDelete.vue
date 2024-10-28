<script setup>
import { shouldDeleteOrTransferStatus } from "@/libs/api/status/fetchUtilStatus"
import { defineProps, defineEmits, ref, watch, onMounted } from "vue"
import { useStoreStatus } from "@/stores/statusStores"
import { useRoute } from "vue-router";
import { storeToRefs } from "pinia"
const route = useRoute();
const routerId = ref(route.params.id);
const statusStore = useStoreStatus()
const { statuses } = storeToRefs(statusStore)
const emit = defineEmits(["close", "sentDelete", "sentTranfer"])
const props = defineProps({
  status: {
    type: Object,
    default: {
      id: undefined,
      name: "",
      description: "",
    },
  },
})
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
const oldId = ref(props.status.id)
const newId = ref(null)
const shouldDeleteOrTransfer = ref(false)

onMounted(async () => {
  const deleted = await shouldDeleteOrTransferStatus(props.status.id,routerId.value)
  if (deleted === true) {
    shouldDeleteOrTransfer.value = true
  } else {
    shouldDeleteOrTransfer.value = false
  }
})
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-[100px]"
    >
      <div class="w-[40%] m-[auto] max-h-[80%]">
        <div
          class="flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
        >
          <div class="text-xl font-semibold text-red-400">
            {{ shouldDeleteOrTransfer ? "Transfer Status" : "Delete Status" }}
          </div>
          <div class="border-b my-3"></div>
          <div class="break-all itbkk-message">
            'Do you want to delete the {{ status.name }} status'
          </div>
          <div v-if="shouldDeleteOrTransfer">
            <div class="flex justify-start items-center">
              <div class="itbkk-message mr-3">Transfer to</div>
              <select
                class="itbkk-status select select-bordered bg-slate-100 shadow-inner text-black border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                v-model="newId"
              >
                <option
                  v-for="status in statuses"
                  :key="status.id"
                  :value="status.id"
                >
                  {{ status.name }}
                </option>
              </select>
            </div>
          </div>
          <div class="flex justify-end my-4">
            <div
              @click=";[$emit('close', false)]"
              class="itbkk-button-cancel btn btn-error text-white mx-2"
            >
              Cancel
            </div>
            <div
              v-if="shouldDeleteOrTransfer"
              @click="
                () => {
                  $emit('close', false), $emit('sentTranfer', { oldId, newId })
                }
              "
              class="itbkk-button-confirm btn btn-success text-white"
            >
              Transfer
            </div>
            <div
              v-else
              @click="
                () => {
                  $emit('close', false), $emit('sentDelete', oldId)
                }
              "
              class="itbkk-button-confirm btn btn-success text-white"
            >
              Confirm
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
