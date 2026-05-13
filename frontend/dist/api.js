export const API_BASE = "http://localhost:8080";
export async function apiFetch(path) {
    const res = await fetch(`${API_BASE}${path}`);
    if (!res.ok)
        throw new Error(`HTTP ${res.status}: ${res.statusText}`);
    return res.json();
}
