<script setup>
import { useRouter } from "vue-router";
const router = useRouter();
const goBack = () => {
    router.push({ name: "board" }); // Go to the previous page
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="text-center">
      <h1 class="text-6xl font-bold text-red-500">403</h1>
      <p class="mt-4 text-xl text-gray-700">Access Denied</p>
      <p class="mt-2 text-gray-500">
        You do not have permission to access this page.
      </p>
      <button
        @click="goBack"
        class="mt-6 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
      >
        Go Back
      </button>
    </div>
  </div>
</template>

<style scoped>
</style>