<script setup>
import StatusAddEdit from './StatusAddEdit.vue';
import StatusDelete from '@/views/StatusDelete.vue';
import { defineProps, defineEmits, ref, watch } from 'vue';
const emit = defineEmits(['close']);
const props = defineProps({
  statusMode: String
});
const sentclose = (value) => {
  emit('close', value);
};
const sentStatusMode = ref(props.statusMode);
</script>
 
<template>
<div>
    <div v-if="statusMode === 'add' || statusMode === 'edit'">
      <StatusAddEdit @close="sentclose" :mode="sentStatusMode" />
    </div>
    <div v-if="statusMode === 'delete'">
        <StatusDelete @close="sentclose" />
    </div>
</div>
</template>
 
<style scoped>

</style>