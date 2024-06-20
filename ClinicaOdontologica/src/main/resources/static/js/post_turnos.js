window.addEventListener('load', function () {
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
            odontologos.forEach(odontologo => {
                const option = document.createElement('option');
                option.value = odontologo.matricula;
                option.textContent = `${odontologo.nombre} ${odontologo.apellido}`;
                odontologoSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching odontologos:', error));

    //Al cargar la pagina buscamos y obtenemos el formulario donde estarán
    //los datos que se cargará del nuevo turno
    const formulario = document.querySelector('#add_new_turno');

    //Ante un submit del formulario se ejecutará la siguiente funcion
    formulario.addEventListener('submit', function (event) {

        //Hora para la API
        const horaInput = document.querySelector('#hora').value;
        const horaParaAPI = horaInput + ":00"; // Agregamos ":00" para completar el formato "HH:mm:ss"

        //creamos un JSON que tendrá los datos del nuevo turno
        const formData = {
            paciente:{
                cedula: document.querySelector('#cedula').value,
            },
            odontologo:{
                matricula: document.querySelector('#odontologo').value,
            },
            fecha: document.querySelector('#fecha').value,
            hora: horaParaAPI
        };
        //invocamos utilizando la función fetch la API turnos con el método POST que guardará
        //el turno que enviaremos en formato JSON
        const url = '/turnos';
        const settings = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        }

        fetch(url, settings)
            .then(response => response.json())
            .then(data => {
                //Si no hay ningun error se muestra un mensaje diciendo que el turno
                //se agrego bien
                let successAlert = '<div class="alert alert-success alert-dismissible">' +
                    '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                    '<strong></strong> Turno agregado </div>'

                document.querySelector('#response').innerHTML = successAlert;
                document.querySelector('#response').style.display = "block";
                resetUploadForm();

            })
            .catch(error => {
                //Si hay algun error se muestra un mensaje diciendo que el turno
                //no se pudo guardar y se intente nuevamente
                let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
                    '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                    '<strong> Error intente nuevamente</strong> </div>'

                document.querySelector('#response').innerHTML = errorAlert;
                document.querySelector('#response').style.display = "block";
                //se dejan todos los campos vacíos por si se quiere ingresar otro paciente
                resetUploadForm();})
    });


    function resetUploadForm(){
        document.querySelector('#cedula').value = "";
        document.querySelector('#odontologo').value = "";
        document.querySelector('#fecha').value = "";
        document.querySelector('#hora').value = "";
    }

    (function(){
        let pathname = window.location.pathname;
        if(pathname === "/"){
            document.querySelector(".nav .nav-item a:first").addClass("active");
        } else if (pathname == "/post_turnos.html") {
            document.querySelector(".nav .nav-item a:last").addClass("active");
        }
    })();
});