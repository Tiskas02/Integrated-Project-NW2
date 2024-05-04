<script setup>
import { deleteItemById } from '../libs/fetchUtil.js';
import { defineProps, defineEmits, ref } from 'vue';
const emit = defineEmits(['setDelete','statusCode'])
const props = defineProps({
  tasks: Object
})
const statusCode = ref(0)
const removeTask = async (removeId) => {
  //backend
  const removeTask = await deleteItemById(
    import.meta.env.VITE_BASE_URL,
    removeId
  );
  console.log(removeTask);
  if (removeTask === 200) {
    statusCode.value = 200
  } else {
    statusCode.value = 400
  }
  console.log(statusCode.value);
}
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-[100px]"
    >
      <div class="w-[40%] m-[auto] max-h-[80%]">
        <div
          class="flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
        >
          <div class="text-xl font-semibold text-red-400">Delete Task</div>
          <div class="border-b my-3"></div>
          <div class="break-all">
            Do you want to delete the task " {{ tasks?.title }} " ?
          </div>
          <div class="flex justify-end my-4">
            <div
              @click="
                [$emit('setDelete', false), $router.replace({ name: 'task' })]
              "
              class="itbkk-button btn btn-error text-white mx-2"
            >
              Cancle
            </div>
            <div
              @click="
                [removeTask(tasks?.id),$emit('setDelete', false), $router.replace({ name: 'task' }),$emit('statusCode', statusCode)]
              "
              class="itbkk-button btn btn-success text-white"
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
