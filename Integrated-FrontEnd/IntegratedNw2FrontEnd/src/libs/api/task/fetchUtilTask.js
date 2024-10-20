const url = import.meta.env.VITE_BASE_URL

function getToken() {
  return localStorage.getItem("token")
}

async function getTaskDataInBoardId(id,collabid) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${id}/tasks/${collabid}`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
    })
    const data = await res.json()
    console.log(`data from fetch : ${data}`);
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function getTaskById(routeId, id) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${routeId}/tasks/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
    })
    if (!res.ok) {
      alert("The requested task does not exist")
      history.back()
      return null
    }
    const data = await res.json()
    console.log(data);
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function getBoardByBoardId(boardid,collabId){
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${boardid}/${collabId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
    })
    if (!res.ok) {
      alert("The requested task does not exist")
      history.back()
      return null
    }
    const data = await res.json()
    console.log(data);
    
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function addTask(newTask, id) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${id}/tasks`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({
        ...newTask,
      }),
    })
    const addedTask = await res.json()
    return addedTask
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

async function deleteItemById(routerId, id) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${routerId}/tasks/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      },
    })
    return res.status
  } catch (error) {
    console.error("Error adding task:", error)
    return null
  }
}

async function editTask(routerId, id, editTask) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${routerId}/tasks/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({
        ...editTask,
      }),
    })
    const editedTask = await res.json()
    return editedTask
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

export { getTaskDataInBoardId,getBoardByBoardId, getTaskById, addTask, deleteItemById, editTask }
