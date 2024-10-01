import { createRouter, createWebHistory } from "vue-router"
import NotFound from "../views/NotFound.vue"
import TaskHome from "@/views/TaskHome.vue"
import TaskModal from "@/components/TaskModal.vue"
import StatusTable from "@/components/StatusTable.vue"
import TaskAddEdit from "@/components/TaskAddEdit.vue"
import StatusAddEdit from "@/components/StatusAddEdit.vue"
import Login from "@/components/Login.vue"
import BoardHome from "@/views/BoardHome.vue"
const url = import.meta.env.VITE_BASE_URL
function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1]
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/")
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    )
    return JSON.parse(jsonPayload)
  } catch (error) {
    return null // Return null if parsing fails
  }
}

async function refreshAccessToken() {
  try {
    const refreshToken = localStorage.getItem("refresh_token")
    const res = await fetch(`${url}/token`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${refreshToken}`,
        "content-type": "application/json",
      },
    })

    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`)
    }

    const data = await res.json()

    localStorage.setItem("token", data.access_token)
    return data
  } catch (error) {
    console.log(`Error during token refresh: ${error}`)
    return null
  }
}

// Helper function to delete token from localStorage
function deleteTokenFromLocalStorage() {
  localStorage.removeItem("token")
  localStorage.removeItem("refresh_token");
}

const history = createWebHistory(import.meta.env.BASE_URL)

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
    path: "/board",
    name: "board",
    component: BoardHome,
    meta: { requiresAuth: true },
  },
  {
    path: "/board/:id/task",
    name: "Task",
    component: TaskHome,
    meta: { requiresAuth: true },
    children: [
      {
        path: ":taskid",
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
        path: ":editid/edit",
        name: "editTask",
        component: TaskAddEdit,
        meta: { requiresAuth: true },
      },
    ],
  },
  {
    path: "/board/:id/status",
    name: "status",
    component: TaskHome,
    meta: { requiresAuth: true },
    children: [
      {
        path: ":statusid",
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
        path: ":editid/edit",
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
]

const router = createRouter({
  history,
  routes,
  linkActiveClass: "text-[#2ff6da]",
  linkExactActiveClass: "hover:text-[#2ff6da] hover:text-[#2ff6da] p-2",
})

router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem("token");
  const refreshToken = localStorage.getItem("refresh_token");
  let isAuthenticated = false;

  if (token) {
    const jwtPayload = parseJwt(token);
    const jwtPayloadRefresh = parseJwt(refreshToken);

    if (jwtPayload && jwtPayload.exp >= Date.now() / 1000) {
      isAuthenticated = true;
    } else if (jwtPayloadRefresh && jwtPayloadRefresh.exp >= Date.now() / 1000) {
      try {
        const refreshResponse = await refreshAccessToken();
        if (refreshResponse) {
          localStorage.setItem("token", refreshResponse.access_token);
          isAuthenticated = true;
        } else {
          console.log("Failed to refresh access token. Please log in again.");
          deleteTokenFromLocalStorage();
        }
      } catch (error) {
        console.log("Error refreshing token: ", error);
        deleteTokenFromLocalStorage();
        alert("Your session has expired. Please log in again.");
      }
    } else {
      console.log("Session expired. Redirecting to login.");
      deleteTokenFromLocalStorage();
      alert("Your session has expired. Please log in again.");
    }
  }

  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: "login" });
  } else {
    next();
  }
});
export default router
