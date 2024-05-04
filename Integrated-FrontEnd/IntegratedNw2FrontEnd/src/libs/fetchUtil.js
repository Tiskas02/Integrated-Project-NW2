async function getTaskData(url) {
  try {
    const res = await fetch(`${url}/v1/tasks`);
    const data = await res.json();
    return data
  } catch (error) {
    console.error('Error fetching data:', error);
    return null;
  }
}
async function getTaskById(url,id) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`);
    const data = await res.json();
    return data
  } catch (error) {
    console.error('Error fetching data:', error);
    return null;
  }
}

async function addItem(url,id) {
  try {
    const res = await fetch(`${url}/v1/tasks`, {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        ...id
      })
    })
    const addedItem = await res.json()
    return addedItem
  } catch (error) {
    console.log(`error: ${error}`)
  }
}


export { getTaskData , getTaskById , addItem}