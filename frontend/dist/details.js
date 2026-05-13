import { apiFetch } from "./api.js";
async function showFilmDetail(id) {
    const panel = document.getElementById("detail-panel");
    panel.style.display = "block";
    panel.innerHTML = '<p class="loading">Loading detail...</p>';
    const film = await apiFetch(`/api/films/${id}`);
    panel.innerHTML = `
        <button onclick="document.getElementById('detail-panel').style.display='none'">✕ Хаах</button>
        <h3><span class="rating">${film.rating}</span> ${film.title}</h3>
        <p>${film.description ?? "Тайлбар байхгүй."}</p>
        <table>
            <tr><th>Rental Rate</th>  <td>$${film.rentalRate.toFixed(2)}</td></tr>
            <tr><th>Duration</th>     <td>${film.rental_duration} days</td></tr>
            <tr><th>Replace Cost</th> <td>$${film.replacement_cost.toFixed(2)}</td></tr>
            <tr><th>Features</th>     <td>${film.special_features?.join(", ") ?? "—"}</td></tr>
        </table>
    `;
}
// Film card дарахад — event delegation ашиглана
document.getElementById("film-list")
    .addEventListener("click", (e) => {
    const card = e.target.closest(".film-card");
    if (!card)
        return;
    const id = Number(card.getAttribute("data-id"));
    if (id)
        showFilmDetail(id);
});
