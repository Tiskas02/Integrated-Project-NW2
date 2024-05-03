<script setup>
const emit = defineEmits(['setDetail']);
const props = defineProps({
  tasks: Object,
  mode: String
});
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-[100px]"
    >
      <div class="w-[60%] m-[auto] max-h-[80%]">
        <div v-if="mode === 'add'">
          <div
            class="flex flex-col justify-between bg-white p-4 border-gray-200 rounded-lg shadow-xl"
          >
            <div class="text-xl font-bold my-3">Add New Task</div>
            <div class="border-b my-2"></div>
            <div class="text-lg">Title</div>
            <div>
              <textarea
                class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                placeholder="Enter your title here..."
              ></textarea>
            </div>
            <div class="flex my-1">
              <div class="max-w-fit my-auto mx-6">Status</div>
              <div>
                <label class="form-control w-full">
                  <select
                    class="itbkk-status select select-bordered bg-slate-100 shadow-inner text-black border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  >
                    <option disabled>Status</option>
                    <option>To do</option>
                    <option>Doing</option>
                    <option>Done</option>
                    <option selected>No status</option>
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
                ></textarea>
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
              <div class="">
                <textarea
                  class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner"
                  placeholder="Enter your description here..."
                ></textarea>
              </div>
            </div>
            <div class="flex flex-row w-full justify-end">
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
        <div v-if="mode === 'edit'">
          <div
            class="flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
          >
            <div class="text-xl font-bold my-3">Edit Task</div>
            <div class="border-b my-2"></div>
            <div class="text-lg mt-4">Title</div>
            <div>
              <textarea
                class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                placeholder="Enter your title here..."
                >{{ tasks?.title }}</textarea
              >
            </div>
            <div class="flex my-1">
              <div class="my-auto 2xl:w-[13%] sm:w-[17%]">Status</div>
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
              <div class="my-auto 2xl:w-[15%] sm:w-[20%]">Assignees</div>
              <div class="w-full">
                <textarea
                  class="itbkk-assignees w-[90%] h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  placeholder="Enter your assign here..."
                  :style="{
                    fontStyle: tasks && tasks.assignees ? 'normal' : 'italic'
                  }"
                  >{{ tasks?.assignees ?? 'Unassigned' }}</textarea
                >
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
              <div class="">
                <textarea
                  class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner"
                  placeholder="Enter your description here..."
                  :style="{
                    fontStyle: tasks && tasks.description ? 'normal' : 'italic'
                  }"
                  >{{
                    tasks?.description ?? 'No Description Provided'
                  }}</textarea
                >
              </div>
            </div>
            <div class="flex flex-row w-full justify-end mt-9">
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
