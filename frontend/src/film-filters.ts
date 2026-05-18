import { apiFetch, Film } from "./api.js";

// ── Length Filter ───────────────────────────────────────────────────────
async function loadByLength(min: number, max: number): Promise<void> {
    const container = document.getElementById("length-results")!;
    container.innerHTML = '<p class="loading">Loading...</p>';
     const films = await apiFetch<Film[]>(`/api/films/search?minLength=${min}&maxLength=${max}`); // (55)
    //const films = await apiFetch<Film[]>(`/api/by-length`); // (55)

    renderCardList(films, container);
}

// ── Rental Duration Filter ──────────────────────────────────────────────
async function loadByDuration(min: number, max: number): Promise<void> {
    const container = document.getElementById("duration-results")!;
    container.innerHTML = '<p class="loading">Loading...</p>';
    const films = await apiFetch<Film[]>(`/api/films/by-duration?min=${min}&max=${max}`); // (56)
    renderCardList(films, container);
}

function renderCardList(films: Film[], container: HTMLElement): void {
    container.innerHTML = films.length === 0
        ? "<p>Кино олдсонгүй.</p>"
        : films.map(f => `<div class="film-card"><span class="rating">${f.rating}</span> <strong>${f.title}</strong> <span class="price">$${f.rentalRate}</span></div>`).join("");
}

function init(): void {
    document.getElementById("btn-length-filter")!.addEventListener("click", () => {

        const min = Number((document.getElementById("length-min") as HTMLInputElement).value); // (57)
        const max = Number((document.getElementById("length-max") as HTMLInputElement).value); // (58)
        if (min >= 0 && max > min) loadByLength(min, max);
    });
    
    document.getElementById("btn-duration-filter")!.addEventListener("click", () => {
        const min = Number((document.getElementById("duration-min") as HTMLInputElement).value); // (59)
        const max = Number((document.getElementById("duration-max") as HTMLInputElement).value);
        if (min >= 0 && max > min) loadByDuration(min, max);
    });
}
// init() дуудлагын оронд үүнийг ашигла
document.addEventListener("DOMContentLoaded", () => {
    init();
});