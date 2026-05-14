import { apiFetch, Actor } from "./api.js";
import { showLoading, showSuccess, showError } from "./status.js";

async function loadActors(): Promise<void> {
    const container = document.getElementById("actor-list")!;
    container.innerHTML = '<p class="loading">Loading...</p>';

    const actors = await apiFetch<Actor[]>("/api/actors");

    container.innerHTML = actors.map(actor => `
        <span class="actor-card">
            ${actor.actorId} ${actor.firstName} ${actor.lastName} 
        </span>
    `).join("");
}

document.getElementById("btn-load-actors")!
    .addEventListener("click", loadActors);
