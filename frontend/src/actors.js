"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const api_js_1 = require("./api.js");
async function loadActors() {
    const container = document.getElementById("actor-list");
    container.innerHTML = '<p class="loading">Loading...</p>';
    const actors = await (0, api_js_1.apiFetch)("/api/actors");
    container.innerHTML = actors.map(actor => `
        <span class="actor-card">
            ${actor.actorId} ${actor.firstName} ${actor.lastName} 
        </span>
    `).join("");
}
document.getElementById("btn-load-actors")
    .addEventListener("click", loadActors);
//# sourceMappingURL=actors.js.map