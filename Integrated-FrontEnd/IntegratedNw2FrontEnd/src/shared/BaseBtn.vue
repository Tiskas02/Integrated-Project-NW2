<script setup>

</script>
 
<template>
<div>
    <botton v-if="$slots.default" class="rounded-lg px-5 py-4 bg-gradient-to-r from-blue-700 to-blue-400 text-white shadow-md hover:bg-gradient-to-r hover:from-blue-900 hover:to-blue-700 cursor-pointer">
        <slot name="default"> Save </slot>
    </botton>
    <botton v-if="$slots.cancel" class="btn btn-error text-white shadow-md">
        <slot name="cancel"> Cancle </slot>
    </botton>
    <botton v-if="$slots.save" class="btn btn-accent text-white shadow-md">
        <slot name="save"> Save </slot>
    </botton>
</div>
</template>
 
<style scoped>

</style>