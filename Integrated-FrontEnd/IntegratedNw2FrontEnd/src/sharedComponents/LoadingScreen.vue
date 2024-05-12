<script setup>

</script>

<template>
    <div class="bg-grey-600 backdrop-brightness-50 w-[100%] h-[100vh] flex justify-center items-center">
        <div class="loader"></div>
    </div>
</template>

<style scoped>
.loader {
    border: 10px solid #f1f1f1;
    border-top: 10px solid #002be9;
    border-radius: 90%;
    width: 200px;
    height: 200px;
    animation: spin 1.0s ease-in-out infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}
</style>