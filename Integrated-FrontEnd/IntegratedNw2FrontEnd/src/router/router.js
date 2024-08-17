import { createRouter, createWebHistory } from "vue-router";
import NotFound from "../views/NotFound.vue";
import TaskHome from "@/views/TaskHome.vue";
import TaskModal from "@/components/TaskModal.vue";
import StatusTable from "@/components/StatusTable.vue";
import TaskAddEdit from "@/components/TaskAddEdit.vue";
import StatusAddEdit from "@/components/StatusAddEdit.vue";
import Login from "@/components/Login.vue";
const history = createWebHistory(import.meta.env.BASE_URL);
const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "login",
    component: Login,
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
  {
    path: "/status",
    name: "status",
    component: TaskHome,
    children: [
      {
        path: ":id",
        name: "statusDetail",
        component: StatusTable,
      },
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
  {
    path: "/:notfoundpath(.*)",
    name: "NotFound",
    component: NotFound,
  },
];

const router = createRouter({
  history,
  routes,
  linkActiveClass: "text-[#2ff6da]",
  linkExactActiveClass: "hover:text-[#2ff6da] hover:text-[#2ff6da] p-2",
});

export default router;
