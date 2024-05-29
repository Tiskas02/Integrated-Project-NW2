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
              <div class="mt-6 mx-10 flex items-center">
                <div
                  class="mobile:w-[70%] mobile:h-[70%] tablet:w-[7%] tablet:h-[7%] transition ease-in-out hover:scale-125 duration-300"
                >
                  <router-link :to="{ name: 'task' }"
                    ><img src="/icon.png"
                  /></router-link>
                </div>
                <div
                  class="font-bold text-3xl text-blue-220 text-slate-700 ml-2"
                >
                  IT-Bangmod Kradan Kanban
                </div>
              </div>
            </div>
            <div class="flex w-full items-center justify-between">
              <div>
                <div class="text-sm breadcrumbs ml-8 text-slate-700">
                  <ul>
                    <li class="itbkk-botton-home text-lg">
                      <router-link :to="{ name: 'task' }">
                        <div>Home</div>
                      </router-link>
                    </li>
                    <li class="itbkk-botton-status text-lg">
                      <router-link :to="{ name: 'status' }">Status</router-link>
                    </li>
                  </ul>
                </div>
              </div>
              <div v-if="!usePath">
                <router-link :to="{ name: 'status' }">
                  <div
                    class="itbkk-botton-status tablet:mr-11 btn btn-outline btn-accent mobile:mr-4 mobile:ml-4"
                  >
                    Manage Status
                  </div></router-link
                >
              </div>
            </div>
            <div v-if="usePath" class="w-full">
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
