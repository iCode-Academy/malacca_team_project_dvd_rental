"use strict";
// ── Interfaces — Spring Boot JPA API-ийн JSON хэлбэр ─────────────────────
Object.defineProperty(exports, "__esModule", { value: true });
exports.API_BASE = void 0;
exports.apiFetch = apiFetch;
// ── Generic fetch helper ──────────────────────────────────────────────────
exports.API_BASE = "http://localhost:8080";
async function apiFetch(path) {
    const res = await fetch(`${exports.API_BASE}${path}`);
    if (!res.ok)
        throw new Error(`HTTP ${res.status}: ${res.statusText}`);
    return res.json();
}
//# sourceMappingURL=api.js.map