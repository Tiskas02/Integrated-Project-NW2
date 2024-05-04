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

async function addTask(url, taskData) {
  try {
    const res = await fetch(`${url}/v1/tasks`, {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        title: taskData.title.trim(),
        assignees: taskData.assignees.trim(),
        description: taskData.description.trim(),
        status: taskData.status.trim()
      })
    });
    const addedItem = await res.json();
    return addedItem;
  } catch (error) {
    console.error('Error adding task:', error);
    return null;
  }
}



export { getTaskData , getTaskById , addTask}