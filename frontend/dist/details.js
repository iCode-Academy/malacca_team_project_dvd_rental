import { apiFetch } from "./api.js";
/**
 * Тухайн киноны дэлгэрэнгүй мэдээллийг татаж, панел дээр харуулна.
 */
async function showFilmDetail(id) {
    const panel = document.getElementById("detail-panel");
    panel.style.display = "block";
    panel.innerHTML = '<p class="loading">Мэдээллийг ачаалж байна...</p>';
    try {
        const film = await apiFetch(`/api/films/${id}`);
        // specialFeatures нь backend-аас "Trailers,Commentaries" гэж string ирнэ
        // тиймээс array болгоно
        const features = film.specialFeatures
            ? film.specialFeatures.split(",").map(f => f.trim()).filter(Boolean)
            : [];
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
                    <span class="detail-value">${film.rentalDuration} days</span>
                </div>
                <div class="detail-stat">
                    <span class="detail-label">Replace Cost</span>
                    <span class="detail-value">$${film.replacementCost.toFixed(2)}</span>
                </div>
                <div class="detail-stat">
                    <span class="detail-label">Features</span>
                    <div class="detail-value">
                        ${features.length > 0
            ? features.map(f => `<span class="feature-tag">${f}</span>`).join("")
            : "—"}
                    </div>
                </div>
            </div>
        `;
        document.getElementById("btn-close-detail")?.addEventListener("click", (e) => {
            e.stopPropagation();
            panel.style.display = "none";
        });
    }
    catch (err) {
        console.error("Detail Error:", err);
        panel.innerHTML = `
            <div class="error">
                <strong>Алдаа гарлаа:</strong> ${err.message}
            </div>
        `;
    }
}
/**
 * Event Delegation: Хуудас дээрх аль ч .film-card дээр дарахад ажиллана.
 */
document.addEventListener("click", (e) => {
    const target = e.target;
    const card = target.closest(".film-card");
    if (card) {
        const idAttr = card.getAttribute("data-id");
        if (idAttr && idAttr !== "undefined") {
            const id = Number(idAttr);
            showFilmDetail(id);
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
        else {
            console.error("ID олдсонгүй! films.js доторх film_id-г шалгана уу.");
        }
    }
});
