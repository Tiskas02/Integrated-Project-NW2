const url = import.meta.env.VITE_BASE_URL

function getToken() {
  return localStorage.getItem("token")
}

async function getStatusData(id) {
  try {
    const res = await fetch(`${url}/v3/boards/${id}/statuses`)
    const data = await res.json()
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function getStatusDataById(routeId, id) {
  try {
    const res = await fetch(`${url}/v3/boards/${routeId}/statuses/${id}`)
    const data = await res.json()
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}
async function addStatus(newStatus, id) {
  try {
    const res = await fetch(`${url}/v3/boards/${id}/statuses`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...newStatus,
      }),
    })
    const addedStatus = await res.json()
    return addedStatus
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

async function editStatus(id, editStatus, routerId) {
  try {
    const res = await fetch(`${url}/v3/boards/${routerId}/statuses/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...editStatus,
      }),
    })
    const editedStatus = await res.json()
    return editedStatus
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

async function shouldDeleteOrTransferStatus(id) {
  try {
    const res = await fetch(`${url}/v3/boards/statuses/${id}/indicator`)
    const data = await res.json()
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}
async function deleteOneStatus(id) {
  try {
    const res = await fetch(`${url}/v3/boards/statuses/${id}`, {
      method: "DELETE",
    })
    return res
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function deleteTranferStatus(oldStatusId, newStatusId) {
  try {
    const res = await fetch(
      `${url}/v3/boards/statuses/${oldStatusId}/${newStatusId}`,
      {
        method: "DELETE",
      }
    )
    return res
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

export {
  getStatusData,
  addStatus,
  getStatusDataById,
  editStatus,
  shouldDeleteOrTransferStatus,
  deleteOneStatus,
  deleteTranferStatus,
}
