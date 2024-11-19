<script setup>
import { ref, defineProps, watchEffect } from "vue";
const props = defineProps({
  mode: String,
});
watchEffect(() => {
  console.log("Mode prop updated:", props.mode);
});

const isMenuOpen = ref(false);

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};
const parseJwt = (token) => {
  const base64Url = token.split(".")[1];
  const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );
  return JSON.parse(jsonPayload);
};

const userPayload = localStorage.getItem("token")
  ? parseJwt(localStorage.getItem("token"))
  : null;

const logout = () => {
  localStorage.removeItem("token")
  localStorage.removeItem("refresh_token")
  router.push("/")
}
</script>
<template>
  <div
    class="text-white"
    :class="{ 'backdrop-blur-md': isMenuOpen, 'backdrop-blur-0': !isMenuOpen }"
  >
    <!-- Top Navigation Menu -->
    <div
      class="topnav relative bg-white h-[60px] flex flex-col tablet:flex-row items-start tablet:items-center justify-between"
    >
      <!-- Logo -->
      <div class="flex flex-row">
        <img
          src="/icon.png"
          alt="icon"
          class="w-[15%] m-2 hover:animate-pulse"
        />
        <div
          class="flex items-center justify-start font-chivo font-medium text-md text-slate-500 mx-1"
        >
          IT-Bangmod Kradan Kanban {{ mode }}
        </div>
      </div>
      <!-- Hamburger Menu (visible only on mobile) -->
      <div
        id="myLinks"
        :class="{ absolute: isMenuOpen, hidden: !isMenuOpen }"
        class="transition-all animate-fade-down animate-once animate-ease-in-out animate-duration-[600ms] animate-fill-both w-full bg-[#81B2D6] tablet:hidden flex flex-col mt-[60px] space-y-2 rounded-b-2xl shadow-md"
      >
        <RouterLink
          v-if="mode === 'board'"
          to="/board"
          class="block py-4 hover:bg-gray-600 border-b-[1px] text-center"
        >
          {{ userPayload.name }}
        </RouterLink>

        <!-- Show Task, Status, and Collaborator links only if mode is not "board" -->
        <template v-else>
          <RouterLink
            to="/board/:id/task"
            class="block py-3 hover:bg-gray-600 border-b-[1px] text-center text-lg"
          >
            Task
          </RouterLink>
          <RouterLink
            to="/board/:id/status"
            class="block py-3 hover:bg-gray-600 border-b-[1px] text-center text-lg"
          >
            Status
          </RouterLink>
          <RouterLink
            to="/board/:id/collab"
            class="block py-3 hover:bg-gray-600 border-b-[1px] text-center text-lg"
          >
            Collaborator
          </RouterLink>
        </template>

        <!-- Always show the Logout link -->
        <RouterLink
          to="/login"
          class="block py-3 hover:bg-gray-600 text-center text-lg pb-4"
          @click="logout"
        >
          Logout
        </RouterLink>
      </div>
      <!-- Hamburger Icon (visible only on mobile) -->
      <button
        @click="toggleMenu"
        class="tablet:hidden absolute right-1 p-4 text-white hover:animate-jump hover:animate-once hover:animate-ease-in-out"
      >
        <svg
          width="30"
          height="30"
          viewBox="0 0 30 30"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
        >
          <rect width="30" height="30" fill="url(#pattern0_267_529)" />
          <defs>
            <pattern
              id="pattern0_267_529"
              patternContentUnits="objectBoundingBox"
              width="1"
              height="1"
            >
              <use xlink:href="#image0_267_529" transform="scale(0.01)" />
            </pattern>
            <image
              id="image0_267_529"
              width="100"
              height="100"
              xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAACXBIWXMAAAsTAAALEwEAmpwYAAADFklEQVR4nO3cbTNUcRzGce8Ak5CEdRMSiSV2NXmw76mbN+GmUipFUiqVQpRyk7BrmulJM85idy3H/WLtzvT016xNu6rRLKdx8P3MXK/g8j/H/PeaExUFAAAAAAAAAAAAADu6aF03mEf9lRVjfotWKRsJj9dSNhSMMZD+rXiC6fVYCsPTHUp+92IwnaHktodHteS2BZO1ldbtMbS4Q2kKj8OSEkhDKEmB1IfHbkmqs1vit1L7Z45VjVcmVI2n7fnPrGLMm262+gfNNr9oGZPVJ6ZRn5SP+KRseEPKPm/IhSGvlH7ySungupQMrIuxf02MfWtS/HFVij6sSlGvR86/90jhO48U9qzIuZ4VKeheloK3y5LftST5nUtytmNR8t4sSt7rBTnTviC5r+Yl9+W85LyYk+y2Ocl+Pienn6mS9VSVrNZZyXwyKxmPZySjZUbSH7nF0OwWw8NpSWualrRGl6Q2uiTlgUtS7jvlVINTku85JPmuQ07emZKk+ilJuj0pJ25NSmLdhCTenJCEG3aJv26X+Fq7HK9R/pb+hFrFsKsyLn1djzHbfNOU4dSqjM3EVSvT0VWumIgLMVl91yjDqWkZv1KtXIm4ELPN18Vjyql9GZuFjHdGfkJsvn7eGQ7ty/j5Ltm3Qo7wC1x0VwhlKPophDIUbU+I2ebvpIxJLR9T4emIvBCr7yonY/J/lBHI5YgLKf6yFm22+V08piY0LSOuRnHF1juio3Z7h2Wy+gd4Z9i1ORnVSp8md1rlw99Td7pcPEoXhfG7SOByMbb6W8qeiwAAAAAAAMA/MZRzMJQzHs7f2RnK5einjODvIQzl5nRTBkO5Nh2WwVBO1VcZDOVUvZWxP7sstlmKvgphKKdoWwhDuSmtH1PhYShn2N93xu9hKGfQSRkM5Zp1dDIYyrkZygEAAAAAABwxDOUcDOWMDOW244tyLoZyJUflU018UU7VTxkM5VR9lcFQTtVbGQzlMvVVxv4UwlBO0bYQhnJTDOUKD9O/tgzl1ANRBkO5Zv2UwVCuiaEcAAAAAAAAAAAAgKgD5AfxfWg35htxzgAAAABJRU5ErkJggg=="
            />
          </defs>
        </svg>
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
  </div>
</template>
