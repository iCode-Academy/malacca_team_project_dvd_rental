import { apiFetch, FilmDetail } from "./api.js";

/**
 * Тухайн киноны дэлгэрэнгүй мэдээллийг татаж, панел дээр харуулна.
 */
async function showFilmDetail(id: number): Promise<void> {
    const panel = document.getElementById("detail-panel")!;
    
    // Панелыг харагдах боломжтой болгож ачаалж буйг мэдэгдэнэ
    panel.style.display = "block";
    panel.innerHTML = '<p class="loading">Мэдээллийг ачаалж байна...</p>';

    try {
        // API-аас мэдээлэл татах
        const film = await apiFetch<FilmDetail>(`/api/films/${id}`);
        
        // Хүссэн загварын дагуу HTML бүтэц (Grid ашигласан)
        panel.innerHTML = `
            <div class="detail-header">
                <span class="rating">${film.rating}</span>
                <h3>${film.title}</h3>
                <button id="btn-close-detail" class="detail-close" title="Хаах">✕</button>
            </div>

            ${film.description ? `<p class="detail-desc">${film.description}</p>` : ""}

            <div class="detail-grid">
                <div class="detail-stat">
                    <span class="detail-label">Rental Rate</span>
                    <span class="detail-value price">$${film.rentalRate.toFixed(2)}</span>
                </div>
                <div class="detail-stat">
                    <span class="detail-label">Duration</span>
                    <span class="detail-value">${film.rental_duration} days</span>
                </div>
                <div class="detail-stat">
                    <span class="detail-label">Replace Cost</span>
                    <span class="detail-value">$${film.replacement_cost.toFixed(2)}</span>
                </div>
                <div class="detail-stat">
                    <span class="detail-label">Features</span>
                    <div class="detail-value">
                        ${film.special_features && film.special_features.length > 0
                            ? film.special_features.map(f => `<span class="feature-tag">${f}</span>`).join("")
                            : "—"
                        }
                    </div>
                </div>
            </div>
        `;

        // Хаах товчлуурын event-ийг тохируулах
        document.getElementById("btn-close-detail")?.addEventListener("click", (e) => {
            e.stopPropagation(); // Карт дарагдах event-ээс тусгаарлах
            panel.style.display = "none";
        });

    } catch (err) {
        console.error("Detail Error:", err);
        panel.innerHTML = `
            <div class="error">
                <strong>Алдаа гарлаа:</strong> ${(err as Error).message}
            </div>
        `;
    }
}

/**
 * Event Delegation: Хуудас дээрх аль ч .film-card дээр дарахад ажиллана.
 */
document.addEventListener("click", (e) => {
    const target = e.target as HTMLElement;
    
    // Хамгийн ойр байгаа .film-card-ыг олно
    const card = target.closest(".film-card");
    
    if (card) {
        const idAttr = card.getAttribute("data-id");
        
        // "undefined" биш байгааг шалгаж байна
        if (idAttr && idAttr !== "undefined") {
            const id = Number(idAttr);
            showFilmDetail(id);
            
            // Хэрэглэгч карт дээр дарахад дээшээ гүйлгэж панелыг харуулах (UX)
            window.scrollTo({ top: 0, behavior: 'smooth' });
        } else {
            console.error("ID олдсонгүй! films.js доторх film_id-г шалгана уу.");
        }
    }
});