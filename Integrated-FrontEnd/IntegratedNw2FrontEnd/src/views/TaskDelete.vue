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
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-[100px] z-[2]"
    >
      <div class="w-[40%] m-[auto] max-h-[80%]">
        <div
          class="flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
        >
          <div class="text-xl font-semibold text-red-400">Delete Task</div>
          <div class="border-b my-3"></div>
          <div class="break-all itbkk-message">
            Do you want to delete the task number {{ index + 1 }}"
            {{ task?.title }} " ?
          </div>
          <div class="flex justify-end my-4">
            <div
              @click=";[$emit('close', false)]"
              class="itbkk-button-cancel btn btn-error text-white mx-2"
            >
              Cancel
            </div>
            <div
              @click="
                () => {
                  $emit('close', false), $emit('savedDelete', task?.id)
                }
              "
              class="itbkk-button-confirm btn btn-success text-white"
            >
              Confirm
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
