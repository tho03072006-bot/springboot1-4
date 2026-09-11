document.querySelectorAll('form[data-confirm]').forEach(form => form.addEventListener('submit', event => {
    if (!window.confirm(form.dataset.confirm)) event.preventDefault();
}));
document.querySelectorAll('.topnav-links a').forEach(link => {
    const path=new URL(link.href).pathname;
    if (location.pathname===path || (!path.endsWith('/admin') && location.pathname.startsWith(path+'/'))) {
        link.classList.add('active'); link.setAttribute('aria-current','page');
    }
});
document.querySelectorAll('.password-toggle').forEach(btn => btn.addEventListener('click', () => {
    const input=document.getElementById(btn.dataset.toggleFor);
    if (!input) return;
    const show=input.type==='password';
    input.type=show?'text':'password';
    btn.textContent=show?'Ẩn':'Hiện';
}));
