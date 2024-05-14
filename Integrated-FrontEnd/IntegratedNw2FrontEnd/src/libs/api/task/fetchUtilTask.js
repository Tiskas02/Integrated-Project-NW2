const url = import.meta.env.VITE_BASE_URL;

async function getTaskData() {
    try {
      const res = await fetch(`${url}/v2/tasks`);
      const data = await res.json();
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
      return null;
    }
  }

  async function getTaskById(id) {
    try {
      const res = await fetch(`${url}/v2/tasks/${id}`);
      
      // if the response is not ok, go back to the previous page
      // if(!res.ok){
      //   history.back()
      //   return null;
      // }

      const data = await res.json();
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      return null;
    }
  }
  async function addTask(newTask) {
    console.log(newTask);
    try {
      const res = await fetch(`${url}/v2/tasks`, {
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          ...newTask,
        }),
      });
      const addedTask = await res.json()
      return addedTask;
    } catch (error) {
      console.log(`error: ${error}`);
    }
  }

  async function deleteItemById(id) {
    try {
      const res = await fetch(`${url}/v2/tasks/${id}`, {
        method: "DELETE",
      });
      return res.status;
    } catch (error) {
      console.error("Error adding task:", error);
      return null;
    }
  }

  async function editTask(id, editTask) {
    try {
      const res = await fetch(`${url}/v2/tasks/${id}`, {
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
  export { getTaskData , getTaskById , addTask , deleteItemById , editTask};   