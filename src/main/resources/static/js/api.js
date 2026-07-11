const API_BASE = '';

function getToken() {
    return localStorage.getItem('tm_token');
}

function saveToken(token) {
    localStorage.setItem('tm_token', token);
}

function clearToken() {
    localStorage.removeItem('tm_token');
}

function authHeaders() {
    const token = getToken();
    return {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    };
}

async function apiRequest(method, path, body = null) {
    const options = {
        method,
        headers: authHeaders()
    };
    if (body) options.body = JSON.stringify(body);

    const res = await fetch(API_BASE + path, options);

    if (res.status === 401) {
        clearToken();
        showAuth();
        throw new Error('Sessão expirada. Faça login novamente.');
    }

    return res;
}

async function apiGet(path) {
    return apiRequest('GET', path);
}

async function apiPost(path, body) {
    return apiRequest('POST', path, body);
}

async function apiPut(path, body) {
    return apiRequest('PUT', path, body);
}

async function apiDelete(path) {
    return apiRequest('DELETE', path);
}