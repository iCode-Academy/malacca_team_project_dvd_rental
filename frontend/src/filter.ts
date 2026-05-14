import { apiFetch, Film } from "./api.js";
import { showLoading, showSuccess, showError } from "./status.js";

const RATINGS = ["G", "PG", "PG-13", "R", "NC-17"];

function buildFilterButtons(): void {
    const container = document.getElementById("filter-buttons")!;
    container.innerHTML = RATINGS.map(r => `
        <button class="filter-btn" data-rating="${r}">${r}</button>
    `).join("");

    container.addEventListener("click", async (e) => {
        const btn = (e.target as HTMLElement).closest(".filter-btn");
        if (!btn) return;

        // Active товчийг тэмдэглэх
        container.querySelectorAll(".filter-btn")
            .forEach(b => b.classList.remove("active"));
        btn.classList.add("active");

        const rating = btn.getAttribute("data-rating")!;
        await loadByRating(rating);
    });
}

async function loadByRating(rating: string): Promise<void> {
    const container = document.getElementById("filtered-films")!;
    container.innerHTML = '<p class="loading">Loading...</p>';

    // const films = await apiFetch<Film[]>(`/api/films?rating=${rating}`);

    // container.innerHTML = films.map(film => `
    //     <div class="film-card">
    //         <span class="rating">${film.rating}</span>
    //         <strong>${film.title}</strong>
    //         <span class="price">$${film.rentalRate.toFixed(2)}</span>
    //     </div>
    // `).join("");

    showLoading(`${rating} ангиллын кинонуудыг татаж байна...`);

    try {
        const films = await apiFetch<Film[]>(`/api/films?rating=${rating}`);

        // 2. Кинонуудыг карт хэлбэрээр дүрслэх
        container.innerHTML = films.map(film => `
            <div class="film-card">
                <span class="rating">${film.rating}</span>
                <strong>${film.title}</strong>
                <span class="price">$${film.rentalRate.toFixed(2)}</span>
            </div>
        `).join("");

        // 3. Амжилттай болсон мэдэгдэл
        showSuccess(`${films.length} кино ачааллав.`);
        
    } catch (err) {
        // 4. Алдаа гарвал харуулах
        showError(`Кино татахад алдаа гарлаа: ${err}`);
        container.innerHTML = `<p class="error">Өгөгдөл татахад алдаа гарлаа.</p>`;
    }
}

buildFilterButtons();