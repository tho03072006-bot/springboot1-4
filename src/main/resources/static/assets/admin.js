document.querySelectorAll('form[data-confirm]').forEach(form => form.addEventListener('submit', event => {
    if (!window.confirm(form.dataset.confirm)) event.preventDefault();
}));
document.querySelectorAll('.sidebar nav a').forEach(link => {
    const path=new URL(link.href).pathname;
    if (location.pathname===path || (!path.endsWith('/admin') && location.pathname.startsWith(path+'/'))) {
        link.classList.add('active'); link.setAttribute('aria-current','page');
    }
});
