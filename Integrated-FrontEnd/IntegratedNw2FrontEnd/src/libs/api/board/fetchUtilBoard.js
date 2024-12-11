const url = import.meta.env.VITE_BASE_URL;

function getToken() {
  return localStorage.getItem("token");
}

async function getBoardData() {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/boards`, {
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

async function getBoardDataByCollabId(boardId) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/boards/collab/${boardId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    const data = await res.json();
    console.log(`data from fetch : ${data}`);
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}

async function addBoard(newBoard) {
  try {
    const token = getToken();
    const res = await fetch(`${url}/v3/boards`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        ...newBoard,
      }),
    });
    if (!res.ok) {
      throw new Error("There is a problem. Please try again later.");
    }
    const addedTask = await res.json();
    return addedTask;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function updateBoardVisibility(id, visibility) {
  try {
    console.log(`id: ${id}, visibility: ${visibility}`);
    const token = getToken();
    const res = await fetch(`${url}/v3/boards/${id}`, {
      method: "PATCH",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        visibility: visibility,
      }),
    });
    if (!res.ok) {
      throw new Error("There is a problem. Please try again later.");
    }
    const updatedBoard = res.json();
    return updatedBoard;
  } catch (error) {
    console.error("Error updating data:", error);
  }
}

export {
  getBoardData,
  addBoard,
  updateBoardVisibility,
  getBoardDataByCollabId,
};
