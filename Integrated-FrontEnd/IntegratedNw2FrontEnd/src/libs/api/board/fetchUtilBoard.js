const url = import.meta.env.VITE_BASE_URL;

async function getBoardData() {
    try {
        const res = await fetch(`${url}/v2/boards`);
        const data = await res.json();
        return data;
    } catch (error) {
        console.error("Error fetching data:", error);
        return null;
    }
}
export { getBoardData };