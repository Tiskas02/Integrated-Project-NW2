<script setup>
import { useRouter } from "vue-router";
const router = useRouter();
const goBack = () => {
  router.push({ name: "board" });
};
</script>

<template>
  <div
    class="min-h-screen flex items-center justify-center"
    style="
      background-image: url('https://res.cloudinary.com/dyhavbbzf/image/upload/v1733736424/jcw7sjnihbpmwljizi7p.png');
      background-size: cover;
      background-position: center;
    "
  >
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
        Go Back to Board
      </button>
    </div>
  </div>
</template>

<style scoped></style>
