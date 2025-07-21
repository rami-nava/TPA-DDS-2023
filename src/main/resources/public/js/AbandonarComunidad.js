document.getElementById('abandonarComunidad').addEventListener('click', function() {
    var comunidadId = this.getAttribute('data-comunidad-id');
    // Realizar una solicitud al servidor para abandonar la comunidad
    fetch('/usuarios/abandonarComunidad/' +comunidadId, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/comunidades';
        } else {
            window.alert('Error al abandonar comunidad');
        }
    });
});

