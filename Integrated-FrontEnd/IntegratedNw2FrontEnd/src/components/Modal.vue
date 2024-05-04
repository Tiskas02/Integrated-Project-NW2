<script setup>
import { ref, defineProps, defineEmits, onMounted,computed } from 'vue';
import TaskAddEdit from './TaskAddEdit.vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const emit = defineEmits(['setDetail']);
const props = defineProps({
  tasks: Object,
  mode: String
});
console.log(props.tasks);
const dataReceived = ref();
dataReceived.value = props.tasks;
console.log(dataReceived.value);
const sentEmits = (value) => {
  if (!value) {
    emit('setDetail', false);
  }
};
console.log(dataReceived.value);
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-[100px]"
    >
      <div class="w-[60%] m-[auto] max-h-[80%]">
        <div v-if="mode === 'add'">
          <TaskAddEdit @setModal="sentEmits"/>
          </div>
        <div v-if="mode === 'edit'">
          <TaskAddEdit  @setModal="sentEmits" :tasks="dataReceived" />
        </div>
        <div v-if="mode === 'view'">
          <div
            class="flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
          >
            <div
              class="itbkk-title text-xl font-semibold text-slate-800 my-4 break-all"
            >
              {{ tasks?.title }}
            </div>

            <div class="flex my-1">
              <div class="2xl:w-[13%] sm:w-[17%] my-auto">Status</div>
              <div>
                {{ tasks?.status }}
              </div>
            </div>
            <div class="flex my-1">
              <div class="my-auto 2xl:w-[13%] sm:w-[17%]">Assignees</div>
              <div>
                {{ tasks?.assignees }}
              </div>
            </div>

            <div class="flex itbkk-timezone my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">TimeZone</div>
              <div>{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</div>
            </div>
            <div class="flex itbkk-created-on my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">Created On</div>
              <div>
                {{ new Date(tasks?.createdOn).toLocaleString('en-GB') }}
              </div>
            </div>
            <div class="flex itbkk-updated-on my-1">
              <div class="2xl:w-[13%] sm:w-[17%]">Updated On</div>
              <div>
                {{ new Date(tasks?.updatedOn).toLocaleString('en-GB') }}
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
              <div class="itbkk-description overflow-x-auto break-all">
                {{ tasks?.description }}
              </div>
            </div>
            <div class="flex flex-row w-full justify-end mt-11">
              <div class="mr-2">
                <div
                  @click="
                    [
                      $emit('setDetail', false),
                      $router.replace({ name: 'task' })
                    ]
                  "
                  class="itbkk-button btn btn-info text-white"
                >
                  ok
                </div>
              </div>
              <div>
                <div
                  @click="
                    [
                      $emit('setDetail', false),
                      $router.replace({ name: 'task' })
                    ]
                  "
                  class="itbkk-button btn btn-error text-white"
                >
                  close
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
