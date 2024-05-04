import { ref } from "vue";

class TaskManagement {
  constructor() {
    this.tasks = []
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
        console.log(task);
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
  removeTask(removeId) {
    console.log(removeId);
    console.log(this.tasks.findIndex((task) => task.id === removeId));
    this.tasks.splice(
      this.tasks.findIndex((task) => task.id === removeId),
      1
    )
  }
  

  getTaskById() {
    return this.tasks;
  }
}
export default new TaskManagement() 
