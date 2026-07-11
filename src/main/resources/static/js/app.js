// ── SCREEN CONTROL ────────────────────────────────────────────
function showAuth() {
    document.getElementById('auth-screen').classList.remove('hidden');
    document.getElementById('app-screen').classList.add('hidden');
}

function showApp() {
    document.getElementById('auth-screen').classList.add('hidden');
    document.getElementById('app-screen').classList.remove('hidden');
    loadTasks();
}

// ── THEME ──────────────────────────────────────────────────────
function toggleTheme() {
    const html = document.documentElement;
    const isDark = html.getAttribute('data-theme') === 'dark';
    const next = isDark ? 'light' : 'dark';
    html.setAttribute('data-theme', next);
    localStorage.setItem('tm_theme', next);
    document.getElementById('icon-moon').classList.toggle('hidden', !isDark);
    document.getElementById('icon-sun').classList.toggle('hidden', isDark);
}

function applyStoredTheme() {
    const stored = localStorage.getItem('tm_theme') || 'dark';
    document.documentElement.setAttribute('data-theme', stored);
    const isDark = stored === 'dark';
    document.getElementById('icon-moon').classList.toggle('hidden', !isDark);
    document.getElementById('icon-sun').classList.toggle('hidden', isDark);
}

// ── TOAST ──────────────────────────────────────────────────────
let toastTimer;
function showToast(msg, isError = false) {
    const el = document.getElementById('toast');
    el.textContent = msg;
    el.className = 'toast' + (isError ? ' error' : '');
    el.classList.remove('hidden');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => el.classList.add('hidden'), 2800);
}

// ── KEYBOARD ───────────────────────────────────────────────────
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeModal();
    if ((e.metaKey || e.ctrlKey) && e.key === 'Enter') {
        const overlay = document.getElementById('modal-overlay');
        if (!overlay.classList.contains('hidden')) saveTask();
    }
});

// ── INIT ───────────────────────────────────────────────────────
applyStoredTheme();

if (getToken()) {
    showApp();
} else {
    showAuth();
}