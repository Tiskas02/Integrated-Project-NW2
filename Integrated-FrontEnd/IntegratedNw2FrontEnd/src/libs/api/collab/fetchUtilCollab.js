const url = import.meta.env.VITE_BASE_URL

function getToken() {
  return localStorage.getItem("token")
}

async function getAllCollabDataByUserId(collabId) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/collab/${collabId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    const data = await res.json()
    console.log(`data from fetch : ${data}`)
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function getCollabDataByBoardId(boardId) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${boardId}/collabs`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    const data = await res.json()   
    console.log(data);
     
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function addCollabData(newCollab, routeId) {
  try {
    console.log("Sending collab data:", newCollab)

    const token = getToken()
    if (!token) {
      throw new Error("Token is missing")
    }

    const res = await fetch(`${url}/v3/boards/${routeId}/collabs`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(newCollab),
    })

    if (!res.ok) {
      throw new Error(`HTTP error! Status: ${res.status}`)
    }

    const data = await res.json()
    console.log("Response data:", data)
    return data
  } catch (error) {
    console.error("Error fetching data:", error.message)
    return null
  }
}
async function deleteCollabUtil(collabId, routeId) {
  try {
    const token = getToken()
    console.log("collabId", collabId);
    console.log("routeId", routeId);
    
    const res = await fetch(`${url}/v3/boards/${routeId}/collabs/${collabId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    // const data = await res.json();
    return res
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function sendInvitationEmail(newCollab, boardId) {
  try {
    console.log(newCollab)
    console.log(boardId);
    const token = getToken()
    const res = await fetch(`${url}/v3/boards/${boardId}/collabs/invitations`, {
      method: "PATCH",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(newCollab),
    })
    if (!res.ok) {
      throw new Error(`HTTP error! Status: ${res.status}`)
    }
    return res
  } catch (error) {
    console.error("Error fetching data:", error.message)
    return null
  }
}

export {
  getAllCollabDataByUserId,
  getCollabDataByBoardId,
  addCollabData,
  deleteCollabUtil,
  sendInvitationEmail,
}
