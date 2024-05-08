<script setup>
import {
  getTaskById,
  getTaskData,
  addTask,
  deleteItemById,
  updateTask,
} from "../libs/fetchUtil.js"
import { onMounted, ref, computed } from "vue"
import taskManagement from "../libs/TaskManagement.js"
import { useRoute, useRouter } from "vue-router"
import router from "../router/router.js"
import Modal from "../components/Modal.vue"
import Delete from "../views/Delete.vue"


const showDetail = ref(false)
const showDelete = ref(false)
const route = useRoute()
const dataById = ref()
const storeMode = ref(null)
let historyStack = []
const myTasks = ref(taskManagement)
const removeId = ref()
const storeIndex = ref(0)

const updateEdit = async (newEdit) => {
  if (newEdit.taskId === undefined) {
    let convertedStatus = ""
    //backend
    const addedTask = await addTask(import.meta.env.VITE_BASE_URL, {
      assignees: newEdit.assignees,
      status: newEdit.status,
      title: newEdit.title,
      description: newEdit.description,
    })
    switch (addedTask.status) {
      case "NO_STATUS":
        convertedStatus = "No Status"
        break
      case "TO_DO":
        convertedStatus = "To Do"
        break
      case "DOING":
        convertedStatus = "Doing"
        break
      case "DONE":
        convertedStatus = "Done"
        break
      default:
        convertedStatus = "No Status" // Handle any other status
        break
    }
    //frontend
    myTasks.value.addTask({
      taskId: addedTask.taskId,
      title: addedTask.title,
      assignees: addedTask.assignees,
      status: convertedStatus,
      description: addedTask.description,
      createdOn: addedTask.createdOn,
      updatedOn: addedTask.updatedOn,
    })
  } else {
    const updatedTask = await updateTask(
      import.meta.env.VITE_BASE_URL,
      newEdit.taskId,
      newEdit
    )
    let convertedStatus = ""
    switch (updatedTask.status) {
      case "NO_STATUS":
        convertedStatus = "No Status"
        break
      case "TO_DO":
        convertedStatus = "To Do"
        break
      case "DOING":
        convertedStatus = "Doing"
        break
      case "DONE":
        convertedStatus = "Done"
        break
      default:
        convertedStatus = "No Status" // Handle any other status
        break
    }
    console.log(convertedStatus)
    myTasks.value.updateTask({
      taskId: updatedTask.taskId,
      assignees: updatedTask.assignees,
      status: convertedStatus,
      title: updatedTask.title,
      description: updatedTask.description,
      createdOn: updatedTask.createdOn,
      updatedOn: updatedTask.updatedOn,
    })
  }
}

const setIndex = (index) => {
  storeIndex.value = index
}

const removeTask = async (removeId) => {
  console.log(removeId);
  const removeTask = await deleteItemById(
    import.meta.env.VITE_BASE_URL,
    removeId
  )
  if (removeTask === 200) {
    myTasks.value.removeTask(removeId)
  } else {
    alert("Error deleting task ")
  }
}

onMounted(async () => {
  myTasks.value.setTasks(await getTaskData(import.meta.env.VITE_BASE_URL))
})

const setDelete = (del) => {
  showDelete.value = del
}
const setMode = (mode) => {
  storeMode.value = mode
}
const setDetail = (set) => {
  
  showDetail.value = set
  console.log(showDetail.value);
}
function routeToadd() {
  router.push({ name: "addTask" })
}

// Fetch task by id
async function fetchById(id) {
  if (!id) {
    throw new Error('Missing required param "id"')
  }
  dataById.value = await getTaskById(import.meta.env.VITE_BASE_URL, id)
  console.log(dataById.value);
  if (showDetail.value === true) {
    if (storeMode.value === "edit") {
      router.push({ name: "taskDetail", params: { id: id } }).then(() => {
        router.push({ name: "editTask", params: { id: id } })
      })
    } else {
      router.push({ name: "taskDetail", params: { id: id } })
    }

    if (dataById.value.status == "404") {
      alert("The requested task does not exist")
      router.replace({ name: "task" })
      return
    }
    console.log(dataById.value);
    setDetail(true)
  }
}


// Add Task

window.onpopstate = function () {
  const previousState = historyStack.pop()
  if (previousState === true) {
    setDetail(true) // Forward navigation
  } else {
    setDetail(false) // Backward navigation or initial load
  }
}

function navigateToDetail(showDetail) {
  historyStack.push(showDetail)
}

