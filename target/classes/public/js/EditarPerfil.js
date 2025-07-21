const confirmarBoton = document.querySelector('.btn-editar-perfil');
confirmarBoton.addEventListener("click", function(event){
    checkCampos(event);
});

function checkCampos(event) {
      const camposObligatorios = document.querySelectorAll(".obligatorio");
      let todosLosCamposLlenos = true;
      camposObligatorios.forEach(function (campo) {
        if (!campo.value.trim()) { //trim para evitar los espacios en blanco
            todosLosCamposLlenos = false;
        }
    });

    if(!todosLosCamposLlenos)
    {
        event.preventDefault();
        window.alert("Debes completar todos los campos")
    }
};

function selectMedioDeComunicacion(){
    const select = document.getElementById("medioDeComunicacionSelect")
    const medioSeleccionado = select.getAttribute("data-medio-seleccionado")
    const opciones = select.getElementsByTagName('option');

    for(let i = 0; i < opciones.length; i++)
    {
        if(opciones[i].value === medioSeleccionado)
        {
            opciones[i].selected = true;
            break;
        }
    }
}

function selectFormaDeNotificar(){
    const select = document.getElementById("formaDeNotificarSelect")
    const formaSeleccionada = select.getAttribute("data-forma-seleccionada")
    const opciones = select.getElementsByTagName('option');

    for(let i = 0; i < opciones.length; i++)
    {
        if(opciones[i].value === formaSeleccionada)
        {
            opciones[i].selected = true;
            break;
        }
    }
}
function error(){
    const infError = document.getElementById("infError");
    const hayError = infError.getAttribute("data-hay-error");
    const mensajeDeError = infError.getAttribute("data-mensaje-error");

    if(hayError){
        window.alert(mensajeDeError);
    }
}

selectFormaDeNotificar();
selectMedioDeComunicacion();
error();
