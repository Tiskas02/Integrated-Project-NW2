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
        console.log('pussy');
        task.status = this.convertStatus(task.status);
        this.tasks.push(task);
      });

      resolve();
    });
  }
  addTask({taskId, title, assignees, description, status, createdOn, updatedOn}) {
    let convertedStatus = '';
      switch (status) {
        case "NO_STATUS":
          convertedStatus = "No Status";
        case "TO_DO":
          convertedStatus = "To Do";
        case "DOING":
          convertedStatus = "Doing";
        case "DONE":
          convertedStatus = "Done";
      }
   
     this.tasks.push({
      taskId: taskId,
      title: title,
      assignees: assignees,
      description: description,
      status: convertedStatus,
      createdOn: createdOn,
      updatedOn: updatedOn
    });
    
    
  }
  convertStatusTwo(status) {
    switch (status) {
      case "No Status":
        return "No Status";
      case "To Do":
        return "To Do";
      case "Doing":
        return "Doing";
      case "Done":
        return "Done";
    }
  }

  convertStatus(status) {
    switch (status) {
      case "NO_STATUS":
        return "No Status";
      case "TO_DO":
        return "To Do";
      case "DOING":
        return "Doing";
      case "DONE":
        return "Done";
    }
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
