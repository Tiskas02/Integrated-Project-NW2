<script setup>
import { defineProps} from "vue";
defineProps(['disabled']);
</script>

<template>
  <div>
    <botton
      v-if="$slots.default"
      :class="[
        'rounded-lg px-5 py-4 text-white shadow-md cursor-pointer',
        disabled
          ? 'bg-gradient-to-r from-blue-400 to-blue-300 cursor-not-allowed'
          : 'bg-gradient-to-r from-blue-700 to-blue-400 hover:bg-gradient-to-r hover:from-blue-900 hover:to-blue-700'
      ]"
      :disabled="disabled"
      >
      <slot name="default"> Save </slot>
    </botton>
    <botton v-if="$slots.cancel" class="btn bg-gradient-to-r from-red-700 to-red-400 text-white shadow-md border-none">
      <slot name="cancel"> Cancle </slot>
    </botton>
    <botton v-if="$slots.save" class="btn btn-accent text-white shadow-md">
      <slot name="save"> Save </slot>
    </botton>
  </div>
</template>

<style scoped></style>
