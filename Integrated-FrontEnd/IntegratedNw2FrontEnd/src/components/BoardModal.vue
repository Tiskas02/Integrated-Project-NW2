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
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-20"
      style="translate: transform(-50%, -50%)"
    >
      <div class="w-[60%] m-[auto] max-h-screen">
        <div
          class="overflow-auto max-h-screen flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
        >
          <div class="itbkk-modal-new">
            <div class="text-xl font-bold my-3">Create personal board</div>
            <div class="border-b my-2"></div>
            <div class="text-lg z-0">Name</div>
            <div>
              <textarea
                class="itbkk-board-name w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                placeholder="Enter board name"
                required
                maxlength="120"
                v-model="newBoard.name"
              ></textarea>
            </div>
          </div>
          <div class="flex flex-row w-full justify-end my-4">
            <div class="flex mr-2">
              <BaseBtn class="itbkk-button-ok mx-4 mt-3">
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
              <BaseBtn class="itbkk-button-cancel mx-4">
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

<style scoped>
</style>
