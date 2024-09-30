<script setup>
import BaseBtn from "./BaseBtn.vue"
import { defineProps } from "vue"
import router from "@/router/router.js"
const props = defineProps({
  userPayload: String,
})

const parseJwt = (token) => {
  const base64Url = token.split(".")[1]
  const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/")
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2)
      })
      .join("")
  )
  return JSON.parse(jsonPayload)
}

const userPayload = localStorage.getItem("token")
  ? parseJwt(localStorage.getItem("token"))
  : null

const logout = () => {
  localStorage.removeItem("token")
  localStorage.removeItem("refresh_token")
  router.push("/")
}

</script>

<template>
  <div class="itbkk-home w-full flex items-center">
    <router-link :to="{ name: 'board' }">
      <div class="flex items-center">
        <slot name="image"><img src="/icon.png" class="w-[10%] m-2" /></slot>
        <slot name="text">
          <div
            class="text-center font-chivo font-medium text-md text-blue-220 text-white"
          >
            IT-Bangmod Kradan Kanban
          </div>
        </slot>
      </div>
    </router-link>
    <div class="grow"></div>
    <div class="mx-4">
      <div class="flex items-center">
        <div class="mx-2">
          <svg
            width="20"
            height="20"
            viewBox="0 0 20 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M9.99992 10C9.08325 10 8.29853 9.67362 7.64575 9.02084C6.99297 8.36807 6.66659 7.58334 6.66659 6.66668C6.66659 5.75001 6.99297 4.96529 7.64575 4.31251C8.29853 3.65973 9.08325 3.33334 9.99992 3.33334C10.9166 3.33334 11.7013 3.65973 12.3541 4.31251C13.0069 4.96529 13.3333 5.75001 13.3333 6.66668C13.3333 7.58334 13.0069 8.36807 12.3541 9.02084C11.7013 9.67362 10.9166 10 9.99992 10ZM3.33325 16.6667V14.3333C3.33325 13.8611 3.45492 13.4272 3.69825 13.0317C3.94159 12.6361 4.26436 12.3339 4.66659 12.125C5.5277 11.6945 6.4027 11.3717 7.29159 11.1567C8.18047 10.9417 9.08325 10.8339 9.99992 10.8333C10.9166 10.8328 11.8194 10.9406 12.7083 11.1567C13.5971 11.3728 14.4721 11.6956 15.3333 12.125C15.736 12.3333 16.0591 12.6356 16.3024 13.0317C16.5458 13.4278 16.6671 13.8617 16.6666 14.3333V16.6667H3.33325Z"
              fill="white"
            />
          </svg>
        </div>
        <div class="itbkk-fullname text-white font-rubik font-medium text-lg">
          {{ userPayload ? userPayload.name : "User" }}
        </div>
        <BaseBtn class="mx-4">
          <template #cancel>
            <button @click="logout">Logout</button>
          </template>
        </BaseBtn>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
