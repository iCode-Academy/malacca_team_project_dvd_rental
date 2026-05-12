import { apiFetch, Film } from "./api.js";

async function searchFilms(title: string): Promise<void> {
    const container = document.getElementById("search-results")!;
    container.innerHTML = '<p class="loading">Хайж байна...</p>';

    const params = new URLSearchParams({ title });
    const films = await apiFetch<Film[]>(`/api/films/search?${params}`);

    if (films.length === 0) {
        container.innerHTML = `<p>"${title}" нэртэй кино олдсонгүй.</p>`;
        return;
    }

    container.innerHTML = films.map(film => `
        <div class="film-card">
            <span class="rating">${film.rating}</span>
            <strong>${film.title}</strong>
            <span class="price">$${film.rental_rate.toFixed(2)}</span>
        </div>
    `).join("");
}

document.getElementById("btn-search")!
    .addEventListener("click", () => {
        const input = document.getElementById("input-search") as HTMLInputElement;
        const title = input.value.trim();
        if (!title) return;
        searchFilms(title);
    });