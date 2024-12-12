<script setup>
import { useRouter } from "vue-router";
const router = useRouter();
const goHome = () => {
  router.push({ name: "board" });
};
</script>

<template>
  <div
    class="flex flex-col justify-center items-center min-h-screen"
    style="
      background-image: url('https://res.cloudinary.com/dyhavbbzf/image/upload/v1733736424/jcw7sjnihbpmwljizi7p.png');
      background-size: cover;
      background-position: center;
    "
  >
    <h1 class="text-9xl font-extrabold text-red-500">404</h1>
    <p class="mt-4 text-2xl text-gray-700">Page Not Found</p>
    <p class="mt-2 text-gray-500 text-lg text-center max-w-md">
      Sorry, the page you are looking for does not exist. You may have mistyped
      the address or the page may have been moved.
    </p>
    <button
      @click="goHome"
      class="mt-6 px-6 py-3 bg-blue-500 text-white text-lg font-semibold rounded-lg shadow-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-opacity-75"
    >
      Go Back to Board
    </button>
  </div>
</template>

<style scoped></style>
