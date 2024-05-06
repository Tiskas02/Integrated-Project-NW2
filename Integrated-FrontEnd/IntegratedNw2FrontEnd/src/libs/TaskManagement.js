import { pushScopeId, reactive, ref } from "vue";
//frontEnd
class TaskManagement {
  constructor() {
    this.tasks = [];
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
  addTask({taskId, title, assignees, description, status, createdOn, updatedOn}) {
    console.log(taskId);
    this.tasks.push({
      taskId: taskId,
      title: title,
      assignees: assignees,
      description: description,
      status: status,
      createdOn: createdOn,
      updatedOn: updatedOn
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
    this.tasks = this.tasks.filter((task) => {
      return task.taskId != removeId;
    });
  }
  // editTask(taskId) {
  //   this.tasks = this.tasks.map((task) => {
  //     return task.taskId === taskId
  //     ?{...task, taskId: taskId}
  //     :task
  //   })
  // }
  updateTask({
    taskId,
    title,
    description,
    status,
    assignees,
    createdOn,
    updatedOn,
  }) {
    this.tasks = this.tasks.map((task) => {
      return task.taskId === taskId
        ? {
            ...task,
            title: title,
            description: description,
            status: status,
            assignees: assignees,
            createdOn: createdOn,
            updatedOn: updatedOn,
          }
        : task;
    });
  }
  getTaskById() {
    return this.tasks;
  }
}
export default new TaskManagement();
