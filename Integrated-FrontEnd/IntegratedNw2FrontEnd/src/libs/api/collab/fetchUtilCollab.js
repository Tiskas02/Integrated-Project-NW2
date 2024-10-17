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
    console.log(`data from fetch : ${data}`);
    return data
  } catch (error) {
    console.error("Error fetching data:", error)
    return null
  }
}
export { getAllCollabDataByUserId }