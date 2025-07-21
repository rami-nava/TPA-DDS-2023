document.querySelectorAll('.btn-unirse').forEach(button => {
    button.addEventListener('click', function() {
        var comunidadId = this.getAttribute('data-comunidad-id');
        // Realizar una solicitud al servidor para eliminar el usuario
        fetch('/usuarios/unirseAComunidad/' + comunidadId, {
            method: 'POST'
        })
          .then(response => {
                    if (response.ok) {
                        window.location.href = '/comunidades';
                    } else {
                        window.alert('Error al unirse a comunidad');
                    }
                });
    });
});
