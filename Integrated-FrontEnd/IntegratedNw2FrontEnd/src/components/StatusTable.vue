<script setup>
import { ref } from 'vue';
import StatusAddEdit from './StatusAddEdit.vue';
const showModal = ref(false);
const openModal = () => {
  showModal.value = true;
};
</script>

<template>
  <div>
    <div class="flex justify-between">
      <div class="">
        <div class="ml-10 btn btn-outline btn-accent">
            <router-link :to="{ name: 'task' }">Home</router-link>    
        </div>
      </div>
      <div class="">
        <!-- Add Task-->
        <div class="itbkk-button-add btn btn-outline btn-primary mr-10" @click="openModal()">
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
        <!-- Added ml-2 class for margin-left -->
      </div>
    </div>

    <div class="w-full flex justify-center">
      <div
        class="overflow-x-auto shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40 overflow-y-auto  mt-4"
      >
        <div class="min-w-full divide-y divide-gray-200">
          <div class="#4793AF bg-slate-600 flex">
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
          <!-- <div class="w-full border bg-white ">
            <div class="w-full h-32">
              <p class="h-full flex justify-center items-center font-sans font-semibold">No status Provided</p>
            </div>
          </div> -->
          <!-- Edit Task -->
          <div>
            <div class="bg-white divide-y divide-gray-200 overflow-auto">
              <div class="w-full max-h-[550px]">
                <div
                  class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50"
                >
                  <div class="flex">
                    <div class="w-[10%] px-6 py-4 whitespace-nowrap">1</div>
                    <div
                      class="w-[20%] itbkk-title px-6 py-4 whitespace-nowrap overflow-x-auto"
                    >
                      No Status
                    </div>
                    <div
                      class="w-[50%] itbkk-assignees px-6 py-4 whitespace-nowrap overflow-x-auto"
                    >
                    The default status
                    </div>
                    <div class="w-[20%] px-6 py-4 whitespace-nowrap flex gap-4">
                      <div
                        class="btn btn-outline btn-warning"
                      >
                        Edit
                      </div>
                      <div
                        class="itbkk-button-delete btn btn-outline btn-error"
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
        <StatusAddEdit v-if="showModal"/>
      </teleport>
  </div>
</template>

<style scoped></style>
