document.querySelectorAll('.btn-eliminar').forEach(button => {
    button.addEventListener('click', function() {
        var comunidadId = this.getAttribute('data-comunidad-id');
        var resultado = window.confirm('¿Estás seguro? Esta acción es irreversible');

        if (resultado === true) {
                fetch('/comunidades/'+ comunidadId + '/eliminar', {
                    method: 'POST'
                })
                .then(response => {
                    if (response.ok) {
                       window.location.href = '/comunidades'
                    } else {
                        window.alert('Error al eliminar la comunidad');
                    }
                });
        }
        else {
         window.alert('Acción cancelada');
        }
    });
});
