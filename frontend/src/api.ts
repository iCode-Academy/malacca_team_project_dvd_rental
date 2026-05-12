// ── Interfaces — Spring Boot JPA API-ийн JSON хэлбэр ─────────────────────

export interface Film {
    film_id:     number;
    title:       string;
    rating:      string;
    rental_rate: number;
    description?: string;
}

export interface FilmDetail extends Film {
    rental_duration:  number;
    replacement_cost: number;
    special_features: string[];
}

export interface Actor {
    lastName: any;
    firstName: any;
    actor_id:   number;
    first_name: string;
    last_name:  string;
}

export interface Category {
    category_id: number;
    name:        string;
}

export interface CategoryStats extends Category {
    film_count:      number;
    avg_rental_rate: number;
    total_rentals:   number;
}

// ── Generic fetch helper ──────────────────────────────────────────────────

export const API_BASE = "http://localhost:8080";

export async function apiFetch<T>(path: string): Promise<T> {
    const res = await fetch(`${API_BASE}${path}`);
    if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`);
    return res.json() as Promise<T>;
}