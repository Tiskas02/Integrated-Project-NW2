<script setup>

</script>
 
<template>
<div>
    <botton v-if="$slots.default" class="btn btn-active btn-accent text-white">
        <slot name="default"> Save </slot>
    </botton>
    <botton v-if="$slots.cancel" class="btn btn-error text-white">
        <slot name="cancel"> Cancle </slot>
    </botton>
</div>
</template>
 
<style scoped>

</style>