function switchTab(tab) {
    const isLogin = tab === 'login';

    document.getElementById('tab-login').classList.toggle('active', isLogin);
    document.getElementById('tab-register').classList.toggle('active', !isLogin);
    document.getElementById('form-login').classList.toggle('hidden', !isLogin);
    document.getElementById('form-register').classList.toggle('hidden', isLogin);

    clearAuthErrors();
}

function clearAuthErrors() {
    const loginErr = document.getElementById('login-error');
    const regErr   = document.getElementById('reg-error');
    loginErr.classList.add('hidden');
    loginErr.textContent = '';
    regErr.classList.add('hidden');
    regErr.textContent = '';
}

function showAuthError(id, msg) {
    const el = document.getElementById(id);
    el.textContent = msg;
    el.classList.remove('hidden');
}

async function handleLogin() {
    const email    = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;

    if (!email || !password) {
        showAuthError('login-error', 'Preencha e-mail e senha.');
        return;
    }

    try {
        const res = await apiPost('/auth/login', { email, password });
        if (!res.ok) {
            const data = await res.json().catch(() => ({}));
            showAuthError('login-error', data.message || 'E-mail ou senha inválidos.');
            return;
        }
        const data = await res.json();
        saveToken(data.token);
        showApp();
    } catch (e) {
        showAuthError('login-error', e.message || 'Erro ao conectar. Tente novamente.');
    }
}

async function handleRegister() {
    const name     = document.getElementById('reg-name').value.trim();
    const email    = document.getElementById('reg-email').value.trim();
    const password = document.getElementById('reg-password').value;

    if (!name || !email || !password) {
        showAuthError('reg-error', 'Preencha todos os campos.');
        return;
    }
    if (password.length < 6) {
        showAuthError('reg-error', 'A senha deve ter pelo menos 6 caracteres.');
        return;
    }

    try {
        const res = await apiPost('/auth/register', { name, email, password });
        if (!res.ok) {
            const data = await res.json().catch(() => ({}));
            showAuthError('reg-error', data.message || 'Erro ao criar conta. E-mail já cadastrado?');
            return;
        }
        const data = await res.json();
        saveToken(data.token);
        showApp();
    } catch (e) {
        showAuthError('reg-error', e.message || 'Erro ao conectar. Tente novamente.');
    }
}

function handleLogout() {
    clearToken();
    showAuth();
}