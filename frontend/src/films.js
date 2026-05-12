"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const api_js_1 = require("./api.js");
async function loadFilms() {
    const container = document.getElementById("film-list");
    container.innerHTML = '<p class="loading">Loading...</p>';
    const films = await (0, api_js_1.apiFetch)("/api/films");
    renderFilmCards(films, container);
}
function renderFilmCards(films, container) {
    if (films.length === 0) {
        container.innerHTML = "<p>Кино олдсонгүй.</p>";
        return;
    }
    container.innerHTML = films.map(film => `
        <div class="film-card" data-id="${film.film_id}">
            <span class="rating">${film.rating}</span>
            <strong>${film.title}</strong>
            <span class="price">$${film.rentalRate.toFixed(2)}</span>
        </div>
    `).join("");
}
document.getElementById("btn-load-films")
    .addEventListener("click", loadFilms);
// renderFilmCards дотор film.film_id-г data-id attribute-т хэрхэн нэмэх вэ?
// <div class="film-card" data-id="${film.film_id}">
// rental_rate-г 2 decimal-тай харуулах
// $${film.rental_rate.toFixed(2)}
//# sourceMappingURL=films.js.map