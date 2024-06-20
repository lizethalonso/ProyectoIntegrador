window.addEventListener('load', function () {


    //Buscamos y obtenemos el formulario donde estan
    //los datos que el usuario pudo haber modificado del turno
    const formulario = document.querySelector('#update_turno_form');

    formulario.addEventListener('submit', function (event) {
        let turnoId = document.querySelector('#turno_id').value;

        //Hora para la API
        const horaInput = document.querySelector('#hora').value;
        const horaParaAPI = horaInput + ":00"; // Agregamos ":00" para completar el formato "HH:mm:ss"
        //creamos un JSON que tendrá los datos del turno
        //a diferencia de un turno nuevo en este caso enviamos el id
        //para poder identificarlo y modificarlo para no cargarlo como nuevo
        const formData = {
            id: turnoId,
            paciente:{
                cedula:document.querySelector('#cedula').value,
            },
            odontologo:{
                matricula:document.querySelector('#odontologo').value,
            },
            fecha: document.querySelector('#fecha').value,
            hora: horaParaAPI
        };

        //invocamos utilizando la función fetch la API turnos con el método PUT que modificará
        //el paciente que enviaremos en formato JSON
        const url = '/turnos';
        const settings = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        }
        fetch(url,settings)
            .then(response => response.json())

    })
})

//Es la funcion que se invoca cuando se hace click sobre el id de un turno del listado
//se encarga de llenar el formulario con los datos del turno
//que se desea modificar
function findBy(id) {
    const url = '/turnos'+"/"+id;
    const settings = {
        method: 'GET'
    }
    fetch(url,settings)
        .then(response => response.json())
        .then(data => {
            let turno = data;
            document.querySelector('#turno_id').value = turno.id;
            document.querySelector('#cedula').value = turno.paciente.cedula;
            //document.querySelector('#matricula').value = turno.odontologo.matricula;
            document.querySelector('#fecha').value = turno.fecha;
            document.querySelector('#hora').value = turno.hora.slice(0, 5); // Mantener formato "HH:mm"
            //el formulario por default esta oculto y al editar se habilita
            document.querySelector('#div_turno_updating').style.display = "block";

            // Fetch odontologos from the API and populate the dropdown
            fetch('/odontologos')
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Network response was not ok: ${response.statusText}`);
                    }
                    return response.json();
                })
                .then(odontologos => {
                    const odontologoSelect = document.querySelector('#odontologo');
                    odontologoSelect.innerHTML = ''; // Limpiamos opciones previas
                    odontologos.forEach(odontologo => {
                        const option = document.createElement('option');
                        option.value = odontologo.matricula;
                        option.textContent = `${odontologo.nombre} ${odontologo.apellido}`;
                        if (odontologo.matricula === turno.odontologo.matricula) {
                            option.selected = true; // Seleccionamos el odontólogo del turno
                        }
                        odontologoSelect.appendChild(option);
                    });
                    // Mostrar el formulario de actualización
                    document.querySelector('#div_turno_updating').style.display = "block";
                })
                .catch(error => {
                    alert("Error fetching odontologos: " + error);
                });
        })
        .catch(error => {
            alert("Error: " + error);
        });

}