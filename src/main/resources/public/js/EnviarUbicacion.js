window.addEventListener('DOMContentLoaded', function(){
    if('geolocation' in navigator)
    navigator.geolocation.getCurrentPosition(posicionObtenida, posicionNoObtenida);
    else
    console.log('No tienes geolocalizador')
});

function posicionObtenida (pos){
    var lat = pos.coords.latitude;
    var long = pos.coords.longitude;

      fetch('/sugerenciasDeRevision/' + lat + ',' + long + '/notificacion')
         .then(response => response.text())
                .then(data => {
                    if (data === 'POSITIVO') {
                    var resultado = window.confirm('¡Tienes una solicitud de revisión de incidente!');
                    if (resultado === true) {
                           window.location.href = '/sugerenciasDeRevision'
                    }
                    else{
                     window.alert('Has rechazado la solicitud de revisión');
                    }
                    }
                    }
                )
      .catch(error => {
        console.error('Error en el envío de la ubicación:', error);
      });
}


function posicionNoObtenida(err){
    const mensaje = document.getElementById('mensaje');

    console.warn(err.message);
    switch(err.code) {
        case err.PERMISSION_DENIED:
            mensaje.innerText = "No diste permiso para obtener tu posición"; break;
        case err.POSITION_UNAVAILABLE:
            mensaje.innerText = "Tu posición actual no está disponible"; break;
        case err.TIMEOUT:
            mensaje.innerText = "No se ha podido obtener tu posición en un tiempo prudencial"; break;
        default:
            mensaje.innerText = "Error desconocido";
    break;
}
}