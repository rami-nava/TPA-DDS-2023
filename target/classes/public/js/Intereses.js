document.querySelectorAll('.btn-nuevo-interes').forEach(button => {
    button.addEventListener('click', function() {
        var organismoId = this.getAttribute('data-organismo-id');
        var entidadPrestadoraId = this.getAttribute('data-entidadPrestadora-id');
        var entidadId = this.getAttribute('data-entidad-id');

        fetch('/intereses/entidad/' + entidadId)
            .then(response => response.json())
            .then(interes => {
                if (!interes) {
                    fetch('/intereses/entidad/'+ entidadId +'/agregar' , {
                        method: 'POST'
                    })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = '/organismosDeControl/' + organismoId + '/entidadesPrestadoras/' + entidadPrestadoraId + '/entidades';
                        } else {
                            window.alert('Error al definir nuevo interés');
                        }
                    });
                } else {
                    window.alert("Ya es de tu interes esta entidad");
                }
            });
    });
});

document.querySelectorAll('.btn-nuevo-interesServicio').forEach(button => {
    button.addEventListener('click', function() {
        var organismoId = this.getAttribute('data-organismo-id');
        var entidadPrestadoraId = this.getAttribute('data-entidadPrestadora-id');
        var entidadId = this.getAttribute('data-entidad-id');
        var establecimientoId = this.getAttribute('data-establecimiento-id');
        var servicioId = this.getAttribute('data-servicio-id');
        var rol = this.getAttribute('data-rol');

        fetch('/intereses/servicio/' + servicioId)
            .then(response => response.json())
            .then(interes => {
                if (!interes) {
                        fetch('/intereses/servicio/'+ servicioId +'/agregar/' + rol, {
                        method: 'POST'
                        })
                        .then(response => {
                            if (response.ok) {
                                window.location.href = '/organismosDeControl/' + organismoId + '/entidadesPrestadoras/' + entidadPrestadoraId + '/entidades/' + entidadId + '/establecimientos/' + establecimientoId + '/servicios';
                            } else {
                                window.alert('Error al definir nuevo interés');
                            }
                        });
                } else {
                    window.alert("Ya es de tu interés este servicio");
                }
            });
    });
});

document.querySelectorAll('.btn-eliminar-entidadInteres').forEach(button => {
    button.addEventListener('click', function() {
        var entidadId = this.getAttribute('data-entidad-id');

                    fetch('/intereses/entidad/'+ entidadId +'/eliminar', {
                        method: 'POST'
                    })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = window.location.pathname;
                        } else {
                            window.alert('Error al eliminar interés');
                        }
                    });
    });
});

document.querySelectorAll('.btn-eliminar-parInteres').forEach(button => {
    button.addEventListener('click', function() {
        var servicioId = this.getAttribute('data-servicio-id');

                    fetch('/intereses/servicio/'+ servicioId +'/eliminar', {
                        method: 'POST'
                    })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = window.location.pathname;
                        } else {
                            window.alert('Error al eliminar interés');
                        }
                    });
    });
});

document.querySelectorAll('.btn-eliminar-servicioInteres').forEach(button => {
    button.addEventListener('click', function() {
        var servicioId = this.getAttribute('data-servicio-id');

                    fetch('/servicio/'+ servicioId +'/eliminar', {
                        method: 'POST'
                    })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = window.location.pathname;
                        } else {
                            window.alert('Error al eliminar interés');
                        }
                    });
    });
});

document.querySelectorAll('.btn-cambiar-rol-interes').forEach(button => {
    button.addEventListener('click', function() {
        var servicioId = this.getAttribute('data-servicio-id');
                        fetch('/intereses/servicio/'+ servicioId +'/cambiarRol', {
                            method: 'POST'
                        })
                        .then(response => {
                            if (response.ok) {
                                window.location.href = window.location.pathname;
                            } else {
                                window.alert('Error al cambiar el rol');
                            }
                        });
            });
    });

document.querySelectorAll('.btn-cambiar-rol-servicio').forEach(button => {
    button.addEventListener('click', function() {
        var servicioId = this.getAttribute('data-servicio-id');
                        fetch('/servicio/'+ servicioId +'/cambiarRol', {
                            method: 'POST'
                        })
                        .then(response => {
                            if (response.ok) {
                                window.location.href = window.location.pathname;
                            } else {
                                window.alert('Error al cambiar el rol');
                            }
                        });
            });
    });
