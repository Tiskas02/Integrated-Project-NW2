<script setup>
import { ref, onMounted ,watch} from 'vue';
import TaskTable from '../components/TaskTable.vue';
import StatusTable from '../components/StatusTable.vue';
// import {
//   getTaskById,
//   getTaskData,
//   addTask,
//   deleteItemById,
//   updateTask,
// } from "../libs/fetchUtil.js"
import { storeToRefs } from 'pinia';
import { useStoreTasks } from '../stores/taskStores.js';
import { useRoute } from 'vue-router';

// const dataLoaded = ref(false)
const tasksStore = useStoreTasks();
const { isLoading } = storeToRefs(tasksStore);
const route = useRoute();
const showPath = ref(false);

watch(() => route.path, () => {
    if (route.path.startsWith('/status')) {
      showPath.value = true;
    }else {
      showPath.value = false;
    }
  }, { immediate: true }
  );

</script>
<template>
  <div>
    <!-- <LoadingScreen v-if="!isLoading" /> -->
    <div  class="w-full h-screen flex justify-center items-center">
      <div class="w-[95%] h-[90%]">
        <div class="w-full bg-slate-100 h-full rounded-2xl shadow-inner">
          <div class="w-[100%] py-3 flex-col flex items-start">
            <div>
              <div class="font-bold text-3xl text-blue-220 mt-6 mx-10">
                IT-Bangmod Kradan Kanban
              </div>
            </div>
            <div class="text-sm breadcrumbs ml-8">
              <ul>
                <li class="text-lg">
                  <router-link :to="{ name: 'task' }">Home</router-link>
                </li>
                <li class="text-lg">
                  <router-link :to="{ name: 'status' }" >Status</router-link>
                </li>
              </ul>
            </div>
            <div v-if="showPath" class="w-full h-screen">
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
