<script setup>
import BaseBtn from "@/shared/BaseBtn.vue";
</script>

<template>
  <div>
    <div>
      <div>
        <div class="w-[60%] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
          >
            <div>
              <div class="text-xl font-bold my-3"><slot name="Title">Add Task</slot></div>
              <div class="border-b my-2"></div>
              <div class="text-lg z-0">Title</div>
              <div><textarea
                  class="itbkk-title w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  placeholder="Enter your title here..."
                  required
                  maxlength="50">
                </textarea></div>
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
                <div></div>
              </div>
            </div>
             <div class="flex flex-row w-full justify-end my-4">
              <div class="mr-2">
                <BaseBtn>
                  <template #default> 
                    <slot name="default"> puake </slot>
                  </template>
                </BaseBtn>
              </div>
              <div>
                <BaseBtn>
                  <template #cancel>
                    <slot name="cancel">Delete</slot> </template>
                </BaseBtn>
              </div>
            </div> 
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
