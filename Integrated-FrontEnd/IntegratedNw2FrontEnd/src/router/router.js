import { createRouter, createWebHistory } from "vue-router";
import Task from "../views/Task.vue";
import NotFound from "../views/NotFound.vue";
import Modal from "@/components/Modal.vue";
import AddEditModal from "@/components/AddEditModal.vue";
import TaskHome from "@/components/TaskHome.vue";
import TaskModal from "@/components/TaskModal.vue";
import StatusTable from "@/components/StatusTable.vue";
const history = createWebHistory();
// const routes = [
//   {
//     path: "/",
//     redirect: "/task", // Redirect root path to /task
//   },
//   {
//     path: "/task",
//     name: "task",
//     component: Task,
//     children: [
//       {
//         path: ":id",
//         name: "taskDetail",
//         component: Modal,
//       },
//     ],
//   },
//   {
//     path: "/:notfoundpath(.*)",
//     name: "NotFound",
//     component: Task,
//     redirect: "/task",
//   },
//   {
//     path: "/task/add",
//     name: "addTask",
//     component: AddEditModal,
//   },
//   {
//     path: "/task/:id/edit",
//     name: "editTask",
//     component: AddEditModal,
//   },
// ];
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
    ],
  },
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
        name: "addTask",
        component: AddEditModal,
      },
      {
        path: ":id/edit",
        name: "editTask",
        component: AddEditModal,
      },
    ],
  }

];

const router = createRouter({
  history,
  routes,
  linkActiveClass: "text-[#2ff6da]",
  linkExactActiveClass: "hover:text-[#2ff6da] hover:text-[#2ff6da] p-2",
});

export default router;
