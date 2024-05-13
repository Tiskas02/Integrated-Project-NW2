<script setup>
import TaskView from "../views/TaskView.vue";
import TaskAddEdit from "./TaskAddEdit.vue";
import TaskDelete from "@/views/TaskDelete.vue";
import { defineProps, defineEmits, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { getTaskById } from "@/libs/api/task/fetchUtilTask";
const emit = defineEmits(["close"]);
const props = defineProps({
  mode: String,
  task: Object,
});

const sentMode = ref(props.mode);
const sentTask = ref(props.task);

const sentclose = (value) => {
  // props.task = {};
  emit("close", value);
  sentTask.value = {};
  console.log(sentTask.value);
};
</script>

<template>
  <div>
    <div v-if="mode === 'view'">
      <TaskView @close="sentclose" :task="sentTask" />
    </div>
    <div v-else-if="mode === 'add'">
      <TaskAddEdit @close="sentclose" :mode="sentMode" />
    </div>
    <div v-else-if="mode === 'edit'">
      <TaskAddEdit @close="sentclose" :mode="sentMode" :task="sentTask" />
    </div>
    <div v-else>
      <TaskDelete @close="sentclose" />
    </div>
  </div>
</template>

<style scoped></style>
