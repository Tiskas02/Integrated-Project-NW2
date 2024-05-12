async function getStatusData() {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v2/statuses`);
      console.log(res ?'fetched':'cannot fetch');
      const data = await res.json();
      console.log(data);
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
      return null;
    }
  }
  async function addStatus(newStatus) {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v2/statuses`, {
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          ...newStatus,
        }),
      });
      const addedStatus = await res.json();
      return addedStatus;
    } catch (error) {
      console.log(`error: ${error}`);
    }
  }
  export { getStatusData,addStatus }  
