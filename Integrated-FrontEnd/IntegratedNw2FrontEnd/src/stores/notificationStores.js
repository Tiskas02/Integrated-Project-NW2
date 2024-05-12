import { defineStore } from "pinia";

const defaultTimeout = 2000;

const createToast = (text, status) => ({
  text,
  status,
  id: Math.random() * 1000,
});

export default defineStore("toaster-store", {
  state: () => ({
    toasts: [],
  }),
  actions: {
    updateState(payload, status) {
      const { text, timeout } = payload;

      const toast = createToast(text, status);

      this.toasts.push(toast);

      setTimeout(() => {
        this.toasts = this.toasts.filter((t) => t.id !== toast.id);
      }, timeout ?? defaultTimeout);
    },
    success(payload) {
      this.updateState(payload, "success");
    },

    warning(payload) {
      this.updateState(payload, "warning");
    },

    error(payload) {
      this.updateState(payload, "error");
    },
  },
});