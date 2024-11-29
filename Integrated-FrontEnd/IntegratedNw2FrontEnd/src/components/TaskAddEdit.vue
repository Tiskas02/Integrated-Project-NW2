<script setup>
import { defineProps, defineEmits, ref, computed, onMounted,watch } from "vue"
import { useStoreStatus } from "@/stores/statusStores"
import { useRoute } from "vue-router"
import NavBar from "@/shared/NavBar.vue";
const route = useRoute()
const emit = defineEmits(["close", "sentData"])
const statusStore = useStoreStatus()
const allStatus = ref([])
const routerId = ref(route.params.id)
const props = defineProps({
  mode: String,
  task: {
    type: Object,
    default: {
      id: undefined,
      assignees: null,
      status: 0,
      title: "",
      description: "",
      createdOn: "",
      updatedOn: "",
    },
  },
})

const defaultStatus = ref({
  statusId: null,
})

onMounted(async () => {
  allStatus.value = await statusStore.fetchStatus(routerId.value)
  const noStatus = allStatus.value.find((status) => status.name === "No Status")

  if (noStatus) {
    defaultStatus.value.statusId = noStatus.id
  }
})

const newTask = ref({
  ...props.task,
  statusId: props.task?.status?.statusId ?? defaultStatus.value.statusId,
})

const titleCharCount = computed(() =>
  newTask.value.title ? newTask.value.title.length : 0
)

const assigneesCharCount = computed(() =>
  newTask.value.assignees ? newTask.value.assignees.length : 0
)

const descriptionCharCount = computed(() =>
  newTask.value.description ? newTask.value.description.length : 0
)
const selectedStatus = ref(props?.task?.status?.name ?? "No Status");
const isDropdownOpen = ref(false);
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};
const updateStatus = (statusName) => {
  selectedStatus.value = statusName;
  newTask.value.statusId = allStatus.value.find((status) => status.name === statusName).id
  isDropdownOpen.value = false; // Close the dropdown after selection
};

// watch(newTask, (newValue) => {
//   console.log(newValue);
// }, { deep: true })
</script>

<template>
  <div>
    <div>
      <div
        class="itbkk-modal-task bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-16 tablet:pt-20"
        style="translate: transform(-50%, -50%)"
      >
        <div class="w-full tablet:w-[60%] laptop:w-[600px] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-b-xl tablet:rounded-lg shadow-xl"
          >
            <div>
              <div class="text-xl font-bold my-3 text-white">
                {{ mode === "add" ? "Add Task" : "Edit Task" }}
              </div>
              <div class="border-b my-2"></div>
              <div class="text-lg font-bold text-white">Title</div>
              <div>
                <textarea
                  class="itbkk-title w-full h-[90%] px-4 py-2 my-1 bg-[#b3d1e8] shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 placeholder:text-white"
                  placeholder="Enter your title here..."
                  required
                  maxlength="100"
                  v-model="newTask.title"
                  >{{ task?.title }}</textarea
                >
              </div>
              <div class="flex justify-end text-xs text-white">
                {{ titleCharCount }}/100
              </div>
            </div>
            <div class="flex flex-col my-1">
              <div class="text-lg font-bold text-white">Status</div>
              <div class="inline-block text-left">
                <!-- Dropdown button -->
                <div>
                  <button
                    type="button"
                    @click="toggleDropdown"
                    class="inline-flex justify-center w-full text-white font-semibold rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#b3d1e8] text-sm   focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    id="menu-button"
                    aria-expanded="true"
                    aria-haspopup="true"
                  >
                    {{ selectedStatus}}
                    <!-- Chevron Icon -->
                    <svg
                      class="-mr-1 ml-2 h-5 w-5"
                      xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 20 20"
                      fill="currentColor"
                      aria-hidden="true"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                        clip-rule="evenodd"
                      />
                    </svg>
                  </button>
                </div>

                <!-- Dropdown menu -->
                <div
                  v-if="isDropdownOpen"
                  class="absolute mt-2 w-[375px] tablet:w-[405px] laptop:w-[545px] rounded-md shadow-lg bg-[#b3d1e8] ring-1 ring-black ring-opacity-5 focus:outline-none"
                  role="menu"
                  aria-orientation="vertical"
                  aria-labelledby="menu-button"
                  tabindex="-1"
                >
                  <div class="py-1 text-center " role="none">
                    <a
                      v-for="status in allStatus"
                      :key="status.id"
                      href="#"
                      @click.prevent="updateStatus(status.name)"
                      class="block px-4 py-2 text-sm text-white font-semibold hover:bg-gray-100 hover:text-gray-900 border-b border-gray-100"
                      role="menuitem"
                      tabindex="-1"
                      :id="`menu-item-${status.id}`"
                    >
                      {{ status.name }}
                    </a>
                  </div>
                </div>
              </div>
             
            </div>
            <div class="flex flex-col">
              <div class="text-lg font-bold text-white">Assignees</div>
              <div>
                <div class="w-full">
                  <textarea
                    class="itbkk-assignees w-full h-[90%] px-4 py-2 my-1 bg-[#b3d1e8] shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 italic placeholder:text-slate-100"
                    :placeholder="
                      task?.assignees
                        ? task?.assignees
                        : 'Enter your assign here...'
                    "
                    maxlength="30"
                    v-model="newTask.assignees"
                    >{{ task?.assignees }}</textarea
                  >
                </div>
                <div class="flex justify-end text-xs text-white">
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
            <div>
              <div>
                <div class="text-lg font-bold text-white">Description</div>
                <div class="border w-full my-1"></div>
              </div>
              <div>
                <div>
                  <textarea
                    class="itbkk-description w-full h-[90%] px-4 py-2 my-1 bg-[#b3d1e8] text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner placeholder:text-slate-100"
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
                <div class="flex justify-end text-xs pb-3 text-white">
                  {{ descriptionCharCount }}/500
                </div>
              </div>
            </div>

            <div class="flex flex-row w-full justify-end">
              <div class="mr-2">
                <div v-if="mode !== 'view'">
                  <button
                    class="itbkk-button-confirm disabled btn bg-gradient-to-r from-blue-700 to-blue-300 border-none text-white"
                    @click="
                      () => {
                        $router.go(-1)
                        emit('sentData', newTask)
                        emit('close', false)
                      }
                    "
                    :disabled="
                      newTask.title.trim() === '' ||
                      ((newTask.assignees ?? '') === (task?.assignees ?? '') &&
                        (newTask.description ?? '') ===
                          (task?.description ?? '') &&
                        (newTask.statusId ?? '') ===
                          (task?.status?.statusId ?? '') &&
                        (newTask.title ?? '') === (task?.title ?? ''))
                    "
                  >
                    Save
                  </button>
                </div>
              </div>
              <div>
                <div
                  class="itbkk-button-cancel btn bg-gradient-to-r from-red-700 to-red-300 border-none text-white"
                  @click="
                    () => {
                      $router.go(-1)
                      emit('close', false)
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
