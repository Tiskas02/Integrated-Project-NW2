<script setup>
import { ref } from "vue";

const isMenuOpen = ref(false);

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};
</script>

<template>
    <div
  class="w-full h-screen bg-gray-700 text-white"
  :class="{ 'backdrop-blur-md ': isMenuOpen, 'backdrop-blur-0': !isMenuOpen }"
>
    <!-- Top Navigation Menu -->
    <div
      class="topnav relative bg-gray-800 flex flex-col tablet:flex-row items-start tablet:items-center justify-between"
    >
      <!-- Logo -->
      <a href="#home" class="block py-4 text-white bg-green-500">Logo</a>
      <!-- Hamburger Menu (visible only on mobile) -->
      <div
        id="myLinks"
        :class="{ absolute: isMenuOpen, hidden: !isMenuOpen }"
        class="transition-all duration-300 w-full bg-blue-700 tablet:hidden flex flex-col mt-14 pl-4 space-y-2"
      >
        <a href="#news" class="block py-2 px-4 hover:bg-gray-600">News</a>
        <a href="#contact" class="block py-2 px-4 hover:bg-gray-600">Contact</a>
        <a href="#about" class="block py-2 px-4 hover:bg-gray-600">About</a>
      </div>
      <!-- Hamburger Icon (visible only on mobile) -->
      <button
        @click="toggleMenu"
        class="tablet:hidden absolute top-4 right-4 p-4 bg-black text-white"
      >
        <i class="fa fa-bars"></i>
      </button>
      <!-- Pill Navigation (visible only on tablets and larger) -->
      <div class="hidden tablet:flex space-x-4">
        <a
          href="#news"
          class="py-2 px-4 bg-gray-600 hover:bg-gray-500 rounded-full"
          >News</a
        >
        <a
          href="#contact"
          class="py-2 px-4 bg-gray-600 hover:bg-gray-500 rounded-full"
          >Contact</a
        >
        <a
          href="#about"
          class="py-2 px-4 bg-gray-600 hover:bg-gray-500 rounded-full"
          >About</a
        >
      </div>
    </div>

    <div class="p-4" >
      <h3 class="text-lg font-bold">Responsive Navbar</h3>
      <p class="mt-2">
        This example demonstrates how a navigation menu switches between a
        hamburger menu on mobile and pill navigation on larger screens.
      </p>
      <p class="mt-2">
        This example demonstrates how a navigation menu switches between a
        hamburger menu on mobile and pill navigation on larger screens.
      </p>
      <p class="mt-2">
        This example demonstrates how a navigation menu switches between a
        hamburger menu on mobile and pill navigation on larger screens.
      </p>
      <p class="mt-2">
        This example demonstrates how a navigation menu switches between a
        hamburger menu on mobile and pill navigation on larger screens.
      </p>
    </div>
  </div>
</template>

<style scoped></style>