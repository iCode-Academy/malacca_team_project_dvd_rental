import { apiFetch, Film, FilmDetail, API_BASE } from "./api.js";

// ── POST /api/films ───────────────────────────────────────────
async function createFilm(data: any): Promise<FilmDetail> {
    const res = await fetch(`${API_BASE}/api/films`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        console.error("Серверийн алдаа (POST):", errorData);
        throw new Error(`Create failed: ${res.status}`);
    }
    return res.json();
}

// ── PUT /api/films/{id} ──────────────────────────────────────
async function updateFilm(id: number, data: any): Promise<FilmDetail> {
    const res = await fetch(`${API_BASE}/api/films/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        console.error("Серверийн алдаа (PUT):", errorData);
        throw new Error(`Update failed: ${res.status}`);
    }
    return res.json();
}

async function deleteFilm(id: number): Promise<void> {
    const res = await fetch(`${API_BASE}/api/films/${id}`, {
        method: "DELETE"
    });
    if (!res.ok) throw new Error(`Delete failed: ${res.status}`);
}

// ── Rendering ────────────────────────────────────────────────────────
function renderFilmsWithCRUD(films: Film[], container: HTMLElement): void {
    let html = "";
    // Таны дуртай for loop
    for (let i = 0; i < films.length; i++) {
        const f = films[i];
        html += `
        <div class="film-card" data-id="${f.filmId}" style="border:1px solid #ddd; padding:10px; margin:5px; border-radius:5px;">
            <span class="rating">${f.rating}</span>
            <strong>${f.title}</strong> - <span style="color:green">$${f.rentalRate}</span>
            <hr>
            <button class="btn-edit" data-id="${f.filmId}">✏️ Засах</button>
            <button class="btn-delete" data-id="${f.filmId}" style="color:red">🗑 Устгах</button>

            <div id="edit-form-${f.filmId}" style="display:none; margin-top:10px; background:#f9f9f9; padding:10px;">
                <input id="edit-title-${f.filmId}" value="${f.title}">
                <select id="edit-rating-${f.filmId}">
                    <option value="G" ${f.rating === 'G' ? 'selected' : ''}>G</option>
                    <option value="PG" ${f.rating === 'PG' ? 'selected' : ''}>PG</option>
                    <option value="PG-13" ${f.rating === 'PG-13' ? 'selected' : ''}>PG-13</option>
                    <option value="R" ${f.rating === 'R' ? 'selected' : ''}>R</option>
                    <option value="NC-17" ${f.rating === 'NC-17' ? 'selected' : ''}>NC-17</option>
                </select>
                <input id="edit-rate-${f.filmId}" value="${f.rentalRate}" type="number" step="0.01">
                <button class="btn-save" data-id="${f.filmId}">💾 Хадгалах</button>
                <button class="btn-cancel" data-id="${f.filmId}">Болих</button>
            </div>
        </div>`;
    }
    container.innerHTML = html;

    // "for" loop ашиглан Event Listeners оноох
    const editBtns = container.querySelectorAll(".btn-edit");
    for (let i = 0; i < editBtns.length; i++) {
        editBtns[i].addEventListener("click", (e) => {
            e.stopPropagation(); // Detail panel нээгдэхээс сэргийлнэ
            const id = editBtns[i].getAttribute("data-id");
            (document.getElementById(`edit-form-${id}`) as HTMLElement).style.display = "block";
        });
    }

    const saveBtns = container.querySelectorAll(".btn-save");
    for (let i = 0; i < saveBtns.length; i++) {
        saveBtns[i].addEventListener("click", async (e) => {
            e.stopPropagation();
            const id = Number(saveBtns[i].getAttribute("data-id"));
            const title = (document.getElementById(`edit-title-${id}`) as HTMLInputElement).value;
            const rating = (document.getElementById(`edit-rating-${id}`) as HTMLSelectElement).value;
            const rate = Number((document.getElementById(`edit-rate-${id}`) as HTMLInputElement).value);

            try {
                await updateFilm(id, {
                    title: title,
                    rating: rating,
                    rentalRate: rate,
                    rentalDuration: 3,
                    replacementCost: 19.99,
                    languageId: 1
                });
                await loadAndRender();
            } catch (err) {
                alert("Шинэчлэхэд алдаа гарлаа. Postgres ENUM эсвэл Constraints шалгана уу.");
            }
        });
    }

    const deleteBtns = container.querySelectorAll(".btn-delete");
    for (let i = 0; i < deleteBtns.length; i++) {
        deleteBtns[i].addEventListener("click", async (e) => {
            e.stopPropagation();
            if (confirm("Та энэ киног устгахдаа итгэлтэй байна уу?")) {
                const id = Number(deleteBtns[i].getAttribute("data-id"));
                try {
                    await deleteFilm(id);
                    await loadAndRender();
                } catch (err) {
                    alert("Устгах боломжгүй! Энэ кино Inventory эсвэл Rental-тай холбоотой байна.");
                }
            }
        });
    }

    const cancelBtns = container.querySelectorAll(".btn-cancel");
    for (let i = 0; i < cancelBtns.length; i++) {
        cancelBtns[i].addEventListener("click", (e) => {
            e.stopPropagation();
            const id = cancelBtns[i].getAttribute("data-id");
            (document.getElementById(`edit-form-${id}`) as HTMLElement).style.display = "none";
        });
    }
}

// ── Initialization ───────────────────────────────────────────────────

async function loadAndRender(): Promise<void> {
    const listDiv = document.getElementById("film-crud-list");
    if (!listDiv) return;
    try {
        const films = await apiFetch<Film[]>("/api/films?page=1&size=20");
        renderFilmsWithCRUD(films, listDiv);
    } catch (err) {
        listDiv.innerHTML = "<p style='color:red'>Сервертэй холбогдож чадсангүй.</p>";
    }
}

async function initCreateForm(): Promise<void> {
    const createBtn = document.getElementById("btn-create-film");
    if (!createBtn) return;

    createBtn.addEventListener("click", async () => {
        const titleEl = document.getElementById("create-film-title") as HTMLInputElement;
        const ratingEl = document.getElementById("create-film-rating") as HTMLInputElement; // HTML дээр <select> болгохыг зөвлөж байна
        const rateEl = document.getElementById("create-film-rate") as HTMLInputElement;
        const durationEl = document.getElementById("create-film-duration") as HTMLInputElement;

        if (!titleEl.value) {
            alert("Гарчиг заавал оруулна уу!");
            return;
        }

        try {
            const filmToSave = {
                title: titleEl.value,
                description: "New film created from web",
                rating: ratingEl.value || "G", // Postgres mpaa_rating ENUM-д тааруулж "G" өгөв
                rentalRate: Number(rateEl.value) || 4.99,
                rentalDuration: Number(durationEl.value) || 3,
                replacementCost: 19.99,
                languageId: 1, // Sakila Postgres заавал нэхдэг
                specialFeatures: "Trailers"
            };

            await createFilm(filmToSave);
            titleEl.value = "";
            await loadAndRender();
            alert("Амжилттай нэмэгдлээ!");
        } catch (err) {
            alert("Нэмэхэд алдаа гарлаа. 'G', 'PG', 'R' гэх мэт rating ашиглана уу.");
        }
    });
}

initCreateForm();
loadAndRender();