import { apiFetch } from "./api.js";
/**
 * API-аас киноны жагсаалтыг татаж харуулна.
 */
async function loadFilms() {
    const container = document.getElementById("film-list");
    container.innerHTML = '<p class="loading">Уншиж байна...</p>';
    try {
        const films = await apiFetch("/api/films");
        renderFilmCards(films, container);
    }
    catch (err) {
        console.error("Load Films Error:", err);
        container.innerHTML = `<p class="error">Алдаа гарлаа: ${err.message}</p>`;
    }
}
/**
 * Киноны объектуудыг HTML карт болгон хувиргана.
 */
function renderFilmCards(films, container) {
    if (!films || films.length === 0) {
        container.innerHTML = "<p>Кино олдсонгүй.</p>";
        return;
    }
    // Эхний объектыг консол дээр хэвлэж, ID-гийн нэрийг шалгах (Debug)
    console.log("Таны API-аас ирж буй өгөгдөл:", films[0]);
    let html = "";
    // Жагсаалтыг гүйлгэж карт бүрийг үүсгэнэ
    for (const film of films) {
        /* Монгол хэл дээрх тайлбар:
         Скриншот дээр "undefined" гэж гарч байсан учир ID-г авахдаа
         film_id эсвэл filmId аль алинтай нь ажиллахаар тохирууллаа.
        */
        const id = film.film_id || film.filmId;
        html += `
            <div class="film-card" data-id="${id}">
                <span class="rating">${film.rating}</span>
                <strong>${film.title}</strong>
                <span class="price">$${film.rentalRate.toFixed(2)}</span>
            </div>
        `;
    }
    container.innerHTML = html;
}
// "Load Films" товчлуурт event listener нэмэх
document.getElementById("btn-load-films")?.addEventListener("click", loadFilms);
