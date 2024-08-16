<script setup>
import { ref, computed } from "vue";

const user = ref({
  username: "",
  password: "",
});
const watchUser = computed(() => {
  return user.value.password, user.value.username;
});
</script>

<template>
  <div class="w-full h-screen flex justify-center items-center">
    <div
      class="w-[80%] h-[80%] bg-white rounded-xl shadow-xl flex justify-center items-center"
    >
      <div>
        <div
          class="text-slate-700 font-rubik font-semibold text-3xl text-center w-[20rem] mb-4"
        >
          Welcome
        </div>
        <div class="flex items-center justify-center">
          <div class="flex">
            <div class="w-9"><img src="/icon.png" alt="icon" /></div>
            <div
              class="text-center font-chivo font-medium text-md text-blue-220 text-slate-700 cursor-pointer hover:text-black w-[14rem] pt-1"
            >
              IT-Bangmod Kradan Kanban
            </div>
          </div>
        </div>
        <div>
          <label class="form-control w-full max-w-xs">
            <div class="label">
              <span class="label-text font-semibold text-slate-500"
                >Username</span
              >
            </div>
            <input
              type="text"
              placeholder="Enter your Username"
              class="input input-bordered text-sm w-[20rem] bg-white"
            />
            <!-- <div class="label">
              <span class="label-text-alt">
                Username cannot more than 50
              </span>
            </div> -->
          </label>
          <label class="form-control w-full max-w-xs">
            <div class="label">
              <span class="label-text font-semibold text-slate-500"
                >Password</span
              >
            </div>
            <input
              type="password"
              placeholder="Enter your password"
              class="input input-bordered text-sm w-[20rem] bg-white"
            />
            <!-- <div class="label">
              <span class="label-text-alt visible"
                >Password cannot more than 50
              </span>
            </div> -->
          </label>
          <div class="w-[20rem] btn btn-primary text-white mt-6">Login</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
