import { createRouter, createWebHistory } from "vue-router";
import Task from "../views/Task.vue";
import NotFound from "../views/NotFound.vue";
import Modal from "@/components/Modal.vue";
import AddEditModal from "@/components/AddEditModal.vue";
import TaskHome from "@/views/TaskHome.vue";
import TaskModal from "@/components/TaskModal.vue";
import StatusTable from "@/components/StatusTable.vue";
import TaskAddEdit from "@/components/TaskAddEdit.vue";
import StatusAddEdit from "@/components/StatusAddEdit.vue";
import StatusDelete from "@/views/StatusDelete.vue";
const history = createWebHistory();
const routes = [
  {
    path: "/",
    redirect: "/task", // Redirect root path to /task
  },
  {
    path: "/task",
    name: "task",
    component: TaskHome,
    children: [
      {
        path: ":id",
        name: "taskDetail",
        component: TaskModal,
      },
      {
        path: "add",
        name: "addTask",
        component: TaskAddEdit,
      },
      {
        path: ":id/edit",
        name: "editTask",
        component: TaskAddEdit,
      },
    ],
  },
  // {
  //   path: '/task/add',
  //   name: 'addTask',
  //   component: TaskAddEdit
  // },
  {
    path: "/:notfoundpath(.*)",
    name: "NotFound",
    component: NotFound,
  },
  {
    path: "/status",
    name: "status",
    component: TaskHome,
    children: [
      {
        path: "add",
        name: "addStatus",
        component: StatusAddEdit,
      },
      {
        path: ":id/edit",
        name: "editStatus",
        component: StatusAddEdit,
      },
    ],
  },
];

const router = createRouter({
  history,
  routes,
  linkActiveClass: "text-[#2ff6da]",
  linkExactActiveClass: "hover:text-[#2ff6da] hover:text-[#2ff6da] p-2",
});

export default router;
