async function getTaskData(url) {
  try {
    const res = await fetch(`${url}/v1/tasks`);
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}
async function getTaskById(url, id) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`);
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}
async function addTask(url, id) {
  try {
    const res = await fetch(`${url}/v1/tasks`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        title: taskData.title.trim(),
        assignees: taskData.assignees.trim(),
        description: taskData.description.trim(),
        status: taskData.status.trim(),
      }),
    });
    if (!response.ok) {
      throw new Error('Failed to delete task');
    }
    return await response.json();
  } catch (error) {
    console.error("Error adding task:", error);
    return null;
  }
}
async function updateTask(url, id) {
  try {
    console.log('testtt')
    const res = await fetch(`${url}/v1/tasks/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify(taskData),
    });
    const updatedTask = await res.json();
    return updatedTask;
  } catch (error) {
    console.error("Error updating task:", error);
    throw error; 
  }
}

export { getTaskData, getTaskById, addTask , updateTask};