<script setup>
import { defineProps, defineEmits } from 'vue';
const emit = defineEmits(['close']);
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
          <div >
            <div
              class="itbkk-title text-xl font-semibold text-slate-800 my-4 break-all"
            >
              <!-- {{ tasks?.title }} -->
            </div>
          </div>
          <div class="flex my-1">
            <div class="max-w-fit my-auto mx-6">Status</div>
            <div >
              <div>
                <!-- {{ tasks?.status }} -->
              </div>
            </div>        
          </div>
          <div class="flex">
            <div class="my-auto mx-2">Assignees</div>
            <div >
              <div>
                <!-- {{ tasks?.assignees }} -->
              </div>
            </div>
            
          </div>
          <div>
            <div class="flex itbkk-timezone my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">TimeZone</div>
              <div>{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</div>
            </div>
            <div class="flex itbkk-created-on my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">Created On</div>
              <div>
                <!-- {{ new Date(tasks?.createdOn).toLocaleString('en-GB') }} -->
              </div>
            </div>
            <div class="flex itbkk-updated-on my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">Updated On</div>
              <div>
                <!-- {{ new Date(tasks?.updatedOn).toLocaleString('en-GB') }} -->
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
                <div
                  
                  class="itbkk-description break-all italic"
                  placeholder="No Description Provided"
                >
                  <!-- {{
                    tasks?.description == '' || tasks?.description === null
                      ? 'No Description Provided'
                      : tasks?.description
                  }} -->
                </div>
              </div>
            </div>
          </div>

          <div class="flex flex-row w-full justify-end">
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