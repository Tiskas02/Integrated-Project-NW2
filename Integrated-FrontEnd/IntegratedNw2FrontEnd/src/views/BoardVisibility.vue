<script setup>
import { defineProps, defineEmits, ref } from "vue"

const emit = defineEmits(["close", "newBoard"])
const props = defineProps({
  board: Object,
})

const newBoard = ref({ ...props.board })

console.log(props.board)
console.log(`call visibilities`)

const toggleVisibility = () => {
  // Toggle the visibilities field
  if (newBoard.value.visibilities === "PUBLIC") {
    newBoard.value.visibilities = "PRIVATE"
  } else {
    newBoard.value.visibilities = "PUBLIC"
  }
}
</script>

<template>
  <div>
    <div>
      <div
        class="bg-grey-500 backdrop-brightness-50 w-screen h-screen fixed top-0 left-0 pt-[100px]"
      >
        <div class="w-[40%] m-[auto] max-h-[80%]">
          <div
            class="flex flex-col justify-between bg-white p-7 border-gray-200 rounded-lg shadow-xl"
          >
            <div class="itbkk-modal-alert text-xl font-semibold text-red-400">
              Board visibility changed!
            </div>
            <div class="border-b my-3"></div>
            <div
              v-if="board.visibilities === 'public'"
              class="itbkk-message break-all"
            >
              "Do you want to change board visibility to public?"
            </div>
            <div v-else class="itbkk-message break-all">
              <div>"Do you want to change board visibility to private?"</div>
            </div>
            <div class="flex justify-end my-4 gap-2">
              <button
                class="bg-red-500 text-white px-4 py-2 rounded-lg"
                @click="
                  () => {
                    emit('close', false)
                  }
                "
              >
                Cancel
              </button>
              <button
                class="bg-green-500 text-white px-4 py-2 rounded-lg"
                @click="
                  () => {
                    toggleVisibility()
                    emit('close', false)
                    emit('newBoard', newBoard)
                  }
                "
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
