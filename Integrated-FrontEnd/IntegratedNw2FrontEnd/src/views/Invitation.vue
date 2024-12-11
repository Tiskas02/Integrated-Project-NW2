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
  collab.forEach((item) => {
    if (item.oid === token.oid) {
      collabInvitation.value.push(item);
    }
  });
  if (token.oid === collabInvitation.value[0]?.oid) {
    console.log("You are the owner of this board");
  } else {
    toasterStore.error({
      text: "You are not the owner of this board 🫵",
      setTimeout: 5000,
    });
    router.push({ name: "board" });
  }
  if (collabInvitation.value[0].status === "ACCEPTED") {
    toasterStore.error({
      text: "You are accepted this borad already",
      setTimeout: 5000,
    });
    router.push({ name: "board" });
  }
});
const acceptInvitation = async () => {
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

const declineInvitation = async () => {
  const res = await collabStore.deleteCollab(token.oid, routeId.value);
  if (res.status === 200) {
    toasterStore.success({ text: "Declined Collab Board successfully!" });
  } else {
    toasterStore.error({ text: "Error Declined Collab Board" });
  }
  router.push({ name: "board" });
};
</script>
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
      <div class="w-fit max-h-96 p-8 glass rounded-b-lg tablet:rounded-xl">
        <h1 class="text-2xl font-bold">Invitation</h1>
        <p>
          You have received an invitation. Would you like to accept or decline?
        </p>
        <div>
          <div class="bg-slate-800 flex rounded-md my-2">
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              BoardName
            </div>
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              Owner
            </div>
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              accessRight
            </div>
            <div class="w-[10%] m-auto text-start text-md font-bold text-white">
              status
            </div>
          </div>
          <div
            v-for="(collab, index) in collabInvitation"
            :key="collabs.boardId"
          >
            <div class="bg-slate-100 flex rounded-md">
              <div class="w-[10%] m-auto text-center text-md font-bold">
                {{ collab.boardName }}
              </div>
              <div class="w-[10%] m-auto text-center text-md font-bold">
                {{ collab.ownerName }}
              </div>
              <div class="w-[10%] m-auto text-center text-md font-bold">
                {{ collab.accessRight }}
              </div>
              <div
                class="w-[10%] m-auto text-center text-md font-bold text-yellow-500"
              >
                {{ collab.status }}
              </div>
            </div>
          </div>
        </div>
        <div class="buttons my-4 w-full flex justify-end">
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
