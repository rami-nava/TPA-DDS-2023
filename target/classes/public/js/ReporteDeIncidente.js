// Objeto para manejar los eventos relacionados con los selects
const SelectHandler = {
   init() {
       this.initSelects();
       this.initSubmitButton();
     },

   initSelects() {
    this.inputSelectEntidad = document.getElementById('selectEntidad');
    this.inputSelectEstablecimiento = document.getElementById('selectEstablecimiento');
    this.inputSelectServicio = document.getElementById('selectServicio');

    this.inputSelectEntidad.addEventListener('change', () => this.handleCambioSelectEntidad());
    this.inputSelectEstablecimiento.addEventListener('change', () => this.handleCambioSelectEstablecimiento());
  },

  handleCambioSelectEntidad() {
    const entidadSeleccionadaId = this.inputSelectEntidad.value;
    if (entidadSeleccionadaId !== "Seleccionar..." && entidadSeleccionadaId !== "-1") {

    fetch('/entidades/' + entidadSeleccionadaId + '/establecimientos')
        .then(response => response.json())
        .then(establecimientos => {
            // Limpiar el segundo select
            this.clearSelects([this.inputSelectEstablecimiento]);
            // Agregar opciones al segundo select
                establecimientos.forEach(establecimiento => {
                const option = document.createElement('option');
                option.value = establecimiento.id;
                option.textContent = establecimiento.nombre;
                this.inputSelectEstablecimiento.appendChild(option);
            });
        });
    } else {
      // Restablece los select 2 y 3
      this.clearSelects([this.inputSelectEstablecimiento, this.inputSelectServicio]);
    }
  },

  handleCambioSelectEstablecimiento() {
    const entidadSeleccionadaId = this.inputSelectEntidad.value;
    const establecimientoSeleccionadoId = this.inputSelectEstablecimiento.value;
        if(establecimientoSeleccionadoId!=="-1"){
        fetch('/entidades/' + entidadSeleccionadaId + '/establecimientos/' + establecimientoSeleccionadoId + '/servicios')
            .then(response => response.json())
            .then(servicios => {
                // Limpiar el tercer select
                   this.clearSelects([this.inputSelectServicio]);
                // Agregar opciones al tercer select
                    servicios.forEach(servicio => {
                    const option = document.createElement('option');
                    option.value = servicio.id;
                    option.textContent = servicio.nombre + ' ' + servicio.tipoNombre;
                    this.inputSelectServicio.appendChild(option);
                });
            });
    } else {
      // Restablece el select 3
      this.clearSelects([this.inputSelectServicio]);
    }
  },

  clearSelects(selectElements) {
    selectElements.forEach(select => {
      select.innerHTML = '';
      const option = document.createElement('option');
      option.value = -1;
      option.textContent = "Seleccionar...";
      select.appendChild(option);
    });
  },

    initSubmitButton() {
    const enviarButton = document.querySelector('.btn-cargarReporte');
    const camposInput = document.querySelectorAll(".input-field");

    // Verifica si todos los campos tienen valores
    function checkCampos() {
      let todosLosCamposLlenos = true;
      camposInput.forEach(function (campo) {
        if (!campo.value || campo.value == "Seleccionar..." || campo.value == "-1") {
          todosLosCamposLlenos = false;
        }
      });
      if (todosLosCamposLlenos) {
        enviarButton.removeAttribute("disabled");
      } else {
        enviarButton.setAttribute("disabled", "disabled");
      }
    }
    // Escucha eventos de cambio en los campos
    camposInput.forEach(function (campo) {
      campo.addEventListener("input", checkCampos);
    });
  },
};

// Inicializa el objeto SelectHandler al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  SelectHandler.init();
});

document.querySelector('.btn-cargarReporte').addEventListener('submit', function() {
    fetch('/reporteDeIncidentes/ABIERTO', {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {

        } else {
            window.alert('Error al crear reporte');
        }
    });
});

    const fechaYhoraInput = document.getElementById('fechaYhoraInput');
    const botonEnviar = document.getElementById('btn-cargarReporte');

    fechaYhoraInput.addEventListener('input', function () {
        const fechaYhoraActual = new Date().toISOString().slice(0, 16);
        const fechaYhoraSeleccionada = fechaYhoraInput.value;

        if (fechaYhoraSeleccionada >= fechaYhoraActual) {
            alert('Selecciona una fecha y hora pasada.');
            fechaYhoraInput.value = '';
        }

        // Desactiva el botón si la fecha es posterior
        botonEnviar.disabled = fechaYhoraSeleccionada >= fechaYhoraActual;
    });