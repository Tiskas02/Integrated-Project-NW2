<script setup></script>

<template>
  <div class="bg-black w-[100%] h-[100vh] flex justify-center items-center">
    <div class="loader"></div>
  </div>
</template>

<style scoped>
.loader {
  border: 32px solid #f1f1f1;
  border-top: 32px solid #0061e9;
  border-radius: 50%;
  width: 200px;
  height: 200px;
  animation: spin 1.5s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>
