<script setup>
import { shouldDeleteOrTransferStatus } from "@/libs/api/status/fetchUtilStatus";
import { defineProps, defineEmits, ref, watch, computed, onMounted } from "vue";
import { useStoreStatus } from "@/stores/statusStores";
import { storeToRefs } from "pinia";
const emit = defineEmits(["close","sentDelete","sentTranfer"]);

const props = defineProps({
  status: {
    type: Object,
    default: {
      statusId: undefined,
      name: "",
      description: "",
    },
  },
});
const statusStore = useStoreStatus();
const { statuses } = storeToRefs(statusStore);
const oldId = ref(props.status.statusId);
const newId = ref(null);
watch(
  newId,
  () => {
    console.log(newId.value);
  },
  { deep: true }
);

// true = should transfer, false = should delete
const shouldDeleteOrTransfer = ref(false);

onMounted(async () => {
  shouldDeleteOrTransfer.value = await shouldDeleteOrTransferStatus(
    props.status.statusId
  );
});

import useToasterStore from '../stores/notificationStores';
const toasterStore = useToasterStore();

const deleteStatusNoti = () => {
  try {
    // Simulate deletion logic here
    if (props.status) {
      // Assuming task deletion is successful
      toasterStore.success({ text: "Status deleted successfully!" });
    } else {
      // Assuming task deletion failed
      throw new Error("Failed to delete task.");
    }
  } catch (error) {
    console.error('Error deleting task:', error);
    toasterStore.error({ text: "An error occurred while deleting the task." });
  }
};

const transferStatusNoti = () => {
  try {
    // Simulate transfer logic here
    if (props.status) {
      // Assuming status transfer is successful
      toasterStore.success({ text: "Status transferred successfully!" });
    } else {
      // Assuming status transfer failed
      throw new Error("Failed to transfer status.");
    }
  } catch (error) {
    console.error('Error transferring status:', error);
    toasterStore.error({ text: "An error occurred while transferring the status." });
  }
};
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
            Do you want to delete the Status name "{{ status.name }}" ?
          </div>
          <div v-if="shouldDeleteOrTransfer">
            <div class="flex-row">
            <div class="break-all itbkk-message">
              Transfer to 
            </div>
            <select
                    class="itbkk-status select select-bordered bg-slate-100 shadow-inner text-black border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                    v-model="newId"
                  >
                    <option
                      v-for="status in statuses"
                      :key="status.statusId"
                      :value="status.statusId"
                    >
                      {{ status.name }}
                    </option>
                  </select>
          </div>
          </div>
          <div class="flex justify-end my-4">
            <div
              @click="[$emit('close', false)]"
              class="itbkk-button-cancel btn btn-error text-white mx-2"
            >
              Cancel
            </div>
            <div v-if="shouldDeleteOrTransfer"
              @click="() => { 
                transferStatusNoti(),
                $emit('close', false),
                $emit('sentTranfer',{oldId, newId})
                }"
              class="itbkk-button-confirm btn btn-success text-white"
            >
              Transfer
            </div>
            <div v-else
              @click="() => {
              deleteStatusNoti(),  
              $emit('close', false),
              $emit('sentDelete', oldId)
              }"
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
