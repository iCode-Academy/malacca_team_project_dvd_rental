import { apiFetch } from "./api.js";
async function loadByRating(rating) {
    const container = document.getElementById("rating-results");
    container.innerHTML = '<p class="loading">Loading...</p>';
    const films = await apiFetch(`/api/films/search?rating=${encodeURIComponent(rating)}`);
    renderFilmCards(films, container);
}
async function searchByKeyword(keyword) {
    const container = document.getElementById("search-results");
    container.innerHTML = '<p class="loading">Хайж байна...</p>';
    const films = await apiFetch(`/api/films/search?keyword=${encodeURIComponent(keyword)}`);
    renderFilmCards(films, container);
}
function renderFilmCards(films, container) {
    if (films.length === 0) {
        container.innerHTML = "<p>Кино олдсонгүй.</p>";
        return;
    }
    container.innerHTML = films.map(f => `
        <div class="film-card" data-id="${f.filmId}">
            <span class="rating">${f.rating}</span>
            <strong>${f.title}</strong>
            <span class="price">$${f.rentalRate}</span>
        </div>
    `).join("");
}
const RATINGS = ["G", "PG", "PG-13", "R", "NC-17"];
function init() {
    const filterButtons = document.getElementById("filter-buttons");
    if (filterButtons) {
        filterButtons.innerHTML = RATINGS.map(r => `<button class="filter-btn" data-rating="${r}">${r}</button>`).join("");
        filterButtons.addEventListener("click", (e) => {
            const btn = e.target.closest(".filter-btn");
            if (!btn)
                return;
            document.querySelectorAll(".filter-btn").forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            loadByRating(btn.getAttribute("data-rating"));
        });
    }
    // fixed: was "btn-search-film" but HTML has "btn-search"
    const searchBtn = document.getElementById("btn-search");
    if (searchBtn) {
        searchBtn.addEventListener("click", () => {
            const input = document.getElementById("input-search");
            const keyword = input.value.trim();
            if (keyword)
                searchByKeyword(keyword);
        });
    }
}
init();
