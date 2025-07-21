class IncidentHandler {
  constructor() {
    this.inputEstado = document.getElementById('inputEstado');
    this.inputComunidad = document.getElementById('inputComunidad');

    this.inputEstado.addEventListener('change', () => this.handleEstadoChange());
    this.inputComunidad.addEventListener('change', () => this.handleComunidadChange());
  }

  handleEstadoChange() {
    const estado = this.inputEstado.value;
    const comunidadId = this.inputComunidad.value;

    if (estado !== "Seleccionar...") {
      let url = '/incidentes?estado=' + estado;
      if (comunidadId !== "Seleccionar...") {
        url += '&comunidad=' + comunidadId;
      }
      this.fetchA(url);
    }
    else{
    let url = '/incidentes';
        if (comunidadId !== "Seleccionar...") {
            url += '?comunidad=' + comunidadId;
        }
    this.fetchA(url);
  }
  }

  handleComunidadChange() {
    const estado = this.inputEstado.value;
    const comunidadId = this.inputComunidad.value;

    if (comunidadId !== "Seleccionar...") {
      let url = '/incidentes?comunidad=' + comunidadId;

      if (estado !== "Seleccionar...") {
        url += '&estado=' + estado;
      }
      this.fetchA(url);
    }
    else{
        let url = '/incidentes';
        if (estado !== "Seleccionar...") {
            url += '?estado=' + estado;
        }
        this.fetchA(url);
  }}

  fetchA(url) {
    fetch(url)
      .then(response => {
        if (response.ok) {
          window.location.href = url;
        } else {
          window.alert('Ocurrió un error');
        }
      });
  }
}

// Inicializar el manejador de incidentes
const incidentHandler = new IncidentHandler();

document.querySelector('.btn-cerrar-incidente').addEventListener('click', function() {
    var incidenteId = this.getAttribute('data-incidente-id');
    var comunidadId = this.getAttribute('data-comunidad-id');
    fetch('/reporteDeIncidentes/CERRADO/' + incidenteId + '/' + comunidadId, {
        method: 'POST'
    })
    .then(response => {
            if (response.ok) {
                window.location.href='/incidentes?estado=ABIERTO&comunidad=' + comunidadId;
            } else {
                window.alert('Error al cerrar incidente');
            }
    });
});