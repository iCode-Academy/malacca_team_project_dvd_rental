import { apiFetch } from "./api.js";
async function loadActors() {
    const container = document.getElementById("actor-list");
    container.innerHTML = '<p class="loading">Loading...</p>';
    const actors = await apiFetch("/api/actors");
    container.innerHTML = actors.map(actor => `
        <span class="actor-card">
            ${actor.first_name} ${actor.last_name}
        </span>
    `).join("");
}
document.getElementById("btn-load-actors")
    .addEventListener("click", loadActors);
