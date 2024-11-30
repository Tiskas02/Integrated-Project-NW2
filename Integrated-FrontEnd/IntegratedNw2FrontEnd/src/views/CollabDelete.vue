<script setup>
import { defineProps, defineEmits } from "vue"
const emit = defineEmits(["close", "savedDelete"])
const props = defineProps({
  collab: Object,
})
console.log(props.collab);
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-[100px] z-[2]"
    >
      <div class="w-[90%] tablet:w-[65%] m-[auto] max-h-[80%]">
        <div
          class="flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-lg shadow-xl"
        >
          <div class="text-xl font-semibold text-red-800">Remove Collaberator</div>
          <div class="border-b my-3"></div>
          <div class="break-all itbkk-message text-white">
            Do you want to remove" {{ collab.name }} " from the board? 
          </div>
          <div class="flex justify-end my-4">
            <div
              @click="
                () => {
                  [$emit('close', false ), $emit('savedDelete', collab)]
                }
              "
              class="itbkk-button-confirm btn bg-gradient-to-r from-blue-700 to-blue-400 hover:bg-gradient-to-r hover:from-blue-900 hover:to-blue-700 border-none text-white"
            >
              Confirm
            </div>
            <div
              @click="[$emit('close', false)]"
              class="itbkk-button-cancel btn bg-gradient-to-r from-red-700 to-red-400 hover:bg-gradient-to-r hover:from-red-900 hover:to-red-700 border-none text-white mx-2"
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
