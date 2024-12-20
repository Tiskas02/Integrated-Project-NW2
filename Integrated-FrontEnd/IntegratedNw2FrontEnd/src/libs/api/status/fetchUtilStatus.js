const url = import.meta.env.VITE_BASE_URL;

function getToken() {
  return localStorage.getItem("token");
}

async function getStatusData(id) {
  try {
    const token = getToken();
    let res;
    if (token === null) {
      res = await fetch(`${url}/v3/boards/${id}/statuses`);
    } else {
      res = await fetch(`${url}/v3/boards/${id}/statuses`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    }
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}

async function getStatusDataById(routeId, id) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/boards/${routeId}/statuses/${id}`, {
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
async function addStatus(newStatus, id) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/boards/${id}/statuses`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        ...newStatus,
      }),
    });
    const addedStatus = await res.json();
    return addedStatus;
  } catch (error) {
    console.error(`error: ${error}`);
  }
}

async function editStatus(id, editStatus, routerId) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/boards/${routerId}/statuses/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        ...editStatus,
      }),
    });
    const editedStatus = await res.json();
    return editedStatus;
  } catch (error) {
    console.error(`error: ${error}`);
  }
}

async function shouldDeleteOrTransferStatus(statusId, boardId) {
  try {
    const token = getToken();
    const res = await fetch(
      `${url}/v3/boards/${boardId}/statuses/${statusId}/indicator`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}
async function deleteOneStatus(boardId, id) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/boards/${boardId}/statuses/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return res;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}

async function deleteTranferStatus(boardId, oldStatusId, newStatusId) {
  try {
    const token = getToken();
    const res = await fetch(
      `${url}/v3/boards/${boardId}/statuses/${oldStatusId}/${newStatusId}`,
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return res;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
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
};
