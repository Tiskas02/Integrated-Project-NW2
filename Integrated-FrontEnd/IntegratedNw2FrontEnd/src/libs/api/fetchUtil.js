async function getTaskData() {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v2/tasks`);
      const data = await res.json();
      console.log(data);
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
      return null;
    }
  }

  export { getTaskData };   