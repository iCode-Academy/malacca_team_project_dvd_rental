import { apiFetch, Film } from "./api.js";

async function loadFilms(): Promise<void> {
    const container = document.getElementById("film-list")!;
    container.innerHTML = '<p class="loading">Loading...</p>';

    const films = await apiFetch<Film[]>("/api/films");
    renderFilmCards(films, container);
}

function renderFilmCards(films: Film[], container: HTMLElement): void {
    if (films.length === 0) {
        container.innerHTML = "<p>Кино олдсонгүй.</p>";
        return;
    }
    container.innerHTML = films.map(film => `
        <div class="film-card" data-id="${film.film_id}">
            <span class="rating">${film.rating}</span>
            <strong>${film.title}</strong>
            <span class="price">$${film.rental_rate.toFixed(2)}</span>
        </div>
    `).join(""); 
}

document.getElementById("btn-load-films")!
    .addEventListener("click", loadFilms);

    // renderFilmCards дотор film.film_id-г data-id attribute-т хэрхэн нэмэх вэ?
// <div class="film-card" data-id="${film.film_id}">

// rental_rate-г 2 decimal-тай харуулах
// $${film.rental_rate.toFixed(2)}