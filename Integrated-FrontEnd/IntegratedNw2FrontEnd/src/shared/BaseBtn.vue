<script setup>

</script>
 
<template>
<div>
    <botton v-if="$slots.default" class="btn btn-primary text-white shadow-md">
        <slot name="default"> Save </slot>
    </botton>
    <botton v-if="$slots.cancel" class="btn btn-error text-white shadow-md">
        <slot name="cancel"> Cancle </slot>
    </botton>
    <botton v-if="$slots.delete" class="btn btn-error text-white shadow-md">
        <slot name="delete"> Delete </slot>
    </botton>
</div>
</template>
 
<style scoped>

</style>