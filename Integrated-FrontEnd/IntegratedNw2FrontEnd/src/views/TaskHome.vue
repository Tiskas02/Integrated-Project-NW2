<script setup>
import { ref, watch } from "vue";
import TaskTable from "../components/TaskTable.vue";
import StatusTable from "../components/StatusTable.vue";
import { useRoute } from "vue-router";
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
    <div class="w-full h-screen flex justify-center items-center">
      <div class="w-[95%] h-[90%]">
        <div class="w-full bg-slate-100 h-full rounded-2xl shadow-inner">
          <div class="w-[100%] py-3 flex-col flex items-start">
            <div>
              <div
                class="font-bold text-3xl text-blue-220 mt-6 mx-10 text-slate-700"
              >
                IT-Bangmod Kradan Kanban
              </div>
            </div>
            <div class="flex w-full items-center justify-between">
              <div class="">
                <div class="text-sm breadcrumbs ml-8 text-slate-700">
                  <ul>
                    <li class="itbkk-botton-home text-lg">
                      <router-link :to="{ name: 'task' }">
                        <div>Home</div>
                      </router-link>
                    </li>
                    <li class="itbkk-botton-status text-lg ">
                      <router-link :to="{ name: 'status' }">Status</router-link>
                    </li>
                  </ul>
                </div>
              </div>
              <div v-if="!usePath">
              <div class="itbkk-botton-status mr-11 btn btn-outline btn-accent ">
                <router-link :to="{ name: 'status' }"
                  >Manage Status</router-link
                >
              </div></div>
            </div>
            <div v-if="usePath" class="w-full ">
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
