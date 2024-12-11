<script setup>
import BaseBtn from "../shared/BaseBtn.vue"
import Autocomplete from "../shared/AutoComplete.vue"
import { defineEmits, ref, computed } from "vue"


const emit = defineEmits(["close", "newCollab"])
//Can you receive routeId from others files?

const newCollab = ref({
  email: "",
  accessRight: "READ",
})
const isDropdownOpen = ref(false)
const accessRights = ["READ", "WRITE"]
const selectAccessRight = (nameAcess) => {
  newCollab.value.accessRight = nameAcess
}
const updateEmail = (value) => {
  newCollab.value.email = value 
}
const isEmailValid = computed(() => newCollab.value.email.includes('@ad.sit.kmutt.ac.th'));
const selectedAcess = ref(accessRights[0])
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}
const updateAcess = (accessName) => {
  selectedAcess.value = accessName
  selectAccessRight(accessName)
  isDropdownOpen.value = false 
}




</script>

<template>
  <div>
    <div>
      <div>
        <div
          class="itbkk-modal-new bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-50 left-50 pt-16 tablet:pt-20"
          style="translate: transform(-50%, -50%)"
        >
          <div class="tablet:w-[60%] laptop:w-[50%] m-[auto] max-h-screen">
            <div
              class="overflow-auto max-h-screen flex flex-col justify-between bg-[#81B2D6] p-7 border-gray-200 rounded-b-xl tablet:rounded-lg shadow-xl"
            >
              <div class="itbkk-modal-new">
                <div class="text-xl font-bold my-3 text-white">
                  Add Collaborator
                </div>
                <div class="border-b my-2"></div>
                <div class="flex flex-col tablet:flex-row">
                  <div class="flex flex-col tablet:w-64 laptop:w-[70%]">
                    <div class="text-sm font-semibold text-white my-3">
                      Collaborator email
                    </div>
                    <Autocomplete
                      :modelValue="newCollab.email"
                      @update:modelValue="updateEmail"
                    />
                  </div>
                  <div class="laptop:grow"></div>
                  <div class="flex flex-col">
                    <div class="text-sm font-semibold text-white my-3">
                      Select Access Right
                    </div>
                    <div class="flex tablet:justify-end">
                      <div class="inline-block text-left">
                        <!-- Dropdown button -->
                        <div>
                          <button
                            type="button"
                            @click="toggleDropdown"
                            class="inline-flex justify-center w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                            id="menu-button"
                            aria-expanded="true"
                            aria-haspopup="true"
                          >
                            {{ selectedAcess }}
                            <!-- Chevron Icon -->
                            <svg
                              class="-mr-1 ml-2 h-5 w-5"
                              xmlns="http://www.w3.org/2000/svg"
                              viewBox="0 0 20 20"
                              fill="currentColor"
                              aria-hidden="true"
                            >
                              <path
                                fill-rule="evenodd"
                                d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                clip-rule="evenodd"
                              />
                            </svg>
                          </button>
                        </div>

                        <!-- Dropdown menu -->
                        <div
                          v-if="isDropdownOpen"
                          class="absolute mt-2 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
                          role="menu"
                          aria-orientation="vertical"
                          aria-labelledby="menu-button"
                          tabindex="-1"
                        >
                          <div class="py-1" role="none">
                            <a
                              v-for="access in accessRights"
                              :key="access"
                              href="#"
                              @click.prevent="updateAcess(access)"
                              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                              role="menuitem"
                              tabindex="-1"
                              :id="`menu-item-${access.id}`"
                            >
                              {{ access }}
                            </a>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="flex flex-row w-full justify-end my-4">
                <div class="flex">
                  <BaseBtn
                    class="itbkk-button-confirm itbkk-button-ok mx-4 mt-3"
                    :disabled="!isEmailValid"
                  >
                    <template #default>
                      <button @click="
                          () => {
                            emit('close', false)
                            emit('newCollab', newCollab)
                          }
                        ":disabled="!isEmailValid">Save</button>
                    </template>
                  </BaseBtn>
                  <BaseBtn class="itbkk-button-cancel">
                    <template #cancel>
                      <button
                        @click="
                          () => {
                            emit('close', false)
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
  </div>
</template>

<style scoped></style>
