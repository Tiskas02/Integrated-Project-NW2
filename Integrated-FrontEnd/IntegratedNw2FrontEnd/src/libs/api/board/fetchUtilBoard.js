const url = import.meta.env.VITE_BASE_URL

function getToken() {
  return localStorage.getItem("token")
}

async function getBoardData() {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    const data = await res.json()
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}

async function addBoard(newBoard) {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/boards`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        ...newBoard,
      }),
    })
    if (!res.ok) {
      throw new Error("There is a problem. Please try again later.")
    }
    const addedTask = await res.json()
    return addedTask
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

export { getBoardData, addBoard }
