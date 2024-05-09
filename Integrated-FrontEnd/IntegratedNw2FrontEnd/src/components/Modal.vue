<script setup>
import { computed, onBeforeUnmount, ref, watch } from 'vue';

const emit = defineEmits(['setDetail', 'saveTask']);
const props = defineProps({
  tasks: {
    type: Object,
    default: {
      id: undefined,
      assignees: null,
      assignees: null,
      status: 'NO_STATUS',
      title: '',
      description: null,
      createdOn: null,
      updatedOn: null
    }
  },
  mode: String
});

const newTask = ref({
  id: undefined,
  assignees: null,
  status: 'NO_STATUS',
  title: '',
  description: '',
  createdOn: '',
  updatedOn: ''
});

watch(
  () => props.tasks,
  () => {
    if (props.mode === 'edit') {
      newTask.value = { ...props.tasks };
    }
  },
  { deep: true }
);


const isDisabled = computed(() => {
  if (props.mode === 'add') {
    return newTask.value.title.trim() === '';
  }
  if (props.mode === 'edit') {
    console.log(newTask.value.title.length);
    if (newTask.value.title.length !== 0) {
      return false;
    }
    return newTask.value.title.length;
  }
});
</script>

<template>
  <div>
    <div
      class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-[100px]"
      style="translate: transform(-50%, -50%)"
    >
      <div class="w-[60%] m-[auto] max-h-screen">
        <div
          class="overflow-auto max-h-screen flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
        >
          <div v-if="mode === 'view'">
            <div
              class="itbkk-title text-xl font-semibold text-slate-800 my-4 break-all"
            >
              {{ tasks?.title }}
            </div>
          </div>
          <div v-else>
            <div class="text-xl font-bold my-3">
              {{ mode === 'add' ? 'Add New Task' : 'Edit Task' }}
            </div>
            <div class="border-b my-2"></div>
            <div class="text-lg z-0">Title</div>
            <div>
              <textarea
                class="itbkk-title w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                placeholder="Enter your title here..."
                v-model="newTask.title"
                required
                >{{ mode === 'add' ? '' : tasks?.title }}</textarea
              >
            </div>
          </div>
          <div class="flex my-1">
            <div class="max-w-fit my-auto mx-6">Status</div>
            <div v-if="mode === 'view'">
              <div>
                {{ tasks?.status }}
              </div>
            </div>
            <div v-else>
              <label class="form-control w-full">
                <select
                  class="itbkk-status select select-bordered bg-slate-100 shadow-inner text-black border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  v-model="newTask.status"
                >
                  <option disabled selected>Status</option>
                  <option value="TO_DO" :selected="tasks?.status === 'TO_DO'">
                    To Do
                  </option>
                  <option value="DOING" :selected="tasks?.status === 'DOING'">
                    Doing
                  </option>
                  <option value="DONE" :selected="tasks?.status === 'DONE'">
                    Done
                  </option>
                  <option
                    value="NO_STATUS"
                    :selected="tasks?.status === 'NO_STATUS'"
                  >
                    No Status
                  </option>
                </select>
              </label>
            </div>
          </div>
          <div class="flex">
            <div class="my-auto mx-2">Assignees</div>
            <div v-if="mode === 'view'">
              <div>
                {{ tasks?.assignees }}
              </div>
            </div>
            <div v-else>
              <div class="w-full">
                <textarea
                  class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 italic"
                  placeholder="Enter your assign here..."
                  v-model="newTask.assignees"
                >
                  {{ tasks?.assignees }}
                  </textarea
                >
              </div>
            </div>
          </div>
          <div v-if="mode === 'view' || mode === 'edit'">
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
              <a role="tab" class="tab tab-active text-lg font-bold"
                >Description</a
              >
              <a role="tab" class="tab"></a>
              <a role="tab" class="tab"></a>
            </div>
            <div>
              <div>
                <div
                  v-if="mode === 'view'"
                  class="itbkk-description break-all italic"
                  placeholder="No Description Provided"
                >
                  {{
                    tasks?.description == '' || tasks?.description === null
                      ? 'No Description Provided'
                      : tasks?.description
                  }}
                </div>
                <textarea
                  v-else
                  class="itbkk-description w-full h-[90%] px-4 py-2 my-1 bg-slate-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner"
                  placeholder="Enter your description here..."
                  v-model="newTask.description"
                  >{{ tasks?.description }}</textarea
                >
              </div>
            </div>
          </div>

          <div class="flex flex-row w-full justify-end">
            <div class="mr-2">
              <div v-if="mode !== 'view'">
                <button
                  @click="
                    () => {
                      $emit('setDetail', false);
                      $emit('saveTask', newTask);
                    }
                  "
                  class="itbkk-button-confirm disabled btn btn-info text-white"
                  :class="isDisabled ? 'bg-gray-300' : 'bg-info'"
                  :disabled="
                    newTask.title.trim() === '' ||
                    ((newTask.assignees ?? '') ===
                      (tasks?.assignees ?? '') &&
                      (newTask.description ?? '') ===
                        (tasks?.description ?? '') &&
                      (newTask.status ?? '') === (tasks?.status ?? '') &&
                      (newTask.title ?? '') === (tasks?.title ?? ''))
                  "
                >
                  Save
                </button>
              </div>
            </div>
            <div>
              <div
                @click="
                  [$emit('setDetail', false), $router.replace({ name: 'task' })]
                "
                class="itbkk-button-cancel btn btn-error text-white"
              >
                Cancel
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.view {
  color: #047857;
  border-color: #047857;
  border-radius: 5px;
  background-color: #d1fae5;
}

.success {
  color: #047857;
  border-color: #047857;
  border-radius: 5px;
  background-color: #d1fae5;
}
</style>
