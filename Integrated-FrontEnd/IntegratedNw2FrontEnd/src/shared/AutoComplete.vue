<script setup>
import { ref, defineProps, defineEmits, watch } from "vue";

const inputValue = ref("");
const emailDomains = ["@ad.sit.kmutt.ac.th"]; // Mock email domains
const suggestions = ref([]);
const showSuggestions = ref(false);
const highlightedIndex = ref(-1);
const props = defineProps({
    modelValue: String
});
const emit = defineEmits(["update:modelValue"]);

watch(() => props.modelValue, (newValue) => {
  inputValue.value = newValue;
});

function onInputChange() {
  const query = inputValue.value.trim();
  emit("update:modelValue", inputValue.value); // Emit the updated value to the parent
  // Generate suggestions based on the mock email array
  if (query) {
    suggestions.value = emailDomains.map(
      (domain) => `${query.split("@")[0]}${domain}`
    );
    showSuggestions.value = suggestions.value.length > 0;
  } else {
    resetSuggestions();
  }
}

function highlightNext() {
  if (suggestions.value.length > 0) {
    highlightedIndex.value =
      (highlightedIndex.value + 1) % suggestions.value.length;
  }
}

function highlightPrevious() {
  if (suggestions.value.length > 0) {
    highlightedIndex.value =
      (highlightedIndex.value - 1 + suggestions.value.length) %
      suggestions.value.length;
  }
}

function selectSuggestion(index = highlightedIndex.value) {
  if (index >= 0 && index < suggestions.value.length) {
    inputValue.value = suggestions.value[index];
    emit("update:modelValue", inputValue.value); // Emit the updated value to the parent
    resetSuggestions();
  }
}

function resetSuggestions() {
  suggestions.value = [];
  showSuggestions.value = false;
  highlightedIndex.value = -1;
}
</script>

<template>
  <div class="relative w-full max-w-md">
    <input
      type="text"
      v-model="inputValue"
      @input="onInputChange"
      @keydown.down.prevent="highlightNext"
      @keydown.up.prevent="highlightPrevious"
      @keydown.enter.prevent="selectSuggestion"
      class="w-full p-2 border rounded-lg outline-none bg-[#b3d1e8] text-gray-800 focus:ring focus:ring-blue-400 placeholder:text-white "
      placeholder="Type your email to add collab..."
    />
    <ul
      v-if="suggestions.length && showSuggestions"
      class="absolute z-10 w-full bg-white border rounded-lg shadow-md"
    >
      <li
        v-for="(suggestion, index) in suggestions"
        :key="index"
        :class="{
          'bg-blue-500 text-white': index === highlightedIndex,
          'hover:bg-gray-200': index !== highlightedIndex,
        }"
        class="p-2 cursor-pointer"
        @mousedown="selectSuggestion(index)"
      >
        {{ suggestion }}
      </li>
    </ul>
  </div>
</template>
