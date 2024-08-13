<script setup>
import { ref, watch } from "vue";
import TaskTable from "../components/TaskTable.vue";
import StatusTable from "../components/StatusTable.vue";
import { useRoute } from "vue-router";
import NavBar from "@/shared/NavBar.vue";
const route = useRoute();
const usePath = ref(false);
watch(
  () => route.path,
  () => {
    if (route.path.startsWith("/status")) {
      usePath.value = true;
    } else {
      usePath.value = false;
    }
  },
  { immediate: true }
);
</script>
<template>
  <div>
    <NavBar />
    <div class="w-full h-screen flex justify-center items-center">
      <div class="w-[95%] h-[90%]">
        <div class="w-full bg-white h-full rounded-2xl shadow-inner">
          <div class="w-[100%] h-full flex-col flex justify-center">
            <div v-if="usePath " class="w-full">
              <StatusTable />
            </div>
            <div v-else class="w-full">
              <TaskTable />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
