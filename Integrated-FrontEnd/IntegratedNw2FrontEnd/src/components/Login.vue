<script setup>
import router from "@/router/router";
import { ref, computed } from "vue";

const url = import.meta.env.VITE_BASE_URL_FOR_AUTH;
const user = ref({
  userName: "",
  password: "",
});
const showError = ref(false);

const validateUsername = computed(() => {
  return user.value.userName.length <= 50;
});

const validatePassword = computed(() => {
  return user.value.password.length <= 14;
});

const isFormValid = computed(() => {
  return (
    validateUsername.value &&
    validatePassword.value &&
    user.value.userName.length > 0 &&
    user.value.password.length > 0
  );
});

async function userLogin(user) {
  try {
    const res = await fetch(`${url}/login`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...user,
      }),
    });
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }

    const data = await res.json();

    localStorage.setItem("token", data.access_token);
    localStorage.setItem("refresh_token", data.refresh_token);
    return data;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

const submitForm = async () => {
  const payload = await userLogin(user.value);
  if (payload) {
    router.push("/board");
  } else {
    showError.value = true;
    setTimeout(() => {
      showError.value = false;
    }, 3000);
  }
};
</script>

<template>
  <div
    class="w-full max-h-screen flex flex-col tablet:flex-row my-16 tablet:my-0"
  >
    <div class="w-full tablet:h-screen flex justify-center tablet:items-center">
      <div class="flex items-center space-x-2 justify-center">
        <!-- Icon -->
        <img class="w-[25%]" src="/icon.png" alt="icon" />

        <!-- Text with typing animation -->
        <div
          class="relative w-fit overflow-hidden tablet:overflow-auto laptop:overflow-hidden flex items-center"
        >
          <div class="flex items-center">
            <span
              class="block text-xl font-bold whitespace-nowrap animate-typing text-black"
              style="white-space: nowrap"
            >
              IT-Bangmod Kradan Kanban
            </span>
            <!-- Blinking cursor with fade-out effect -->
            <span
              class="absolute inset-y-0 right-0 border-r-2 border-black animate-blink animate-duration-[800ms] animate-delay-[0ms]"
            ></span>
          </div>
        </div>
      </div>
    </div>

    <div class="w-full p-4 flex justify-center items-center">
      <div class="tablet:border tablet:p-24 tablet:rounded-xl tablet:bg-white">
        <div
          class="text-center font-sans font-medium text-3xl text-black mobile:mt-12 tablet:mt-0"
        >
          Welcome
        </div>
        <div
          class="text-center font-sans font-medium text-2xl text-black tablet:invisible"
        >
          IT-Bangmod Kradan Kanban
        </div>
        <div>
          <label class="form-control w-full max-w-xs my-2">
            <div class="label">
              <span class="label-text font-semibold text-black"
                >Username</span
              >
            </div>
            <input
              type="text"
              v-model="user.userName"
              placeholder="Enter your Username"
              class="itbkk-username input input-bordered text-sm w-[20rem] bg-white"
              :maxlength="50"
            />
            <div v-show="!validateUsername" class="label">
              <span class="label-text-alt text-red-500"
                >Username must be between 1 and 50 characters</span
              >
            </div>
          </label>
          <label class="form-control w-full max-w-xs my-2">
            <div class="label">
              <span class="label-text font-semibold text-black"
                >Password</span
              >
            </div>
            <input
              type="password"
              v-model="user.password"
              placeholder="Enter your password"
              class="itbkk-password input input-bordered text-sm w-[20rem] bg-white"
              :maxlength="14"
            />
            <div v-show="!validatePassword" class="label">
              <span class="label-text-alt text-red-500"
                >Password must be between 1 and 14 characters</span
              >
            </div>
          </label>
          <button
            class="itbkk-button-signin w-[20rem] btn bg-gradient-to-r from-[#023E8A] to-[#3581B9] border-none text-white mt-6"
            :class="{
              'opacity-50 cursor-not-allowed text-slate-600': !isFormValid,
            }"
            :disabled="!isFormValid"
            @click="submitForm"
          >
            Sign In
          </button>
          <div v-show="showError" class="itbkk-message my-2">
            <div role="alert" class="alert alert-error">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-6 w-6 shrink-0 stroke-current"
                fill="none"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
              <span class="itbkk-message"
                >Username or Password is incorrect</span
              >
            </div>
          </div>

          <div class="w-full flex flex-row mt-5">
            <div class="border border-slate-300 w-[45%] h-[1px] m-auto"></div>
            <div class="px-2 text-black">Other</div>
            <div class="border border-slate-300 w-[45%] h-[1px] m-auto"></div>
          </div>
          <div>
            <div class="w-full flex justify-center items-center mt-3">
              <button
                class="btn border-none w-[20rem] h-[10%] bg-white rounded shadow-md text-black hover:bg-black hover:text-white transition-all duration-300"
              >
                <img
                  class="w-[30px] h-[30px]"
                  src="/microsoft-icon.png"
                  alt="google"
                />
                Sign in with Microsoft
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@keyframes typing {
  0% {
    width: 0%;
  }
  100% {
    width: 100%;
  }
}

@keyframes blink {
  0%,
  100% {
    border-color: transparent;
  }
  50% {
    border-color: black;
  }
}
</style>