if (route.params.id) {
  fetchById(route.params.id)
}

const task = ref({
  status: "No Status",
  todo: "To Do",
  doing: "Doing",
  done: "Done",
})

const getStatusColor = (status) => {
  switch (status) {
    case "No Status":
      return "SlateGray"
    case "To Do":
      return "Tomato"
    case "Doing":
      return "Orange"
    case "Done":
      return "LimeGreen"
    default:
      return "transparent"
  }
}
</script>

<template>
  <div class="w-full h-screen flex justify-center items-center">
    <div class="w-[95%] h-[90%]">
      <div class="w-full bg-slate-100 h-full rounded-2xl shadow-inner">
        <div class="w-[100%] py-3 flex-row flex items-start">
          <div>
            <div class="font-bold text-3xl text-blue-220 my-3 mx-6">
              IT-Bangmod Kradan Kanban
            </div>
          </div>
          <div class="grow"></div>
          <div class="mr-20 mt-2 flex-row items-center">
            <!-- Add Task-->
            <div
              class="itbkk-button-add btn btn-outline btn-primary"
              @click=";[setMode('add'), (showDetail = true), routeToadd()]"
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
        
        <!-- No Task -->
        <div class="w-full flex justify-center">
          <div
            class="overflow-x-auto shadow-2xl rounded-md w-[95%] h-[95%] shadow-blue-500/40"
          >
            <div class="min-w-full divide-y divide-gray-200">
              <div class="#4793AF bg-slate-600 flex">
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
              <div
                v-if="myTasks.getTask().length === 0"
                class="w-full border bg-white"
              >
                <div class="w-full">
                  <div></div>
                  <div></div>
                  <div class="m-[auto]">NO TASK</div>
                  <div></div>
                </div>
              </div>
              <!-- Edit Task -->
              <div
                v-else
                class="bg-white divide-y divide-gray-200 overflow-auto"
              >
                <div class="w-full max-h-[550px]">
                  <div
                    v-for="(task, index) in myTasks.getTask()"
                    :key="task.taskId"
                    class="itbkk-item cursor-pointer hover:text-violet-600 hover:duration-200 odd:bg-white even:bg-slate-50"
                  >
                    <div class="flex">
                      <div
                        class="w-[10%] px-6 py-4 whitespace-nowrap"
                        @click="
                          ;[
                            (showDetail = true),
                            fetchById(task.taskId),
                            setMode('view'),
                          ]
                        "
                      >
                        {{ index + 1 }}
                      </div>
                      <div
                        class="w-[22%] itbkk-title px-6 py-4 whitespace-nowrap overflow-x-auto"
                        @click="
                          ;[
                            (showDetail = true),
                            fetchById(task.taskId),
                            setMode('view'),
                          ]
                        "
                      >
                        {{ task.title }}
                      </div>
                      <div
                        class="w-[22%] itbkk-assignees px-6 py-4 whitespace-nowrap overflow-x-auto"
                        :class="{ 'text-red-300': !task.assignees }"
                        :style="{
                          fontStyle: task.assignees ? 'normal' : 'italic',
                        }"
                        @click="
                          ;[
                            (showDetail = true),
                            fetchById(task.taskId),
                            setMode('view'),
                          ]
                        "
                      >
                        {{ task.assignees ? task.assignees : "Unassigned" }}
                      </div>
                      <div
                        class="w-[22%] itbkk-status px-6 py-4 whitespace-nowrap flex justify-center overflow-x-auto"
                      >
                        <div
                          class="itbkk-button-action btn shadow text-white overflow-x-auto"
                          @click="
                            ;[
                              (showDetail = true),
                              fetchById(task.taskId),
                              setMode('view'),
                            ]
                          "
                          :style="{
                            backgroundColor: getStatusColor(task.status),
                          }"
                        >
                          {{ task?.status }}
                        </div>
                      </div>
                      <div
                        class="w-[22%] px-6 py-4 whitespace-nowrap flex gap-4"
                      >
                        <div
                          class="btn btn-outline btn-warning"
                          @click="
                            ;[
                              setMode('edit'),
                              (showDetail = true),
                              fetchById(task.taskId),
                            ]
                          "
                        >
                          {{ task?.id }}
                          Edit
                        </div>
                        <div
                          class="itbkk-button-delete btn btn-outline btn-error"
                          @click="[(showDelete = true), fetchById(task.taskId),setIndex(index+1)]"
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
          <teleport to="body" v-if="showDetail">
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
          </Teleport>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
