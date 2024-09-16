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
    const token = getToken();
    const res = await fetch(`${url}/v3/board/${routeId}/task/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
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

async function deleteItemById(routerId,id) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/board/${routerId}/task/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      }});
    return res.status;
  } catch (error) {
    console.error("Error adding task:", error);
    return null;
  }
}

async function editTask(routerId,id, editTask) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/board/${routerId}/task/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`
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
