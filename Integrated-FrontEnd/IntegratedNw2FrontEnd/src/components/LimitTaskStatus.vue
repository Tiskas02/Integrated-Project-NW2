<script setup>
import { defineProps, defineEmits, ref, computed, watch , onMounted} from "vue";
import { useStoreLimit } from "@/stores/limitStores";
import { useStoreStatus } from "@/stores/statusStores";
import { storeToRefs } from "pinia";
import useToasterStore from "../stores/notificationStores";


const emit = defineEmits(["limit", "close"]);
const props = defineProps({
  task: Object,
  index: Number,
  status: {
    type: Object,
  }
});

const limitStore = useStoreLimit();
const statusStore = useStoreStatus();
const { statuses } = storeToRefs(statusStore);

// Initialize isLimitEnabled and maxTasks as refs
const isLimitEnabled = ref(false);
const maxTasks = ref(0);
const toasterStore = useToasterStore();

// Fetch limit settings on component mount
const fetchLimitSettings = async () => {
  try {
    await limitStore.fetchLimitSettings();
    isLimitEnabled.value = limitStore.isLimitEnabled;
    maxTasks.value = limitStore.maxTasks;
  } catch (error) {
    console.error("Error fetching limit settings:", error);
    toasterStore.error({ text: "An error occurred while fetching the limit settings." });
  }
};

onMounted(() => {
  fetchLimitSettings();
});

// Ensure the maxTasks value is within limits
watch(maxTasks, (newValue) => {
  if (newValue < 0) {
    maxTasks.value = 0;
  } else if (newValue > 10) {
    maxTasks.value = 10;
  }
});

// Compute the statuses exceeding the task limit
const exceedingStatuses = computed(() => {
  if (!statuses.value || !statuses.value.length) return [];
  return statuses.value.filter(status => status.noOfTasks > maxTasks.value);
});

// Check if the save button should be enabled or disabled
const isSaveDisabled = computed(() => {
  return isLimitEnabled.value === limitStore.isLimitEnabled && 
  maxTasks.value === limitStore.maxTasks;
});

const saveLimitSettings = async () => {
  try {
    await limitStore.updateLimitSettings(isLimitEnabled.value, maxTasks.value);
    emit("close");
  } catch (error) {
    console.error("Error saving limit settings:", error);
    toasterStore.error({ text: "An error occurred while saving the limit settings." });
  }
};

// Logging for debugging
console.log("Statuses:", statuses.value);
console.log("Exceeding Statuses:", exceedingStatuses.value);

</script>

<template>
  <div class="fixed inset-0 bg-grey-500 backdrop-brightness-50 flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-xl w-[90%] sm:w-[50%] md:w-[40%] p-6 itbkk-modal-setting">
      <h2 class="text-2xl font-semibold my-2">Status Settings</h2>
      <hr>
      <p class="mt-2">Users can limit the number of task in a status by setting the Maximum tasks in each status </p>
      <p class="mb-3">(except <span class="text-gray-600">"No Status"</span> and <span class="text-green-400">"Done"</span> statuses).</p>
      <div class="flex items-center mb-4">
        <input
          type="checkbox"
          id="limit-tasks"
          class="toggle toggle-accent itbkk-limit-task mr-3"
          v-model="isLimitEnabled"
        />
        <label for="limit-tasks" class="cursor-pointer text-lg">Limit tasks in this status</label>
      </div>
      <div v-if="isLimitEnabled" class="flex gap-2 mb-4">
        <label for="max-tasks" class="text-lg self-center">Maximum tasks</label>
        <input
          type="number"
          id="max-tasks"
          class="itbkk-max-task input input-bordered w-[12%]"
          v-model="maxTasks"
        />
      </div>
      <div v-if="isLimitEnabled && exceedingStatuses.length >= 1" class="mb-4">
        <p class="text-red-600">These statuses have reached the task limit. No additional tasks can be added to these statuses at this time:</p>
        <ul class="flex gap-2 mt-2 overflow-x-auto">
          <li v-for="status in exceedingStatuses" :key="status.id">
            {{ status.name }}
          </li>
        </ul>
      </div>
      <hr>
      <div class="flex justify-end mt-3">
        <button
          @click="saveLimitSettings"
          class="btn btn-success itbkk-button-confirm text-white"
          :disabled="isSaveDisabled"
        >
          Save
        </button>
        <button
          @click="emit('close')"
          class="btn btn-error itbkk-button-cancel mx-2 text-white"
        >
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
