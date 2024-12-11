<script setup>
import { defineProps, defineEmits } from "vue"
const emit = defineEmits(["close", "savedDelete"])
const props = defineProps({
  task: Object,
  index: Number,
})
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-16 tablet:pt-20"
    >
      <div class="tablet:w-[40%] m-[auto] max-h-[80%]">
        <div
          class="flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-b-lg tablet:rounded-lg shadow-xl"
        >
          <div class="text-xl font-semibold text-red-800">Delete Task</div>
          <div class="border-b my-3"></div>
          <div class="break-all itbkk-message text-white">
            Do you want to delete the task number {{ index + 1 }} "{{ task?.title }}" ?
          </div>
          <div class="flex justify-end my-4">
            <div
              @click="
                () => {
                  $emit('close', false), $emit('savedDelete', task?.id)
                }
              "
              class="itbkk-button-confirm btn bg-gradient-to-r from-blue-700 to-blue-300 border-none text-white"
            >
              Confirm
            </div>
            <div
              @click=";[$emit('close', false)]"
              class="itbkk-button-cancel btn bg-gradient-to-r from-red-700 to-red-300 border-none text-white mx-2"
            >
              Cancel
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
