<script setup>
import { defineProps, defineEmits } from "vue";
const emit = defineEmits(["close", "task"]);
const props = defineProps({
  task: Object,
});

const getRandomColor = () => {
  return "#" + Math.floor(Math.random() * 16777215).toString(16);
};
</script>

<template>
  <div>
    <div>
      <div
        class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-36 z-[2]"
        style="translate: transform(-50%, -50%)"
      >
        <div class="w-[60%] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
          >
            <div>
              <div
                class="itbkk-title text-xl font-semibold text-slate-800 my-4 break-all"
              >
                {{ task?.title }}
              </div>
            </div>
            <div class="flex my-1">
              <div class="2xl:w-[13%] sm:w-[17%] my-auto mr-4">Status</div>
              <div>
                <div
                  class="itbkk-status text-lg"
                  :style="{ color: getRandomColor() }"
                >
                  {{ task?.status.name }}
                </div>
              </div>
            </div>
            <div class="flex">
              <div class="2xl:w-[13%] sm:w-[17%] my-auto mr-4">Assignees</div>
              <div>
                <div
                  :style="{
                    fontStyle: task.assignees ? 'normal' : 'italic',
                  }"
                >
                  {{
                    task?.assignees == "" || task?.assignees === null
                      ? "Unassigned"
                      : task?.assignees
                  }}
                </div>
              </div>
            </div>
            <div>
              <div class="flex itbkk-timezone my-2">
                <div class="2xl:w-[13%] sm:w-[17%] mr-4">TimeZone</div>
                <div>
                  {{ Intl.DateTimeFormat().resolvedOptions().timeZone }}
                </div>
              </div>
              <div class="flex itbkk-created-on my-1">
                <div class="2xl:w-[13%] sm:w-[20%]">Created On</div>
                <div>
                  {{ new Date(task?.createdOn).toLocaleString("en-GB") }}
                </div>
              </div>
              <div class="flex itbkk-updated-on my-1">
                <div class="2xl:w-[13%] sm:w-[20%]">Updated On</div>
                <div>
                  {{ new Date(task?.updatedOn).toLocaleString("en-GB") }}
                </div>
              </div>
            </div>
            <div class="mt-5">
              <div role="tablist" class="tabs tabs-bordered mb-3">
                <a role="tab" class="tab tab-active text-lg font-bold"
                  >Description</a
                >
                <a role="tab" class="tab"></a>
                <a role="tab" class="tab"></a>
              </div>
              <div>
                <div>
                  <div
                    class="itbkk-description break-all italic"
                    placeholder="No Description Provided"
                  >
                    {{
                      task?.description == "" || task?.description === null
                        ? "No Description Provided"
                        : task?.description
                    }}
                  </div>
                </div>
              </div>
            </div>

            <div class="flex flex-row w-full justify-end">
              <div>
                <div
                  class="itbkk-button-cancel btn btn-error text-white"
                  @click="
                    () => {
                      emit('close', false);
                    }
                  "
                >
                  Cancel
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
