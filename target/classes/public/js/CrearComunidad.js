const form = document.getElementById("formCrearComunidad");
form.addEventListener('submit',crearComunidad);

function crearComunidad(event){
    event.preventDefault();

    const nombreInput= document.getElementById("nombre");
    const nombre = nombreInput.value;

    if(nombre !== null && nombre !== ""){
        fetch("/comunidades/nueva/" + nombre,{
            method: 'POST',
            body: JSON.stringify({nombre}),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response =>{
            if(response.ok) {
                window.location.href='/comunidades';
            }else if(response.status === 409){
                window.alert("Ese nombre de comunidad ya existe, elija otro.");
            }else{
                window.alert("No se pudo crear la comunidad, ocurrio un error.");
            }
        });
    }
}