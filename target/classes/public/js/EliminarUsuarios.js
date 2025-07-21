document.querySelectorAll('.btn-eliminar').forEach(button => {
    button.addEventListener('click', function() {
        var miembroId = this.getAttribute('data-miembro-id');
        var resultado = window.confirm('¿Estás seguro? Esta acción es irreversible');
        if (resultado === true) {

            fetch('/usuarios/'+ miembroId +'/eliminar', {
                        method: 'POST'
                    })
                    .then(response => {
                        console.log(response);
                        if (response.ok) {
                            window.location.href = 'usuarios';
                        } else {
                            window.alert('Error al eliminar usuario');
                        }
                    });
        } else {
            window.alert('Acción cancelada');
        }
    });
});
