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
async function deleteTask(id) {
  try {
    console.log('testtt');
    const response = await fetch(`${url}/v1/tasks/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      throw new Error('Failed to delete task');
    }
    return await response.json();
  } catch (error) {
    console.error('Error deleting task:', error);
    throw error;
  }

};
export { getTaskData , getTaskById , deleteTask}

