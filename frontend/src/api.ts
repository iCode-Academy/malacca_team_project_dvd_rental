export interface Film {
    filmId:      number;
    title:       string;
    rating:      string;
    rentalRate:  number;
    description?: string;
}

export interface FilmDetail extends Film {
    rentalDuration:  number;
    replacementCost: number;
    specialFeatures: string | null;
    languageId:      number; 
    length?:         number;
}

export interface Actor {
    lastName:  string;
    firstName: string;
    actorId:   number;
}

export interface Category {
    categoryId: number;
    name:        string;
}

export interface CategoryStats extends Category {
    filmCount:      number;
    aveRentRate: number;
    totalRentals:   number;
}

export const API_BASE = "http://localhost:8080";

export async function apiFetch<T>(path: string): Promise<T> {
    const res = await fetch(`${API_BASE}${path}`);
    if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`);
    return res.json() as Promise<T>;
}