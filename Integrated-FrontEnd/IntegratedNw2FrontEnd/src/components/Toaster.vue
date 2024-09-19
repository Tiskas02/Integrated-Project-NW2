<script setup lang="ts">
import { useToasterStore } from "../stores/notificationStores"
const toastStore = useToasterStore()
const toastClassMap = {
  warning: "warning",
  error: "error",
  success: "success",
}
</script>

<template>
  <Teleport to="body">
    <Transition name="toast">
      <div v-if="toastStore.toasts.length" class="toaster__wrapper">
        <TransitionGroup name="toast" tag="ul">
          <li
            v-for="toast in toastStore.toasts"
            :class="['toaster__inner', toastClassMap[toast.status]]"
            :key="toast.text"
          >
            <span class="toaster__inner-text itbkk-message">
              {{ toast.text }}
            </span>
          </li>
        </TransitionGroup>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.toast-enter-from,
.toast-leave-to {
  transform: translateX(100%);
  opacity: 0;
}

.toast-enter-active,
.toast-leave-active {
  transition: 0.25s ease all;
}

.toaster__wrapper {
  position: fixed;
  bottom: 3%;
  right: 5%;

  z-index: 100;

  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.toaster__inner {
  --color: black;
  display: flex;
  align-items: center;
  gap: 1rem;

  border-radius: 1rem;

  border: 1px solid transparent;

  background-color: white;

  padding: 2.2rem 1.6rem;

  border-color: var(--color);
  color: var(--color);
}

.success {
  --color: green;
}

.warning {
  --color: orange;
}

.error {
  --color: red;
}

toaster__inner-text {
  font-size: 1.6rem;
  font-weight: 600;
}
</style>
