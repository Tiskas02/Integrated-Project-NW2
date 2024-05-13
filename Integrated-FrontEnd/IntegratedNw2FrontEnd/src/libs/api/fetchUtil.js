async function getTaskData() {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v2/tasks`);
      const data = await res.json();
      console.log(data);
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
      return null;
    }
  }

  async function getTaskById(id) {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/tasks/${id}`);
      if(!res.ok){
        history.back()
        return null;
      }
      const data = await res.json();
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      return null;
    }
  }
  async function addTask(newTask) {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/tasks`, {
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          ...newTask,
        }),
      });
      const addedTask = await res.json();
      return addedTask;
    } catch (error) {
      console.log(`error: ${error}`);
    }
  }

  async function deleteItemById(id) {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/tasks/${id}`, {
        method: "DELETE",
      });
      return res.status;
    } catch (error) {
      console.error("Error adding task:", error);
      return null;
    }
  }

  async function updateTask(id, editTask) {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/tasks/${id}`, {
        method: "PUT",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          ...editTask,
        }),
      });
      const editedTask = await res.json();
      return editedTask;
    } catch (error) {
      console.log(`error: ${error}`);
    }
  }
  export { getTaskData , getTaskById , addTask , deleteItemById , updateTask};   