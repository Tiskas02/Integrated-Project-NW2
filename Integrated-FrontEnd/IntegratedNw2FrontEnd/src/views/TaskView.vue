<script setup>
import { defineProps, defineEmits } from "vue";
const emit = defineEmits(["close", "task"]);
const props = defineProps({
  task: Object,
});

const getRandomColor = () => {
  return "#" + Math.floor(Math.random() * 16777215).toString(16);
};
console.log(props.task);
</script>

<template>
  <div>
    <div>
      <div
        class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-36 z-[2]"
        style="translate: transform(-50%, -50%)"
      >
        <div class="w-[90%] tablet:w-[60%] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-[#ffffff] border-gray-200 rounded-2xl shadow-xl"
          >
            <div
              class="itbkk-title text-xl font-semibold text-slate-800 h-36 break-all"
              style="
                background-image: url('/images/impressionism.png');
                background-size: cover;
                background-position: center;
              "
            >
              <div class="ml-4 mt-2 text-lg font-semiblod font-rubik">
                Task Detial
              </div>
              <div class="w-full h-[70%] flex justify-center items-center">
                {{ task.title }}
              </div>
            </div>
            <div class="p-7">
              <div class="border p-3 rounded-xl">
                <div class="flex flex-row">
                  <div class="flex flex-col w-[50%] my-1 mr-2">
                    <div class="2xl:w-[13%] sm:w-[17%] my-auto mr-4">
                      Status
                    </div>
                    <div>
                      <div class="itbkk-status text-lg overflow-auto">
                        {{ task?.status?.name }}
                      </div>
                    </div>
                  </div>
                  <div class="border"></div>
                  <div class="flex flex-col w-[50%] my-1 pl-3">
                    <div class="2xl:w-[13%] sm:w-[17%] my-auto mr-4">
                      Assignees
                    </div>
                    <div>
                      <div
                        :style="{
                          fontStyle: task.assignees ? 'normal' : 'italic',
                        }"
                        class="overflow-auto"
                      >
                        {{
                          task?.assignees == "" || task?.assignees === null
                            ? "Unassigned"
                            : task?.assignees
                        }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="h-32 mt-3 mx-2">
                <div class="text-lg font-blod font-rubik my-1">Description</div>
                <div class="border w-full my-1"></div>
                <div
                  class="itbkk-description break-all italic"
                  :class="{
                    'text-red-700':
                      task?.description === '' || task?.description === null,
                  }"
                >
                  {{
                    task?.description == "" || task?.description === null
                      ? "No Description Provided"
                      : task?.description
                  }}
                </div>
              </div>
              <div>
                <div class="flex itbkk-timezone mx-2">
                  <div class="mr-2 text-sm font-chivo">TimeZone</div>
                  <div class="text-sm font-chivo">
                    🌏 {{ Intl.DateTimeFormat().resolvedOptions().timeZone }}
                  </div>
                </div>
                <div class="flex flex-row">
                  <div class="border w-[75%] rounded-md px-2 py-1">
                    <div class="flex itbkk-created-on my-1">
                      <div class="overflow-auto mr-1">Created On :</div>
                      <div>
                        {{ new Date(task?.createdOn).toLocaleString("en-GB") }}
                      </div>
                    </div>
                    <div class="flex itbkk-updated-on my-1">
                      <div class="overflow-auto mr-1">Updated On :</div>
                      <div>
                        {{ new Date(task?.updatedOn).toLocaleString("en-GB") }}
                      </div>
                    </div>
                  </div>
                  <div class="w-[25%] flex justify-end items-end">
                    <div class="">
                      <div>
                        <div
                          class="itbkk-button-cancel btn bg-gradient-to-r from-red-600 to-red-400 text-white"
                          @click="
                            () => {
                              $router.go(-1);
                              emit('close', false);
                            }
                          "
                        >
                          Close
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
  </div>
</template>

<style scoped></style>
