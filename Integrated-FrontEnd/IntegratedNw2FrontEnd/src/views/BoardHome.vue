<script setup>
import { ref, onMounted } from "vue";
import { useStoreBoard } from "@/stores/boardStore";
import LoadingScreen from "@/shared/LoadingScreen.vue";
import Logo from "@/shared/Logo.vue";
import BaseBtn from "@/shared/BaseBtn.vue";
const dataLoaded = ref(true);
const boardStore = useStoreBoard();
const storeBoards = ref([]);

// onMounted(async () => {
//   const data = await boardStore.fetchBoards();
//   storeBoards.value = data;
//   if (storeBoards.value.length > 0) {
//     dataLoaded.value = true;
//   } else {
//     dataLoaded.value = false;
//   }
// });
const userPayload = localStorage.getItem("userPayload")
  ? JSON.parse(localStorage.getItem("userPayload"))
  : null;
</script>

<template>
  <div>
    <LoadingScreen v-if="!dataLoaded" />
    <Logo :userPayload="userPayload">
      <template #image>
        <img src="/icon.png" alt="icon" class="w-[4%] m-2" />
      </template>
      <template #text>
        <div class="font-chivo font-medium text-xl text-white mx-1">
          IT-Bangmod Kradan Kanban
        </div>
      </template>
    </Logo>
    <div class="w-full font-rubik font-medium text-4xl text-white text-center my-6">
      Board List
    </div>
    <div class="flex justify-end mx-10">
    <BaseBtn>
          <template #default >
            <button class="p-4">Create personal board</button>
          </template>
        </BaseBtn>
      </div>
    <div class="w-full flex justify-center mt-6">
      <div class="shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/30">
        <div class="min-w-full divide-y divide-gray-200 overflow-auto">
          <div class="#4793AF bg-slate-800 flex rounded-md overflow-auto">
            <div
              class="w-[10%] m-auto text-start text-md font-bold text-white uppercase overflow-auto"
            ></div>
            <div
              class="w-[22%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">No</div>
            </div>

            <div
              class="w-[22%] h-14 text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Name</div>
            </div>
            <div
              class="w-[22%] h-14 text-center text-md font-bold text-white uppercase flex justify-center items-center"
            >
            </div>
            <div
              class="w-[22%] h-14 text-left text-md font-bold text-white uppercase flex justify-center items-center"
            >
              <div class="w-full">Action</div>
            </div>
          </div>
          <div v-if="storeBoards.length <= 0" class="w-full border bg-white h-[60lvh] rounded-b-box">
            <div class="flex justify-center items-center h-full">
              <p class="text-xl font-bold animate-bounce text-slate-500">
                Board is empty
              </p>
            </div>
          </div>
          <div v-else class="w-full h-[500px] overflow-auto rounded-b-box">
            <div>
              <div class="bg-white divide-y divide-gray-200 overflow-auto shadow-inner">
                <div
                  class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 bg-slate"
                >
                  <div class="flex hover:shadow-inner hover:bg-slate-50">
                    <div
                      class="w-[10%] px-6 py-4 whitespace-nowrap"
                      @click=""
                    >
                    </div>
                    <div
                      class="itbkk-title w-[45%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click=""
                    >
                      {{ task.title }}
                    </div>
                    <div
                      class="itbkk-assignees w-[45%] px-6 py-4 whitespace-nowrap overflow-x-auto"
                      @click=""
                    >
                    </div>
                    <div
                      class="w-[22%] px-6 py-4 whitespace-nowrap flex justify-center overflow-x-auto"
                      @click=""
                    >
                      <div
                        class="itbkk-status btn btn-outline shadow overflow-x-auto"
                      >
                      </div>
                    </div>
                    <div
                      class="itbkk-button-action w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                    >
                      <div
                        class="itbkk-button-edit btn btn-outline btn-warning"
                        @click=""
                      >
                        Edit
                      </div>
                      <div
                        class="itbkk-button-delete btn btn-outline btn-error"
                        @click="
                        "
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
</template>

<style scoped></style>
