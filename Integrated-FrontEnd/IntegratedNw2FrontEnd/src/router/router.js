import { createRouter, createWebHistory } from "vue-router";
import Task from "../views/Task.vue";
import NotFound from "../views/NotFound.vue";
import Modal from "@/components/Modal.vue";

const history = createWebHistory();
const routes = [
	{
        path: "/",
        redirect: "/task" // Redirect root path to /task
    },
	{
		path: "/task",
		name: "task",
		component: Task,
		children: [{
				path: ':id',
				name: 'taskDetail',
				component: Modal,
				children: [
					{
						path: 'edit',
						name: 'editTask',
						component: Modal
					}
				]
			},
			{
				path: 'add',
				name: 'addTask',
				component: Modal
			}
		]
	},
    {
		path: "/:notfoundpath(.*)",
		name: "NotFound",
		component: Task,
		redirect: "/task"
	}
]

const router = createRouter({
	history,
	routes,
	linkActiveClass: "text-[#2ff6da]",
	linkExactActiveClass: "hover:text-[#2ff6da] hover:text-[#2ff6da] p-2",
});

export default router;