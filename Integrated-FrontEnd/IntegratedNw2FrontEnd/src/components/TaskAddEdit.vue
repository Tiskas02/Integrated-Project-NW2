<script setup>
import { defineProps } from 'vue';
const emit = defineEmits(['setModal']);
const props = defineProps({
  tasks: Object
});
console.log(props.tasks);
</script>
 
<template>
    <div>
        <div class="flex flex-col justify-between bg-white p-4 border-gray-200 rounded-lg shadow-xl">
            <div class="text-xl font-bold my-3">Add New Task</div>
            <div class="border-b my-2"></div>
            <div class="text-lg">Title</div>
            <div>
                <textarea
                    class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                    placeholder="Enter your title here...">{{ tasks?.title }}</textarea>
            </div>
            <div class="flex my-1">
                <div class="max-w-fit my-auto mx-6">Status</div>
                <div>
                    <label class="form-control w-full">
                  <select
                    class="itbkk-status select select-bordered bg-slate-100 shadow-inner text-black border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  >
                    <option disabled selected>Status</option>
                    <option :selected="tasks?.status === 'TO_DO'">To do</option>
                    <option :selected="tasks?.status === 'DOING'">Doing</option>
                    <option :selected="tasks?.status === 'DONE'">Done</option>
                    <option :selected="tasks?.status === 'NO_STATUS'">
                      No status
                    </option>
                  </select>
                </label>
                </div>
            </div>
            <div class="flex">
                <div class="my-auto mx-2">Assignees</div>
                <div class="w-full">
                    <textarea
                        class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                        placeholder="Enter your assign here..."
                        :style="{
                    fontStyle: tasks && tasks.assignees ? 'normal' : 'italic'
                  }"
                        >{{ tasks?.assignees ?? 'Unassigned' }}</textarea>
                </div>
            </div>
            <div v-if="tasks?.length >= 0">
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
            </div>
            <div class="mt-5">
                <div role="tablist" class="tabs tabs-bordered mb-3">
                    <a role="tab" class="tab tab-active text-lg font-bold">Description</a>
                    <a role="tab" class="tab"></a>
                    <a role="tab" class="tab"></a>
                </div>
                <div class="">
                    <textarea
                        class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner"
                        placeholder="Enter your description here..."></textarea>
                </div>
            </div>
            <div class="flex flex-row w-full justify-end">
                <div class="mr-2">
                    <div @click="
                        [
                            $emit('setModal', false),
                            $router.replace({ name: 'task' })
                        ]
                        " class="itbkk-button btn btn-info text-white">
                        ok
                    </div>
                </div>
                <div>
                    <div @click="
                        [
                            $emit('setModal', false),
                            $router.replace({ name: 'task' })
                        ]
                        " class="itbkk-button btn btn-error text-white">
                        close
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>