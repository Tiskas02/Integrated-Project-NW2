<script setup>
import { ref, watch } from "vue";
import TaskTable from "../components/TaskTable.vue";
import StatusTable from "../components/StatusTable.vue";
import CollaboratorTable from "@/components/CollaberaterTable.vue";
import { useRoute } from "vue-router";
import Logo from "@/shared/Logo.vue";
import NavBar from "@/shared/NavBar.vue";
const route = useRoute();
const usePath = ref();
watch(
  () => route.path,
  () => {
    if (route.path.includes("/status")) {
      usePath.value = "status";
    } else if (route.path.includes("/task")) {
      usePath.value = "task";
    } else {
      usePath.value = "collaborator";
    }
  },
  { immediate: true }
);
</script>
<template>
  <div>
    <NavBar class="sticky top-0 z-50" />
    <div class="w-full h-full flex justify-center items-center">
      <div class="w-[100%] h-full tablet:w-[85%] ">
        <div class="w-full h-full ">
          <div class="w-[100%] h-full tablet:my-12 my-4">
            <div v-if="usePath === 'status'" class="w-full ">
              <StatusTable />
            </div>
            <div v-if="usePath === 'task'" class="w-full ">
              <TaskTable />
            </div>
            <div v-if="usePath === 'collaborator'" class="w-full">
              <CollaboratorTable />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
