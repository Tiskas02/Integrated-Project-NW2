<script setup>
import router from "@/router/router";
import { ref, computed } from "vue";
const url = import.meta.env.VITE_BASE_URL;
const user = ref({
  username: "",
  password: "",
});
const showError = ref(false);

const validateUsername = computed(() => {
  return user.value.username.length <= 50;
});

const validatePassword = computed(() => {
  return user.value.password.length <= 14;
});

// Computed property to determine if the form is valid
const isFormValid = computed(() => {
  return (
    validateUsername.value &&
    validatePassword.value &&
    user.value.username.length > 0 &&
    user.value.password.length > 0
  );
});
function parseJwt(token) {
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
}
async function userLogin(user) {
  try {
    const res = await fetch(`${url}/v2/user`, {
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
    const payload = parseJwt(data.token);
    console.log("Token Payload:", payload);
    localStorage.setItem("userPayload", JSON.stringify(payload));
    return payload;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}
const submitForm = async () => {
  const payload = await userLogin(user.value);
  if (payload) {
    router.push("/task");
  } else {
    showError.value = true;
    setTimeout(() => {
      showError.value = false;
    }, 3000); 
  }
};
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
              v-model="user.username"
              placeholder="Enter your Username"
              class="input input-bordered text-sm w-[20rem] bg-white"
            />
            <div v-show="!validateUsername" class="label">
              <span class="label-text-alt text-red-500"
                >Username must be between 1 and 50 characters</span
              >
            </div>
          </label>
          <label class="form-control w-full max-w-xs">
            <div class="label">
              <span class="label-text font-semibold text-slate-500"
                >Password</span
              >
            </div>
            <input
              type="password"
              v-model="user.password"
              placeholder="Enter your password"
              class="input input-bordered text-sm w-[20rem] bg-white"
            />
            <div v-show="!validatePassword" class="label">
              <span class="label-text-alt text-red-500"
                >Password must be between 1 and 14 characters</span
              >
            </div>
          </label>
          <button
            class="w-[20rem] btn btn-primary text-white mt-6"
            :disabled="!isFormValid"
            @click="submitForm"
          >
            Login
          </button>
          <div v-show="showError" class="my-2">
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
              <span>Error! <br/>InValid Username and Password</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
