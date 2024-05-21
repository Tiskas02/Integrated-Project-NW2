const url = import.meta.env.VITE_BASE_URL;

async function getStatusData() {
  try {
    const res = await fetch(`${url}/statuses`);
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    console.log(error);
    return null;
  }
}
async function getStatusDataById(id) {
  try {
    const res = await fetch(`${url}/statuses/${id}`);
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    console.log(error);
    return null;
  }
}
async function addStatus(newStatus) {
  try {
    const res = await fetch(`${url}/statuses`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...newStatus,
      }),
    });
    const addedStatus = await res.json();
    console.log(addedStatus);
    return addedStatus;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}
async function editStatus(id, editStatus) {
  try {
    const res = await fetch(
      `${url}/statuses/${id}`,
      {
        method: "PUT",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          ...editStatus,
        }),
      }
    );
    const editedStatus = await res.json();
    return editedStatus;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function shouldDeleteOrTransferStatus(id) {
  try {
    const res = await fetch(
      `${url}/statuses/${id}/indicator`
    );
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    console.log(error);
    return null;
  }
}
async function deleteOneStatus(id) {
  try {
    const res = await fetch(
      `${url}/statuses/${id}`,
      {
        method: "DELETE",
      }
    );
    return res;
  } catch (error) {
    console.error("Error fetching data:", error);
    console.log(error);
    return null;
  }
}
async function deleteTranferStatus(oldStatusId, newStatusId) {
  try {
    const res = await fetch(
      `${url}/statuses/${oldStatusId}/${newStatusId}`,
      {
        method: "DELETE",
      }
    );
    return res;
  } catch (error) {
    console.error("Error fetching data:", error);
    console.log(error);
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
  deleteTranferStatus
};
