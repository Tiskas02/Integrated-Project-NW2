<script setup>
import { ref,onMounted } from "vue";
import { useStoreBoard } from "@/stores/boardStore";
import LoadingScreen from "@/shared/LoadingScreen.vue";
const dataLoaded = ref(false);
const boardStore = useStoreBoard();
const storeBoards = ref([]);

onMounted(async () => {
  const data = await boardStore.fetchBoards();
  storeBoards.value = data;
  if (storeBoards.value.length > 0) {
    dataLoaded.value = true;
  } else {
    dataLoaded.value = false;
  }
});
</script>
 
<template>
<div>
    <LoadingScreen v-if="!dataLoaded" />
</div>
</template>
 
<style scoped>

</style>