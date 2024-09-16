const url = import.meta.env.VITE_BASE_URL;

async function getBoardData() {
    try {
        const token = getToken();
        const res = await fetch(
            `${url}/v3/boards`,{
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
        const data = await res.json();
        console.log(data);
        return data;
    } catch (error) {
        console.error("Error fetching data:", error);
        return null;
    }
}
export { getBoardData };