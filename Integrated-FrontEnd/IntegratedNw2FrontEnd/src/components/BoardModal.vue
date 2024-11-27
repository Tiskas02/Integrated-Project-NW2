<script setup>
import BaseBtn from "@/shared/BaseBtn.vue"
import { defineEmits, ref } from "vue"
const emit = defineEmits(["close", "newBoard"])
const newBoard = ref({
  name: "",
})
</script>

<template>
  <div>
    <div
      class="itbkk-modal-new bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-16 tablet:pt-20"
      style="translate: transform(-50%, -50%)"
    >
      <div class="w-full tablet:w-[60%] m-[auto] max-h-screen laptop:w-[40%]">
        <div
          class="overflow-auto max-h-screen flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-b-2xl tablet:rounded-xl  shadow-xl"
          >
          <div class="itbkk-modal-new">
            <div class="text-xl font-bold my-3 text-white">Create personal board</div>
            <div class="border-b my-2"></div>
            <div class="text-lg z-0 text-white">Name</div>
            <div>
              <textarea
                class="itbkk-title itbkk-board-name w-full h-[90%] px-4 py-2 my-1 bg-[#b3d1e8] bg-opacity-40 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 placeholder:text-slate-400 "
                placeholder="Enter board name"
                required
                maxlength="120"
                v-model="newBoard.name"
              ></textarea>
            </div>
          </div>
          <div class="flex flex-row w-full justify-end my-4">
            <div class="flex ">
              <BaseBtn class="itbkk-button-confirm itbkk-button-ok mx-4 mt-3">
                <template #default>
                  <button
                    @click="
                      () => {
                        emit('close', false)
                        emit('newBoard', newBoard)
                      }
                    "
                    
                  >
                    Save
                  </button>
                </template>
              </BaseBtn>
              <BaseBtn class="itbkk-button-cancel">
                <template #cancel>
                  <button
                    @click="
                      () => {
                        emit('close', false)
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
</template>

<style scoped></style>
