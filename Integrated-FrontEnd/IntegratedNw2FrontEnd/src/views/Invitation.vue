<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { useStoreCollab } from "@/stores/collabStore";
import { useToasterStore } from "@/stores/notificationStores";
import { storeToRefs } from "pinia";
import NavBar from "@/shared/NavBar.vue";
const toasterStore = useToasterStore();
const route = useRoute();
const router = useRouter();
const collabStore = useStoreCollab();
const { collabs } = storeToRefs(collabStore);
const routeId = ref(route.params.id);
const storeModeNavBar = ref("");
const collabInvitation = ref([]);
const parseJwt = (token) => {
  const base64Url = token.split(".")[1];
  const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );
  return JSON.parse(jsonPayload);
};
const receiveToken = localStorage.getItem("token");
const token = parseJwt(receiveToken);
onMounted(async () => {
  storeModeNavBar.value = "collab";
  const collab = await collabStore.fetchCollabsByBoardId(routeId.value);
  console.log(collab);
  collab.forEach((item) => {
    if (item.oid === token.oid) {
      collabInvitation.value.push(item);
    }
  });

  // Result
  console.log(collabInvitation.value);
  console.log(token);
});
const acceptInvitation = async () => {
  // Navigate to the board home after accepting
  const res = await collabStore.sendInvitation(
    { status: "ACCEPTED" },
    routeId.value
  );
  if (res.status === 200) {
    toasterStore.success({ text: "Accepted Collab Board successfully!" });
    router.push({ name: "Task", params: { id: routeId.value } });
  } else {
    toasterStore.error({ text: "Error Accepted Collab Board" });
  }
};

const declineInvitation = () => {
  // Navigate back to the login page or wherever appropriate
  router.push({ name: "board" });
};
</script>

<!-- src/views/Invitation.vue -->
<template>
  <NavBar :mode="storeModeNavBar" class="sticky top-0 z-50" />
  <div
    class="w-full h-lvh"
    style="
      background-image: url('https://res.cloudinary.com/dyhavbbzf/image/upload/v1733736424/jcw7sjnihbpmwljizi7p.png');
      background-size: cover;
      background-position: center;
    "
  >
    <div class="w-full h-full flex flex-col justify-center tablet:items-center">
      <div v-for="(collab, index) in collabInvitation" :key="collabs.boardId">
        <div>{{ collab }}</div>
      </div>
      <div class="w-fit h-48 p-8 glass rounded-b-lg tablet:rounded-xl">
        <h1 class="text-2xl font-bold">Invitation</h1>
        <p>
          You have received an invitation. Would you like to accept or decline?
        </p>
        <div>
          <div class="bg-slate-800 flex rounded-md">
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              BoardName
            </div>
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              
            </div>
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              index
            </div>
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              index
            </div>
          </div>
        </div>
        <div class="buttons my-4">
          <button
            @click="acceptInvitation"
            class="btn bg-gradient-to-r from-blue-700 to-blue-400 border-0 text-white mx-2"
          >
            Accept
          </button>
          <button
            @click="declineInvitation"
            class="btn bg-gradient-to-r from-red-700 to-red-400 border-0 text-white"
          >
            Decline
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
