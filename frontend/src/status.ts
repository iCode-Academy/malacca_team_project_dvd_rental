const statusEl = document.getElementById("global-status")!;

export function showLoading(message = "Loading..."): void {
    statusEl.textContent = message;
    statusEl.style.display  = "block";
    statusEl.style.background = "#f0f0f0";
    statusEl.style.color      = "#333";
}

export function showSuccess(message: string): void {
    statusEl.textContent = message;
    statusEl.style.display  = "block";
    statusEl.style.background = "#d4edda";
    statusEl.style.color      = "#155724";
    setTimeout(() => { statusEl.style.display = "none"; }, 3000);
}

export function showError(message: string): void {
    statusEl.textContent = `❌ ${message}`;
    statusEl.style.display  = "block";
    statusEl.style.background = "#f8d7da";
    statusEl.style.color      = "#721c24";
}

// Global error handler — catch хийгдээгүй Promise rejection барина
window.addEventListener("unhandledrejection", (e) => {
    showError(`Алдаа: ${e.reason}`);
});