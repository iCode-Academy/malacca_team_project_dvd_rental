import { apiFetch } from "./api.js";
const RATINGS = ["G", "PG", "PG-13", "R", "NC-17"];
function buildFilterButtons() {
    const container = document.getElementById("filter-buttons");
    container.innerHTML = RATINGS.map(r => `
        <button class="filter-btn" data-rating="${r}">${r}</button>
    `).join("");
    container.addEventListener("click", async (e) => {
        const btn = e.target.closest(".filter-btn");
        if (!btn)
            return;
        // Active товчийг тэмдэглэх
        container.querySelectorAll(".filter-btn")
            .forEach(b => b.classList.remove("active"));
        btn.classList.add("active");
        const rating = btn.getAttribute("data-rating");
        await loadByRating(rating);
    });
}
async function loadByRating(rating) {
    const container = document.getElementById("filtered-films");
    container.innerHTML = '<p class="loading">Loading...</p>';
    const films = await apiFetch(`/api/films?rating=${rating}`);
    container.innerHTML = films.map(film => `
        <div class="film-card">
            <span class="rating">${film.rating}</span>
            <strong>${film.title}</strong>
            <span class="price">$${film.rental_rate.toFixed(2)}</span>
        </div>
    `).join("");
}
buildFilterButtons();
