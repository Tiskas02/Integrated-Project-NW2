import { reactive } from "vue";

class TaskManagement {
  constructor() {
    this.tasks = reactive([]);
  }
  getTask() {
    return this.tasks;
  }
  setTasks(tasks = []) {
    return new Promise((resolve) => {
      this.tasks.length = 0;

      if (tasks.length === 0) {
        resolve();
        return;
      }

      tasks.forEach((task) => {
        task.status = this.convertStatus(task.status);
        this.tasks.push(task);
      });

      resolve();
    });
  }

  convertStatus(status) {
    const lowerLetter = status.toLowerCase();
    const capitalizedStatus =
      lowerLetter.charAt(0).toUpperCase() + lowerLetter.slice(1);
    const convertStatus = capitalizedStatus.replace(/_/g, " ");
    return convertStatus;
  }
  getTaskById() {
    return this.tasks;
  }
}
export { TaskManagement };
