<script setup>
import { defineProps, defineEmits, ref, computed, onMounted } from "vue";
import { useStoreStatus } from "@/stores/statusStores";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();
const emit = defineEmits(["close", "sentData"]);
const statusStore = useStoreStatus();
const allStatus = ref([]);
const routerId = ref(route.params.id);
onMounted(async () => {
  allStatus.value = await statusStore.fetchStatus(routerId.value);
  console.log(allStatus.value);
});
const props = defineProps({
  mode: String,
  task: {
    type: Object,
    default: {
      id: undefined,
      assignees: null,
      status: {
        id: undefined,
        name: "",
        description: "",
      },
      title: "",
      description: "",
      createdOn: "",
      updatedOn: "",
    },
  },
});
const newTask = ref({
  ...props.task,
  status: props.task.status.id ? props.task.status.id : 1,
});

console.log(newTask.value);

const titleCharCount = computed(() =>
  newTask.value.title ? newTask.value.title.length : 0
);
const assigneesCharCount = computed(() =>
  newTask.value.assignees ? newTask.value.assignees.length : 0
);
const descriptionCharCount = computed(() =>
  newTask.value.description ? newTask.value.description.length : 0
);
computed(newTask.value, () => {
  Errortext.value.title == "" &&
    Errortext.value.description == "" &&
    Errortext.value.assignee == "";
  if (newTask.value.title.trim().length > 100) {
    Errortext.value.title = "Title has longer than 100 character";
  } else if (newTask.value.title.trim().length == 0) {
    Errortext.value.title = "Title can not be empty!!";
  } else if (newTask.value.description.trim().length > 500) {
    Errortext.value.description = "Description has longer than 500 character";
  } else if (newTask.value.assignees.trim().length > 30) {
    Errortext.value.assignees = "Assignees has longer than 30 character";
  } else {
    Errortext.value.assignees = "";
  }
});
</script>

<template>
  <div>
    <div>
      <div
        class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-10 z-[2]"
        style="translate: transform(-50%, -50%)"
      >
        <div class="w-[60%] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
          >
            <div>
              <div class="text-xl font-bold my-3">
                {{ mode === "add" ? "Add Task" : "Edit Task" }}
              </div>
              <div class="border-b my-2"></div>
              <div class="text-lg z-0">Title</div>
              <div>
                <textarea
                  class="itbkk-title w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  placeholder="Enter your title here..."
                  required
                  maxlength="100"
                  v-model="newTask.title"
                  >{{ task?.title }}</textarea
                >
                
              </div>
              <div class="flex justify-end text-xs">
                {{ titleCharCount }}/100
              </div>
            </div>
            <div class="flex my-1">
              <div class="max-w-fit my-auto mx-6">Status</div>

              <div>
                <label class="form-control w-full">
                  <select
                    class="itbkk-status select select-bordered bg-slate-100 shadow-inner text-black border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                    v-model="newTask.id"
                  >
                    <option
                      v-for="status in allStatus"
                      :key="status.id"
                      :value="status.id"
                    >
                      {{ status.name}}
                    </option>
                    <!-- {{task?.status.statusName}} -->
                  </select>
                </label>
              </div>
            </div>
            <div class="flex">
              <div class="my-auto mx-2">Assignees</div>
              <div>
                <div class="w-full">
                  <textarea
                    class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 italic"
                    :placeholder="
                      task?.assignee
                        ? 'Enter your assign here...'
                        : 'Unassigned'
                    "
                    maxlength="30"
                    v-model="newTask.assignees"
                    >{{ task?.assignee }}</textarea
                  >
                </div>
                <div class="flex justify-end text-xs">
                  {{ assigneesCharCount }}/30
                </div>
              </div>
            </div>
            <div v-if="mode === 'edit'">
              <div class="flex itbkk-timezone my-1">
                <div class="2xl:w-[13%] sm:w-[17%]">TimeZone</div>
                <div>
                  {{ Intl.DateTimeFormat().resolvedOptions().timeZone }}
                </div>
              </div>
              <div class="flex itbkk-created-on my-1">
                <div class="2xl:w-[13%] sm:w-[17%]">Created On</div>
                <div>
                  {{ new Date(task?.createdOn).toLocaleString("en-GB") }}
                </div>
              </div>
              <div class="flex itbkk-updated-on my-1">
                <div class="2xl:w-[13%] sm:w-[17%]">Updated On</div>
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
                  <textarea
                    class="itbkk-description w-full h-[90%] px-4 py-2 my-1 bg-slate-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner"
                    :placeholder="
                      task?.description
                        ? 'Enter your description here...'
                        : 'No Description Provided'
                    "
                    maxlength="500"
                    v-model="newTask.description"
                    >{{ task?.description }}</textarea
                  >
                </div>
                <div class="flex justify-end text-xs pb-3">
                  {{ descriptionCharCount }}/500
                </div>
              </div>
            </div>

            <div class="flex flex-row w-full justify-end">
              <div class="mr-2">
                <div v-if="mode !== 'view'">
                  <button
                    class="itbkk-button-confirm disabled btn btn-info text-white"
                    @click="
                      () => {
                        $router.go(-1);
                        emit('sentData', newTask);
                        emit('close', false);
                      }
                    "
                    :disabled="
                      newTask.title.trim() === '' ||
                      ((newTask.assignees ?? '') === (task?.assignees ?? '') &&
                        (newTask.description ?? '') ===
                          (task?.description ?? '') &&
                        (newTask.status ?? '') === (task?.status.id ?? '') &&
                        (newTask.title ?? '') === (task?.title ?? ''))
                    "
                  >
                    Save
                  </button>
                </div>
              </div>
              <div>
                <div
                  class="itbkk-button-cancel btn btn-error text-white"
                  @click="
                    () => {
                      $router.go(-1);
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
