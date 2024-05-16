<script setup>
import StatusAddEdit from './StatusAddEdit.vue';
import StatusDelete from '@/views/StatusDelete.vue';
import { defineProps, defineEmits, ref, watch } from 'vue';
const emit = defineEmits(['close', 'newStatus', 'deleteId', 'sentTranferId']);
const props = defineProps({
  status: {
    type: Object,
    default: {
      id: undefined,
      name: '',
      description: ''
    }
  },
  statusMode: String
});
const sentclose = (value) => {
  emit('close', value);
};
const sentNewStatus = (value) => {
  emit('newStatus', value);
};
const sentDelteId = (value) => {
  console.log(value);
  emit('deleteId', value);
};
const sentTranferId = (value) => {
  console.log(value);
  emit('sentTranferId', value);
};
const sentStatusMode = ref(props.statusMode);
const sentStatus = ref(props.status);
</script>
 
<template>
<div>
    <div v-if="statusMode === 'add' || statusMode === 'edit'">
      <StatusAddEdit @close="sentclose" :mode="sentStatusMode" :status="sentStatus" @newStatus="sentNewStatus"/>
    </div>
    <div v-if="statusMode === 'delete'">
        <StatusDelete @close="sentclose" :status="sentStatus" @sentDelete="sentDelteId" @sentTranfer="sentTranferId"/>
    </div>
</div>
</template>
 
<style scoped>

</style>