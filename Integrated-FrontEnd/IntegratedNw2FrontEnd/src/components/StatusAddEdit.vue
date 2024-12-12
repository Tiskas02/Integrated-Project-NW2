<script setup>
import { defineProps, defineEmits, ref, watch, computed } from "vue";
import BaseBtn from "@/shared/BaseBtn.vue";
const emit = defineEmits(["close", "newStatus"]);
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
});

const storeData = ref({
  id: undefined,
  name: "",
  description: "",
});

watch(
  () => props.status,
  () => {
    if (props.mode === "edit") {
      storeData.value = { ...props.status };
    }
  },
  { deep: true, immediate: true }
);

const nameCharCount = computed(() =>
  storeData.value.name ? storeData.value.name.length : 0
);

const descriptionCharCount = computed(() =>
  storeData.value.description ? storeData.value.description.length : 0
);
</script>

<template>
  <div>
    <div>
      <div
        class="itbkk-modal-status bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-16 tablet:pt-20"
        style="translate: transform(-50%, -50%)"
      >
        <div class="tablet:w-[60%] m-[auto] max-h-screen">
          <div
            class="overflow-auto max-h-screen flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-b-xl tablet:rounded-xl shadow-xl"
          >
            <div>
              <div class="text-xl font-bold my-3 text-white">
                {{ mode === "add" ? "Add Status" : "Edit Status" }}
              </div>
              <div class="border-b border-white my-2"></div>
              <div class="text-lg z-0 mt-3 text-white">Name</div>
              <div>
                <textarea
                  class="itbkk-title itbkk-status-name w-full h-[90%] px-4 py-2 my-1 bg-[#b3d1e8] text-gray-800 border border-white rounded-md focus:outline-none focus:border-blue-500 placeholder:text-white"
                  placeholder="Enter your title here..."
                  required
                  maxlength="50"
                  v-model="storeData.name"
                  >{{ status?.name }}</textarea
                >
              </div>
              <div class="flex justify-end text-xs text-white">
                {{ nameCharCount }}/50
              </div>
            </div>
            <div class="mt-3">
              <div class="text-lg z-0 text-white">Description</div>
              <div>
                <div>
                  <textarea
                    class="itbkk-description w-full h-[90%] px-4 py-2 my-1 bg-[#b3d1e8] text-gray-800 border border-white rounded-md focus:outline-none focus:border-blue-500 placeholder:text-white"
                    placeholder="Enter your description here..."
                    v-model="storeData.description"
                    maxlength="200"
                    >{{ status?.description }}</textarea
                  >
                </div>
              </div>
              <div class="flex justify-end text-xs text-white">
                {{ descriptionCharCount }}/200
              </div>
            </div>
            <div class="flex flex-row w-full justify-end my-4">
              <div class="mr-2">
                <BaseBtn
                  :disabled="
                    storeData.name?.trim() === '' ||
                    (storeData.name === status?.name &&
                      storeData.description === status?.description &&
                      storeData.id === status?.id)
                  "
                  class="itbkk-button-confirm itbkk-button-ok mx-4 mt-3"
                >
                  <template #default>
                    <button
                      @click="
                        () => {
                          $router.go(-1);
                          emit('close', false);
                          emit('newStatus', storeData);
                        }
                      "
                      :disabled="
                        storeData.name?.trim() === '' ||
                        (storeData.name === status?.name &&
                          storeData.description === status?.description &&
                          storeData.id === status?.id)
                      "
                      :class="{
                        'cursor-not-allowed':
                          storeData.name?.trim() === '' ||
                          (storeData.name === status?.name &&
                            storeData.description === status?.description &&
                            storeData.id === status?.id),
                      }"
                    >
                      Save
                    </button>
                  </template>
                </BaseBtn>
              </div>
              <div>
                <BaseBtn class="itbkk-button-cancel">
                  <template #cancel>
                    <button
                      @click="
                        () => {
                          $router.go(-1);
                          emit('close', false);
                        }
                      "
                    >
                      Cancel
                    </button>
                  </template>
                </BaseBtn>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
