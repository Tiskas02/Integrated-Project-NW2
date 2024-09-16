const url = import.meta.env.VITE_BASE_URL;
function getToken() {
  return localStorage.getItem("token");
}
async function getTaskDataInBoardId(id) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/board/${id}/tasks`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}

async function getTaskById(routeId,id) {
  try {
    const res = await fetch(`${url}/v3/board/${routeId}/task/${id}`);
    if (!res.ok) {
      alert("The requested task does not exist");
      history.back();
      return null;
    }
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}

async function addTask(newTask,id) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/board/${id}/task`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`
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

export { getTaskDataInBoardId, getTaskById, addTask, deleteItemById, editTask };
