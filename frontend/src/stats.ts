import { apiFetch, CategoryStats } from "./api.js";

async function loadCategoryStats(): Promise<void> {
    const container = document.getElementById("stats-table")!;
    container.innerHTML = '<p class="loading">Loading...</p>';

    const stats = await apiFetch<CategoryStats[]>("/api/categories/stats");

    container.innerHTML = `
        <table>
            <thead>
                <tr>
                    <th>Category</th>
                    <th>Films</th>
                    <th>Avg Rental Rate</th>
                    <th>Total Rentals</th>
                </tr>
            </thead>
            <tbody>
                ${stats.map(s => `
                    <tr>
                        <td>${s.name}</td>
                        <td>${s.film_count}</td>
                        <td>$${s.avg_rental_rate.toFixed(2)}</td>
                        <td>${s.total_rentals.toLocaleString()}</td>
                    </tr>
                `).join("")}
            </tbody>
        </table>
    `;
}

document.getElementById("btn-load-stats")!
    .addEventListener("click", loadCategoryStats);