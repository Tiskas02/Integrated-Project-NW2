//BackEnd
async function getTaskData(url) {
  try {
    const res = await fetch(`${url}/v1/tasks`);
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    console.log(error);
    return null;
  }
}
async function getTaskById(url, id) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`);
    if(!res.ok){
      return null;
    }
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}

async function addTask(url, newTask) {
  try {
    const res = await fetch(`${url}/v1/tasks`, {
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
async function deleteItemById(url, id) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`, {
      method: "DELETE",
    });
    return res.status;
  } catch (error) {
    console.error("Error adding task:", error);
    return null;
  }
}

async function updateTask(url, id, editTask) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`, {
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
export { getTaskData, getTaskById, addTask, deleteItemById, updateTask };
