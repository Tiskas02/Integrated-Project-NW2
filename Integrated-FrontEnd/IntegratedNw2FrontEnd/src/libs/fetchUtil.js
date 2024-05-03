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


async function deleteTaskById(taskId) {
  try {
      const res = await fetch(`${url}/v1/tasks/${taskId}`, {
          method: "DELETE",
      });
      if (res.ok) {
          // Optionally, you can return some data if needed
          const item = await res.json();
          return item;
      } else {
          // Handle non-200 status codes if needed
          throw new Error(`Failed to delete task. Status: ${res.status}`);
      }
  } catch (error) {
      // Handle any network errors or exceptions
      throw new Error(`Error deleting task: ${error.message}`);
  }
}


export { getTaskData, getTaskById, deleteTaskById };
