window.addEventListener('load', function(){
    if('geolocation' in navigator)
    navigator.geolocation.getCurrentPosition(geoposOK, geoposKO);
    else
    console.log('No tienes geolocalizador')
});

function geoposOK (pos){
    var lat = pos.coords.latitude;
    var long = pos.coords.longitude;
    console.log('Estás en la posición' + lat + ',' + long);
    console.log('https://maps.google.com/?q=' + lat + ',' + long);
    window.location.href = '/sugerenciasDeRevision/' + lat + ',' + long;
    }


function geoposKO(err){
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