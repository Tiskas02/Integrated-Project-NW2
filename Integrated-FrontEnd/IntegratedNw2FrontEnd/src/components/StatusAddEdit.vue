<script setup>
import { defineProps, defineEmits ,ref,watch} from 'vue';
import { useStoreStatus } from '../stores/statusStores.js';
import { storeToRefs } from 'pinia';

const emit = defineEmits(['close','newStatus']);
const props = defineProps({
  status: {
    type: Object,
    default: {
      statusId: undefined,
      name: '',
      description: ''
    }
  },
  mode: String
    
});
console.log(props.mode);
const statusStore = useStoreStatus()
const { statuses } = storeToRefs(statusStore);
const storeModeSent = ref(props.mode);

//store data
const storeData = ref({
    statusId: undefined,
    name: '',
    description: ''
});
watch(
  () => props.status,
  () => {
    if (props.mode === 'edit') {
      storeData.value = { ...props.status };
      console.log(storeData.value);
    }
  },
  { deep: true, immediate: true }
);

import useToasterStore from '../stores/notificationStores';
const toasterStore = useToasterStore();

const saveStatusNoti = () => {
  try {
    if (props.mode === 'add') {
      // Add task logic here
      toasterStore.success({ text: "Status added successfully!" });
    } else if (props.mode === 'edit' && storeData.value.id === props.status.id) {
      // Edit task logic here
      toasterStore.success({ text: "Status updated successfully!" });
    }
  } catch (error) {
    console.error('Error saving task:', error);
    toasterStore.error({ text: "An error occurred while saving the task." });
  }
};


</script>
 
<template>
<div>
    <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50"
      style="translate: transform(-50%, -50%)"
    >
      <div class="w-[60%] m-[auto] max-h-screen">
        <div
          class="overflow-auto max-h-screen flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
        >
          <div>
            <div class="text-xl font-bold my-3">
              {{mode === 'add' ? 'Add Status' : 'Edit Status'}}
            </div>
            <div class="border-b my-2"></div>
            <div class="text-lg z-0">Name</div>
            <div>
              <textarea
                class="itbkk-title w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                placeholder="Enter your title here..."
                required
                v-model="storeData.name"
                >{{ status?.name }}</textarea
              >
            </div>
          </div>
          <div class="mt-5">
            <div role="tablist" class="tabs tabs-bordered mb-3">
              <a role="tab" class="tab tab-active text-lg font-bold"
                >Description</a
              >
              <a role="tab" class="tab"></a>
              <a role="tab" class="tab"></a>
            </div>
            <div>
              <div>
                <textarea
                  class="itbkk-description w-full h-[90%] px-4 py-2 my-1 bg-slate-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner"
                  placeholder="Enter your description here..."
                  v-model="storeData.description"
                  >{{ status?.description }}</textarea
                >
              </div>
            </div>
          </div>

          <div class="flex flex-row w-full justify-end">
            <div class="mr-2">
                <button
                  class="itbkk-button-confirm disabled btn btn-info text-white"
                  @click="() =>{
                    saveStatusNoti(),
                    emit('close',false);
                    emit('newStatus',storeData);
                }"
                >
                  Save
                </button>
              
            </div>
            <div>
              <div
                class="itbkk-button-cancel btn btn-error text-white"
                @click="() =>{
                    emit('close',false);
                }"
              >
                Cancel
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>
 
<style scoped>

</style>