<script setup>
import BaseBtn from "@/shared/BaseBtn.vue";
import { defineEmits, ref } from "vue";
const emit = defineEmits(["close", "newCollab"]);
const newCollab = ref({
  email: "",
  accessRight: "READ",
});

const accessRights = ["Read", "Write"];
const selectAccessRight = (option) => {
  newCollab.value.accessRight = option;
};
</script>

<template>
  <div>
    <div>
    <div>
      <div
        class="itbkk-modal-new bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-16 tablet:pt-20"
        style="translate: transform(-50%, -50%)"
      >
        <div class=" tablet:w-[60%] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-b-xl tablet:rounded-lg shadow-xl"
          >
            <div class="itbkk-modal-new">
              <div class="text-xl font-bold my-3 text-white">Add Collaborator</div>
              <div class="border-b my-2"></div>
              <div class="text-lg z-0 text-white">Collaborator email</div>
              <div>
                <textarea
                  class="itbkk-title itbkk-board-name w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  placeholder="Enter board name"
                  required
                  maxlength="120"
                  v-model="newCollab.email"
                ></textarea>
              </div>
            </div>
            <div>
              <label
                for="accessright"
                class="block text-sm font-medium text-white"
                >Select Access Right</label
              >
              <div class="dropdown">
                <label tabindex="0" class="btn m-1">
                  {{ newCollab.accessRight || 'Select Access Right' }}
                </label>
                <ul
                  tabindex="0"
                  class="dropdown-content menu p-2 shadow bg-base-100 rounded-box w-52"
                >
                  <li v-for="option in accessRights" :key="option">
                    <a href="#" @click="selectAccessRight(option)">{{
                      option
                    }}</a>
                  </li>
                </ul>
              </div>
            </div>
            <div class="flex flex-row w-full justify-end my-4">
              <div class="flex mr-2">
                <BaseBtn class="itbkk-button-confirm itbkk-button-ok mx-4 mt-3">
                  <template #default>
                    <button
                      @click="
                        () => {
                          emit('close', false);
                          emit('newCollab', newCollab);
                        }
                      "
                    >
                      Save
                    </button>
                  </template>
                </BaseBtn>
                <BaseBtn class="itbkk-button-cancel mx-4">
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
    </div>
  </div>
  </div>
</template>

<style scoped></style>
