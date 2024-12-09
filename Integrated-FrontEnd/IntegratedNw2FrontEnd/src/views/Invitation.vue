<script setup>
import { useRoute, useRouter } from "vue-router"
import { ref } from "vue"
import { useStoreCollab } from "@/stores/collabStore"

const route = useRoute()
const router = useRouter()
const collabStore = useStoreCollab()
const routeId = ref(route.params.id)

const acceptInvitation = async () => {
  // Navigate to the board home after accepting
  console.log(routeId.value)
  const res = await collabStore.sendInvitation(
    { status : "ACCEPTED" },
    routeId.value
  )
  router.push({ name: "Task", params: { id: routeId.value } })
}

const declineInvitation = () => {
  // Navigate back to the login page or wherever appropriate
  router.push({ name: "board"})
}
</script>

<!-- src/views/Invitation.vue -->
<template>
  <div class="invitation-container">
    <h1 class="text-2xl font-bold">Invitation</h1>
    <p>You have received an invitation. Would you like to accept or decline?</p>

    <div class="buttons">
      <button @click="acceptInvitation" class="btn-accept">Accept</button>
      <button @click="declineInvitation" class="btn-decline">Decline</button>
    </div>
  </div>
</template>

<style scoped>
.invitation-container {
  text-align: center;
  margin-top: 50px;
}

.buttons {
  margin-top: 20px;
}

.btn-accept,
.btn-decline {
  margin: 10px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}

.btn-accept {
  background-color: green;
  color: white;
}

.btn-decline {
  background-color: red;
  color: white;
}
</style>
