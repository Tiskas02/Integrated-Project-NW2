import { defineStore } from "pinia";
import { ref } from "vue";
import { updateLimitSettings as updateSettings, fetchLimitSettings as fetchSettings } from "../libs/api/status/fetchUtilStatus.js";

export const useStoreLimit = defineStore("limit", () => {
  const isLimitEnabled = ref(false);
  const maxTasks = ref(0);

  async function updateLimitSettings(enable, newMaxTasks) {
    try {
      await updateSettings(enable, maxTasks);
      isLimitEnabled.value = enable;
      maxTasks.value = newMaxTasks;
    } catch (error) {
      console.error("Error updating limit settings:", error);
    }
  }

  async function fetchLimitSettings() {
    try {
      const settings = await fetchSettings();
      isLimitEnabled.value = settings.isLimitEnabled;
      maxTasks.value = settings.maxTasks;
    } catch (error) {
      console.error("Error fetching limit settings:", error);
    }
  }

  return {
    isLimitEnabled,
    maxTasks,
    updateLimitSettings,
    fetchLimitSettings,
  };
});
