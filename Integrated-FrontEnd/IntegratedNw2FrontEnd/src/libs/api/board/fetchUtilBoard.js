const url = import.meta.env.VITE_BASE_URL
function getToken() {
  return localStorage.getItem("token")
}

async function getBoardData() {
  try {
    const token = getToken()
    const res = await fetch(`${url}/v3/board`, {
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
    const res = await fetch(`${url}/v3/board`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        ...newBoard,
      }),
    })
    const addedTask = await res.json()
    return addedTask
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

export { getBoardData, addBoard }
