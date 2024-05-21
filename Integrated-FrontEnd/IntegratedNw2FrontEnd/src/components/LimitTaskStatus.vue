<script setup>
import { defineProps, defineEmits } from "vue";
const emit = defineEmits(["limit", "close"]);
const props = defineProps({
  task: Object,
  index: Number,
});

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
          <div class="text-xl font-semibold text-red-400">Limit Tasks</div>
          <div class="border-b my-3"></div>
          <div class="form-control ml-auto">
            <label class="cursor-pointer label">
              <div class="flex justify-between">
                <span class="label-text mr-3 text-lg">Enable / Disabled</span>
                <input type="checkbox" class="toggle toggle-accent toggle-lg" checked />
              </div>
            </label>
          </div>
          <div class="flex justify-end my-4">
            <div
              @click="[$emit('close', false)]"
              class="itbkk-button-cancel btn btn-error text-white mx-2"
            >
              close
            </div>
        </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
