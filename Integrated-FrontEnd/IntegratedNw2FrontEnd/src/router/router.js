import { createRouter, createWebHistory } from "vue-router";
import NotFound from "../views/NotFound.vue";
import TaskHome from "@/views/TaskHome.vue";
import TaskModal from "@/components/TaskModal.vue";
import StatusTable from "@/components/StatusTable.vue";
import TaskAddEdit from "@/components/TaskAddEdit.vue";
import StatusAddEdit from "@/components/StatusAddEdit.vue";
import Login from "@/components/Login.vue";
import BoardHome from "@/views/BoardHome.vue";
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
    path:"/board",
    name:"board",
    component: BoardHome,
  },
  {
    path: "/task",
    name: "task",
    component: TaskHome,
    meta: { requiresAuth: true },
    children: [
      {
        path: ":id",
        name: "taskDetail",
        component: TaskModal,
        meta: { requiresAuth: true },
      },
      {
        path: "add",
        name: "addTask",
        component: TaskAddEdit,
        meta: { requiresAuth: true },
      },
      {
        path: ":id/edit",
        name: "editTask",
        component: TaskAddEdit,
        meta: { requiresAuth: true },
      },
    ],
  },
  {
    path: "/status",
    name: "status",
    component: TaskHome,
    meta: { requiresAuth: true },
    children: [
      {
        path: ":id",
        name: "statusDetail",
        component: StatusTable,
        meta: { requiresAuth: true },
      },
      {
        path: "add",
        name: "addStatus",
        component: StatusAddEdit,
        meta: { requiresAuth: true },
      },
      {
        path: ":id/edit",
        name: "editStatus",
        component: StatusAddEdit,
        meta: { requiresAuth: true },
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
router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem("userPayload"); 
  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'login' }); 
  } else {
    next(); 
  }
});

export default router;
