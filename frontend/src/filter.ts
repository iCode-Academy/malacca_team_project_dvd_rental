import { apiFetch, Film } from "./api.js";
import { showLoading, showSuccess, showError } from "./status.js";

const RATINGS = ["G", "PG", "PG-13", "R", "NC-17"];

// --- 1. Rating Filter Logic ---
function buildFilterButtons(): void {
    const container = document.getElementById("filter-buttons");
    if (!container) return;

    container.innerHTML = RATINGS.map(r => `
        <button class="filter-btn" data-rating="${r}">${r}</button>
    `).join("");

    container.addEventListener("click", async (e) => {
        const btn = (e.target as HTMLElement).closest(".filter-btn");
        if (!btn) return;

        container.querySelectorAll(".filter-btn").forEach(b => b.classList.remove("active"));
        btn.classList.add("active");

        const rating = btn.getAttribute("data-rating")!;
        await loadByRating(rating);
    });
}

async function loadByRating(rating: string): Promise<void> {
    const container = document.getElementById("filtered-films")!;
    showLoading(`${rating} ангиллын кинонуудыг татаж байна...`);

    try {
        // Таны Controller дээр /api/films/search?rating=... гэж байгааг анхаарна уу!
        const films = await apiFetch<Film[]>(`/api/films/search?rating=${rating}`);
        renderCardList(films, container);
        showSuccess(`${films.length} кино ачааллав.`);
    } catch (err) {
        showError(`Алдаа: ${err}`);
    }
}

// --- 2. Length & Duration Logic ---
async function loadByLength(min: number, max: number): Promise<void> {
    const container = document.getElementById("length-results")!;
    showLoading("Length-ээр шүүж байна...");
    try {
        // Backend-ийн @RequestParam(minLength, maxLength)-тэй тааруулав
        const films = await apiFetch<Film[]>(`/api/films/search?minLength=${min}&maxLength=${max}`);
        renderCardList(films, container);
        showSuccess(`Хайлтаар ${films.length} кино олдлоо.`);
    } catch (err) {
        showError("Алдаа гарлаа");
    }
}

async function loadByDuration(min: number, max: number): Promise<void> {
    const container = document.getElementById("duration-results")!;
    showLoading("Duration-оор шүүж байна...");
    try {
        const films = await apiFetch<Film[]>(`/api/films/by-duration?min=${min}&max=${max}`);
        renderCardList(films, container);
        showSuccess(`Хайлтаар ${films.length} кино олдлоо.`);
    } catch (err) {
        showError("Алдаа гарлаа");
    }
}

// --- Common UI Renderer ---
function renderCardList(films: Film[], container: HTMLElement): void {
    container.innerHTML = films.length === 0
        ? "<p>Кино олдсонгүй.</p>"
        : films.map(f => `
            <div class="film-card">
                <span class="rating">${f.rating}</span>
                <strong>${f.title}</strong>
         // filter.ts доторх renderCardList функц дотор
<span class="price">$${(f as any).rental_rate || f.rentalRate}</span>
            </div>
        `).join("");
}

// --- Initializer ---
function init(): void {
    console.log("Filter module initialized"); // Консол дээр гарах ёстой
    
    buildFilterButtons();

    // Length Filter Event
    const btnLength = document.getElementById("btn-length-filter");
    if (btnLength) {
        btnLength.addEventListener("click", () => {
            console.log("Length filter clicked");
            const min = Number((document.getElementById("length-min") as HTMLInputElement).value);
            const max = Number((document.getElementById("length-max") as HTMLInputElement).value);
            if (max > min) loadByLength(min, max);
        });
    }

    // Duration Filter Event
    const btnDuration = document.getElementById("btn-duration-filter");
    if (btnDuration) {
        btnDuration.addEventListener("click", () => {
            console.log("Duration filter clicked");
            const min = Number((document.getElementById("duration-min") as HTMLInputElement).value);
            const max = Number((document.getElementById("duration-max") as HTMLInputElement).value);
            if (max > min) loadByDuration(min, max);
        });
    }
}

// Хуудас ачаалагдаж дуусахад ажиллуулах
if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
} else {
    init();
}