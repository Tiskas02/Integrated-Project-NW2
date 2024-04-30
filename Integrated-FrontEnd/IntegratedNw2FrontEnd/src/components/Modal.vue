<script setup>
const emit = defineEmits(['setDetail'])
const props = defineProps({
  tasks:Object
})

</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-blur-sm w-screen h-screen fixed top-0 left-0 pt-[100px] "
    >
      <div class="w-[90%] m-[auto]">
        <div
          class="flex flex-col justify-between bg-slate-600 p-4 border-gray-200 rounded-lg"
        >
          <div class="w-full h-[10%] mt-2">
            <h1 class="itbkk-title text-xl font-semibold text-white">{{ tasks?.title }}</h1>
          </div>
          <div class="border-b w-full mt-4"></div>
          <div class="flex flex-row">
            <div class="w-[70%] h-[50%]">
              <div class="pl-4 mt-4 font-semibold text-white">Description</div>
              <div class="w-full h-[420px]">
                <textarea
                  class="itbkk-description w-[95%] h-[90%] px-4 py-2 mx-4 my-2 bg-gray-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  placeholder="Enter your text here..." :style="{ fontStyle: tasks && tasks.description ? 'normal' : 'italic' }"
                >{{ tasks?.description ?? 'No Description Provided'}}</textarea>
              </div>
            </div>
            <div class=" w-[30%] h-[50%] flex-col">
              <div class="h-[180px]">
                <div class="pl-4 mt-4 font-semibold text-white">Assignees</div>
                <div class="h-[150px]">
                  <textarea
                    class="itbkk-assignees w-[95%] h-[90%] px-4 py-2 mx-4 my-2 bg-gray-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                    placeholder="Enter your text here..." :style="{fontStyle: tasks && tasks.assignees ? 'normal' : 'italic'}"
                  >{{ tasks?.assignees ?? 'Unassigned'}}</textarea>
                </div>
              </div>
              <div class=" w-full h-[100px]">
                <label class="form-control w-full pl-4">
                  <div class="label font-semibold ">
                    <span class="label-text text-white"
                      >Status</span
                    >
                  </div>
                  <select
                    class="itbkk-status select select-bordered w-full bg-inherit text-white "
                  >
                  
                    <option disabled selected >Status</option>
                    <option :selected="tasks?.status === 'TO_DO'">To do</option>
                    <option :selected="tasks?.status === 'DOING'">Doing</option>
                    <option :selected="tasks?.status === 'DONE'">Done</option>
                    <option :selected="tasks?.status === 'NO_STATUS'">No status</option>
                  </select>
                </label>
              </div>
              <div class="mt-10 ml-4">
              <div class="itbkk-timezone text-white"><div class="font-semibold">TimeZone</div><div>{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</div></div>
              <div class="itbkk-created-on text-white"><div class="font-semibold">Created On</div><div>{{ new Date(tasks?.createdOn).toLocaleString("en-GB") }}</div></div>
              <div class="itbkk-updated-on text-white"><div class="font-semibold">Updated On</div><div>{{ new Date(tasks?.updatedOn).toLocaleString("en-GB") }}</div></div>
            </div>
            </div>
          </div>
          <div class="flex flex-row w-full justify-end">
          <div class="mr-2">
            <div @click="[$emit('setDetail', false),$router.replace({ name: 'task'})]" class="itbkk-button btn bg-emerald-500 hover:bg-emerald-600 text-white">ok</div>
          </div>
          <div >
            <div @click="[$emit('setDetail', false), $router.replace({ name: 'task'})]" class="itbkk-button btn bg-red-500 hover:bg-red-600 text-white">close</div>
          </div>
        </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
