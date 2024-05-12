<script setup>
import { defineProps, defineEmits ,ref,watch} from 'vue';
const emit = defineEmits(['close']);
const props = defineProps({
    mode: String
});
const storeModeSent = ref(props.mode);
watch(
  () => props.mode,
  () => {
    storeModeSent.value = props.mode;
  },
  {deep: true}
);
console.log(storeModeSent.value);
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
            <div class="text-lg z-0">Title</div>
            <div>
              <textarea
                class="itbkk-title w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                placeholder="Enter your title here..."
                required
                ></textarea
              >
            </div>
          </div>
          <div class="flex my-1">
            <div class="max-w-fit my-auto mx-6">Status</div>
            
            <div >
              <label class="form-control w-full">
                <!-- <select
                  class="itbkk-status select select-bordered bg-slate-100 shadow-inner text-black border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  v-model="newTask.status"
                >
                  <option disabled selected>Status</option>
                  <option value="TO_DO" :selected="tasks?.status === 'TO_DO'">
                    To Do
                  </option>
                  <option value="DOING" :selected="tasks?.status === 'DOING'">
                    Doing
                  </option>
                  <option value="DONE" :selected="tasks?.status === 'DONE'">
                    Done
                  </option>
                  <option
                    value="NO_STATUS"
                    :selected="tasks?.status === 'NO_STATUS'"
                  >
                    No Status
                  </option>
                </select> -->
              </label>
            </div>
          </div>
          <div class="flex">
            <div class="my-auto mx-2">Assignees</div>
            
            <div>
              <div class="w-full">
                <textarea
                  class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 italic"
                  placeholder="Enter your assign here..."
                  
                >
                  
                  </textarea
                >
              </div>
            </div>
          </div>
          <div v-if="mode === 'edit'">
            <div class="flex itbkk-timezone my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">TimeZone</div>
              <div>{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</div>
            </div>
            <div class="flex itbkk-created-on my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">Created On</div>
              <div>
                
              </div>
            </div>
            <div class="flex itbkk-updated-on my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">Updated On</div>
              <div>
                
              </div>
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
                  
                  ></textarea
                >
              </div>
            </div>
          </div>

          <div class="flex flex-row w-full justify-end">
            <div class="mr-2">
                <button
                  class="itbkk-button-confirm disabled btn btn-info text-white"
                  @click="() =>{
                    emit('close',false);
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