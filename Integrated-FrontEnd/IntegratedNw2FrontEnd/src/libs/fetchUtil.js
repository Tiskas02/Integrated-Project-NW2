//BackEnd
async function getTaskData(url) {
  try {
    const res = await fetch(`${url}/v1/tasks`);
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    console.log(error);
    return null;
  }
}
async function getTaskById(url, id) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`);
    if (!res.ok) {
      return null;
    }
    const data = await res.json();
    return data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return null;
  }
}

// async function addTask(url, taskData) {
//   try {
//     const res = await fetch(`${url}/v1/tasks`, {
//       method: 'POST',
//       headers: {
//         'content-type': 'application/json'
//       },
//       body: JSON.stringify({
//         title: taskData.title.trim(),
//         assignees: taskData.assignees.trim(),
//         description: taskData.description.trim(),
//         status: taskData.status.trim()
//       })
//     });
//     const addedItem = await res.json();
//     return addedItem;
//   } catch (error) {
//     console.error('Error adding task:', error);
//     return null;
//   }
// }
async function addTask(url, newTask) {
  try {
    const res = await fetch(`${url}/v1/tasks`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...newTask,
      }),
    });
    const addedTask = await res.json();
    return addedTask;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}
async function deleteItemById(url, id) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`, {
      method: "DELETE",
    });
    return res.status;
  } catch (error) {
    console.error("Error adding task:", error);
    return null;
  }
}
// async function editTask(url, id) {
//   try {
//     const res = await fetch(`${url}/v1/tasks/${id}`, {
//       method:'PUT',
//       headers: {
//         'content-type': 'application/json'
//       },
//       body: JSON.stringify({
//         title: id.title,
//         assignees: id.assignees,
//         description: id.description,
//         status: id.status
//       })
//     })
//     const editTask = await res.json()
//     return editTask
//   } catch (error) {
//     console.log(`error:${error}`);
//   }
// }
async function updateTask(url, id, editTask) {
  try {
    const res = await fetch(`${url}/v1/tasks/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        ...editTask,
      }),
    });
    const editedTask = await res.json();
    return editedTask;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}
export { getTaskData, getTaskById, addTask, deleteItemById, updateTask };
