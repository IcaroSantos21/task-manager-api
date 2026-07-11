let allTasks = [];
let currentView = 'all';
let editingTaskId = null;

const VIEW_META = {
    all:         { title: 'Todas as tarefas',  subtitle: 'Gerencie todas as suas tarefas' },
    PENDING:     { title: 'Pendentes',         subtitle: 'Tarefas aguardando início' },
    IN_PROGRESS: { title: 'Em progresso',      subtitle: 'Tarefas em andamento' },
    COMPLETED:   { title: 'Concluídas',        subtitle: 'Tarefas finalizadas' }
};

const STATUS_LABELS = {
    PENDING:     'Pendente',
    IN_PROGRESS: 'Em progresso',
    COMPLETED:   'Concluída'
};

async function loadTasks() {
    setTasksLoading();
    try {
        const res = await apiGet('/tasks');
        if (!res.ok) throw new Error();
        allTasks = await res.json();
        updateCounts();
        renderTasks();
    } catch {
        setTasksError();
    }
}

function setTasksLoading() {
    document.getElementById('tasks-container').innerHTML = `
    <div class="loading-state">
      <div class="spinner"></div>
      <span>Carregando tarefas...</span>
    </div>`;
}

function setTasksError() {
    document.getElementById('tasks-container').innerHTML = `
    <div class="empty-state">
      <div class="empty-icon">⚠️</div>
      <p>Não foi possível carregar as tarefas.<br>Verifique sua conexão e tente novamente.</p>
    </div>`;
}

function updateCounts() {
    document.getElementById('count-all').textContent     = allTasks.length;
    document.getElementById('count-pending').textContent  = allTasks.filter(t => t.status === 'PENDING').length;
    document.getElementById('count-progress').textContent = allTasks.filter(t => t.status === 'IN_PROGRESS').length;
    document.getElementById('count-done').textContent     = allTasks.filter(t => t.status === 'COMPLETED').length;
}

function setView(view, btn) {
    document.querySelectorAll('.mobile-nav-item').forEach(b => b.classList.remove('active'));
    currentView = view;

    document.querySelectorAll('.nav-item').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    const meta = VIEW_META[view] || VIEW_META['all'];
    document.getElementById('view-title').textContent    = meta.title;
    document.getElementById('view-subtitle').textContent = meta.subtitle;

    renderTasks();
}

function renderTasks() {
    const filtered = currentView === 'all'
        ? allTasks
        : allTasks.filter(t => t.status === currentView);

    const container = document.getElementById('tasks-container');

    if (!filtered.length) {
        container.innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">✓</div>
        <p>Nenhuma tarefa encontrada.<br>Crie uma nova tarefa para começar.</p>
      </div>`;
        return;
    }

    container.innerHTML = `
    <div class="tasks-list">
      ${filtered.map(taskCard).join('')}
    </div>`;
}

function taskCard(t) {
    const isDone = t.status === 'COMPLETED';
    const date   = t.createdAt
        ? new Date(t.createdAt).toLocaleDateString('pt-BR', { day: '2-digit', month: 'short' })
        : '';

    return `
    <div class="task-card status-${t.status}" id="card-${t.id}">
      <div class="task-check ${isDone ? 'done' : ''}" onclick="toggleDone(${t.id}, '${t.status}')">
        <svg viewBox="0 0 12 12" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="2 6 5 9 10 3"/>
        </svg>
      </div>
      <div class="task-body">
        <div class="task-title">${escHtml(t.title)}</div>
        ${t.description ? `<div class="task-desc">${escHtml(t.description)}</div>` : ''}
        <div class="task-meta">
          <span class="badge badge-${t.status}">${STATUS_LABELS[t.status]}</span>
          ${date ? `<span class="task-date">${date}</span>` : ''}
        </div>
      </div>
      <div class="task-actions">
        <button class="btn-icon" title="Editar" onclick="openEditModal(${t.id})">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
          </svg>
        </button>
        <button class="btn-icon danger" title="Deletar" onclick="deleteTask(${t.id})">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="3 6 5 6 21 6"/>
            <path d="M19 6l-1 14H6L5 6"/>
            <path d="M10 11v6"/><path d="M14 11v6"/>
            <path d="M9 6V4h6v2"/>
          </svg>
        </button>
      </div>
    </div>`;
}

function openModal() {
    editingTaskId = null;
    document.getElementById('modal-title-text').textContent = 'Nova tarefa';
    document.getElementById('modal-task-id').value  = '';
    document.getElementById('modal-title').value    = '';
    document.getElementById('modal-desc').value     = '';
    document.getElementById('modal-status').value   = 'PENDING';
    document.getElementById('modal-overlay').classList.remove('hidden');
    document.getElementById('modal-title').focus();
}

function openEditModal(id) {
    const task = allTasks.find(t => t.id === id);
    if (!task) return;

    editingTaskId = id;
    document.getElementById('modal-title-text').textContent = 'Editar tarefa';
    document.getElementById('modal-task-id').value  = id;
    document.getElementById('modal-title').value    = task.title;
    document.getElementById('modal-desc').value     = task.description || '';
    document.getElementById('modal-status').value   = task.status;
    document.getElementById('modal-overlay').classList.remove('hidden');
    document.getElementById('modal-title').focus();
}

function closeModal() {
    document.getElementById('modal-overlay').classList.add('hidden');
    editingTaskId = null;
}

function closeModalOnOverlay(e) {
    if (e.target === document.getElementById('modal-overlay')) closeModal();
}

async function saveTask() {
    const title  = document.getElementById('modal-title').value.trim();
    const desc   = document.getElementById('modal-desc').value.trim();
    const status = document.getElementById('modal-status').value;

    if (!title) { showToast('Informe o título da tarefa.', true); return; }

    const body = { title, description: desc || null, status };

    try {
        let res;
        if (editingTaskId) {
            res = await apiPut(`/tasks/${editingTaskId}`, body);
        } else {
            res = await apiPost('/tasks', body);
        }

        if (!res.ok) throw new Error();
        closeModal();
        showToast(editingTaskId ? 'Tarefa atualizada!' : 'Tarefa criada!');
        await loadTasks();
    } catch {
        showToast('Erro ao salvar tarefa. Tente novamente.', true);
    }
}

async function toggleDone(id, currentStatus) {
    const newStatus = currentStatus === 'COMPLETED' ? 'PENDING' : 'COMPLETED';
    const task = allTasks.find(t => t.id === id);
    if (!task) return;

    try {
        const res = await apiPut(`/tasks/${id}`, {
            title: task.title,
            description: task.description || null,
            status: newStatus
        });
        if (!res.ok) throw new Error();
        await loadTasks();
    } catch {
        showToast('Erro ao atualizar tarefa.', true);
    }
}

async function deleteTask(id) {
    try {
        const res = await apiDelete(`/tasks/${id}`);
        if (!res.ok) throw new Error();
        showToast('Tarefa removida.');
        await loadTasks();
    } catch {
        showToast('Erro ao remover tarefa.', true);
    }
}

function escHtml(s) {
    return String(s || '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}