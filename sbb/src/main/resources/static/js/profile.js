const tabButtons = document.querySelectorAll('.tab-btn');
const tabContents = document.querySelectorAll('.tab-content');

tabButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        const target = btn.dataset.tab;

        tabButtons.forEach(b => {
            b.classList.remove('border-blue-600', 'font-medium');
            b.classList.add('text-gray-600');
        });

        btn.classList.add('border-blue-600', 'font-medium');
        btn.classList.remove('text-gray-600');

        tabContents.forEach(c => c.classList.add('hidden'));
        document.getElementById(`tab-${target}`).classList.remove('hidden');
    });
});
