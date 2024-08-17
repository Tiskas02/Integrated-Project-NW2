<script setup>
import BaseBtn from "./BaseBtn.vue";
import { useRoute } from "vue-router";
import { ref,watch } from "vue";
import duration from "tailwindcss-animated/src/utilities/duration";
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
    <div class="fixed h-[70px] w-full border px-32 bg-white">
      <div class="flex mt-2">
        <div class="flex items-center">
          <div class="w-[3%]"><img src="/icon.png" alt="icon" /></div>
          <div
            class="font-chivo font-medium text-xl text-blue-220 text-slate-700 ml-2 cursor-pointer hover:text-black mr-7"
          >
            IT-Bangmod Kradan Kanban
          </div>
          <div class="w-32 px-7">
            <div :style="{
                    color: usePath ? 'slategray' : 'blue',
                    boxShadow: usePath ? 'none' : '0 3px 0 0 blue',
                    duration: '500ms',
                    transition: 'all 0.3s ease-in-out',
                  }">
              <router-link :to="{ name: 'task' }" custom v-slot="{ navigate }">
                <button @click="navigate" role="link" class="w-full py-4 justify-center">Tasks</button>
              </router-link>
            </div>
          </div>
          <div class="w-32 px-7">
          <div :style="{
                    color: !usePath ? 'slategray' : 'blue',
                    boxShadow: !usePath ? 'none' : '0 3px 0 0 blue',
                    duration: '500ms',
                    transition: 'all 0.3s ease-in-out',
                    
                  }">
              <router-link :to="{ name: 'status' }" custom v-slot="{ navigate }">
                <button @click="navigate" role="link" class="w-full py-4 justify-center">Status</button>
              </router-link>
            </div>
        </div>
      </div>
        <div class="flex-grow"></div>
        <div class="flex items-center">
          <div class="px-4 cursor-pointer hover:text-blue-700">Login</div>
          <div class="px-2 cursor-pointer">
            <BaseBtn>
              <template #default>
                <slot name="default"> Sign Up </slot>
              </template>
            </BaseBtn>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
