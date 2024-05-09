<script setup>
import TaskView from '../views/TaskView.vue';
import TaskAddEdit from './TaskAddEdit.vue';
import { defineProps, defineEmits, ref,watch } from 'vue';
const emit = defineEmits(['close']);
const props = defineProps({
  mode: String
});
const sentclose = (value) => {
  emit('close', value);
};
const sentMode = ref(props.mode);
</script>

<template>
  <div>
    <div v-if="mode === 'view'">
      <TaskView @close="sentclose" />
    </div>
    <div v-else-if="mode === 'add' || mode === 'edit'">
      <TaskAddEdit @close="sentclose" :mode="sentMode" />
    </div>
  </div>
</template>

<style scoped></style>
