<script setup>
import { defineProps, defineEmits, ref, watch, computed } from "vue"
const emit = defineEmits(["close", "newStatus"])
const props = defineProps({
  status: {
    type: Object,
    default: {
      id: undefined,
      name: "",
      description: "",
    },
  },
  mode: String,
})

const storeData = ref({
  id: undefined,
  name: "",
  description: "",
})

watch(
  () => props.status,
  () => {
    if (props.mode === "edit") {
      storeData.value = { ...props.status }
    }
  },
  { deep: true, immediate: true }
)

const nameCharCount = computed(() =>
  storeData.value.name ? storeData.value.name.length : 0
)

const descriptionCharCount = computed(() =>
  storeData.value.description ? storeData.value.description.length : 0
)
</script>

<template>
  <div>
    <div>
      <div
        class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-20"
        style="translate: transform(-50%, -50%)"
      >
        <div class="w-[60%] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
          >
            <div>
              <div class="text-xl font-bold my-3">
                {{ mode === "add" ? "Add Status" : "Edit Status" }}
              </div>
              <div class="border-b my-2"></div>
              <div class="text-lg z-0">Name</div>
              <div>
                <textarea
                  class="itbkk-title w-full h-[90%] px-4 py-2 my-1 bg-slate-100 shadow-inner text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
                  placeholder="Enter your title here..."
                  required
                  maxlength="50"
                  v-model="storeData.name"
                  >{{ status?.name }}</textarea
                >
              </div>
              <div class="flex justify-end text-xs">{{ nameCharCount }}/50</div>
            </div>
            <div class="mt-5">
              <div role="tablist" class="tabs tabs-bordered mb-3">
                <a role="tab" class="tab tab-active text-lg font-bold"
                  >Description</a
                >
                <a role="tab" class="tab"></a>
                <a role="tab" class="tab"></a>
              </div>
              <div>
                <div>
                  <textarea
                    class="itbkk-description w-full h-[90%] px-4 py-2 my-1 bg-slate-100 text-gray-800 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 shadow-inner"
                    placeholder="Enter your description here..."
                    v-model="storeData.description"
                    maxlength="200"
                    >{{ status?.description }}</textarea
                  >
                </div>
              </div>
              <div class="flex justify-end text-xs">
                {{ descriptionCharCount }}/200
              </div>
            </div>
            <div class="flex flex-row w-full justify-end my-4">
              <div class="mr-2">
                <button
                  class="itbkk-button-confirm disabled btn btn-info text-white"
                  @click="
                    () => {
                      $router.go(-1)
                      emit('close', false)
                      emit('newStatus', storeData)
                    }
                  "
                  :disabled="
                    storeData.name.trim() === '' ||
                    (storeData.name === status?.name &&
                      storeData.description === status?.description &&
                      storeData.id === status?.id)
                  "
                >
                  Save
                </button>
              </div>
              <div>
                <div
                  class="itbkk-button-cancel btn btn-error text-white"
                  @click="
                    () => {
                      $router.go(-1)
                      emit('close', false)
                    }
                  "
                >
                  Cancel
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
