<script setup>
import { storeToRefs } from 'pinia';
import { useStoreTasks } from '../stores/taskStores.js';
import { ref } from 'vue';
import TaskModal from '../components/TaskModal.vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const tasksStore = useStoreTasks();
const { isLoading, tasks } = storeToRefs(tasksStore);
const showDetail = ref(false);
const storeMode = ref('');
const setDetail = (value, id, mode) => {
  showDetail.value = value;

  storeMode.value = mode;
  if (id !== null) {
    router.push({ name: 'taskDetail', params: { id: id } });
  } else {
    console.log(storeMode.value);
  }
};
const setClose = (value) => {
  showDetail.value = value;
  router.push({ name: 'task' });
};
const getStatusStyle = (status) => {
  switch (status) {
    case 'No Status':
      return 'btn btn-active btn-primary';
    case 'To Do':
      return 'btn-info';
    case 'Doing':
      return 'btn-warning';
    case 'Done':
      return 'btn-success';
    default:
      return 'transparent';
  }
};
</script>

<template>
  <div>
    <div class="flex justify-between">
      <div class="my-2">
        <div class="ml-10 btn btn-outline btn-accent" @click="">
          <router-link :to="{ name: 'status' }">Manage Status</router-link>
        </div>
      </div>
      <div class="my-2">
        <!-- Add Task-->
        <div
          class="itbkk-button-add btn btn-outline btn-primary mr-10"
          @click="setDetail(true, null, 'add')"
        >
          <svg
            width="20"
            height="20"
            viewBox="0 0 20 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <g clip-path="url(#clip0_529_11)">
              <path
                d="M11 9H15V11H11V15H9V11H5V9H9V5H11V9ZM10 20C7.34784 20 4.8043 18.9464 2.92893 17.0711C1.05357 15.1957 0 12.6522 0 10C0 7.34784 1.05357 4.8043 2.92893 2.92893C4.8043 1.05357 7.34784 0 10 0C12.6522 0 15.1957 1.05357 17.0711 2.92893C18.9464 4.8043 20 7.34784 20 10C20 12.6522 18.9464 15.1957 17.0711 17.0711C15.1957 18.9464 12.6522 20 10 20ZM10 18C12.1217 18 14.1566 17.1571 15.6569 15.6569C17.1571 14.1566 18 12.1217 18 10C18 7.87827 17.1571 5.84344 15.6569 4.34315C14.1566 2.84285 12.1217 2 10 2C7.87827 2 5.84344 2.84285 4.34315 4.34315C2.84285 5.84344 2 7.87827 2 10C2 12.1217 2.84285 14.1566 4.34315 15.6569C5.84344 17.1571 7.87827 18 10 18Z"
                fill="#7C5ED2"
              />
            </g>
            <defs>
              <clipPath id="clip0_529_11">
                <rect width="20" height="20" fill="white" />
              </clipPath>
            </defs>
          </svg>
          add task
        </div>
        <!-- Added ml-2 class for margin-left -->
      </div>
    </div>

    <div class="w-full flex justify-center">
      <div
        class="overflow-x-auto shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40"
      >
        <div class="min-w-full divide-y divide-gray-200">
          <div class="#4793AF bg-slate-600 flex sticky">
            <div
              class="w-[10%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            ></div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Title
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Assignees
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Status
            </div>
            <div
              class="w-[22%] px-6 py-3 text-left text-md font-bold text-white uppercase"
            >
              Action
            </div>
          </div>
          <div v-if="tasks.length <= 0" class="w-full border bg-white">
            <div class="w-full">
              <p>No task</p>
            </div>
          </div>
          <!-- Edit Task -->
          <div class="w-full h-[350px]">
            <div v-for="(task, index) in tasks" :key="task.taskId">
              <div class="bg-white divide-y divide-gray-200 overflow-auto">
                <div class="">
                  <div
                    class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50"
                  >
                    <div class="flex">
                      <div
                        class="w-[10%] px-6 py-4 whitespace-nowrap"
                        @click="setDetail(true, task.taskId, 'view')"
                      >
                        {{ index + 1 }}
                      </div>
                      <div
                        class="w-[22%] itbkk-title px-6 py-4 whitespace-nowrap overflow-x-auto"
                        @click="setDetail(true, task.taskId, 'view')"
                      >
                        {{ task.title }}
                      </div>
                      <div
                        class="w-[22%] itbkk-assignees px-6 py-4 whitespace-nowrap overflow-x-auto"
                        @click="setDetail(true, task.taskId, 'view')"
                      >
                        {{ task.assignees ? task.assignees : 'Unassigned' }}
                      </div>
                      <div
                        class="w-[22%] itbkk-status px-6 py-4 whitespace-nowrap flex justify-center overflow-x-auto"
                        @click="setDetail(true, task.taskId, 'view')"
                      >
                        <div
                          class="itbkk-button-action btn shadow text-white overflow-x-auto"
                          :class="getStatusStyle(task.status)"
                          @click="setDetail(true, task.taskId, 'view')"
                        >
                          {{ task?.status }}
                        </div>
                      </div>
                      <div
                        class="w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                      >
                        <div
                          class="btn btn-outline btn-warning"
                          @click="setDetail(true, task.taskId, 'edit')"
                        >
                          Edit
                        </div>
                        <div
                          class="itbkk-button-delete btn btn-outline btn-error"
                        >
                          Delete
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <teleport to="#body">
        <TaskModal v-if="showDetail" @close="setClose" :mode="storeMode" />
      </teleport>
      <!-- <teleport to="body" v-if="showDetail">
            <Modal
              @setDetail="setDetail"
              :tasks="dataById"
              :mode="storeMode"
              @saveTask="updateEdit"
            ></Modal>
          </teleport>
          <Teleport to="body" v-if="showDelete">
            <Delete
              @setDelete="setDelete"
              :tasks="dataById"
              :index="storeIndex"
              @statusCode="removeTask"
            ></Delete>
          </Teleport> -->
    </div>
  </div>
</template>

<style scoped></style>
