document.addEventListener("DOMContentLoaded", () => {
  

  // Referencias a elementos del DOM
  const form = document.getElementById("formulario");
  const respuesta = document.getElementById("respuesta");
  const btnListar = document.getElementById("btnListar");                 //botón para listar candidatos
  const listaCandidatos = document.getElementById("listaCandidatos");
  const inputId = document.getElementById("inputIdCandidato");            //input para ingresar el id
  const btnBuscar = document.getElementById("btnBuscarCandidato");        //botón de buscar
  const btnGenerarReporte = document.getElementById("btnGenerarReporte"); //botón para generar reporte PDF


  // Maneja el envío del formulario para dar de alta un candidato
  form.addEventListener("submit", async (e) => {
    e.preventDefault();


    // Obtiene valores y elimina espacios en blanco
    const nombre = document.getElementById("nombre").value.trim();
    const apellido = document.getElementById("apellido").value.trim();
    const mail = document.getElementById("mail").value.trim();
    const institucionEducativa = document.getElementById("institucionEducativa").value.trim();
    const carrera = document.getElementById("carrera").value.trim();
    const promedioAcademico = document.getElementById("promedioAcademico").value.trim();


     // Validaciones básicas de campos obligatorios
     if (!nombre) {
    alert("El nombre es obligatorio");
    return;
    }

    if (!apellido) {
    alert("El apellido es obligatorio");
    return;
    }

    if (!mail) {
    alert("El correo electrónico es obligatorio");
    return;
    }

    if (!institucionEducativa) {
    alert("La institución educativa es obligatoria");
    return;
    }

    if (!carrera) {
    alert("La carrera es obligatoria");
    return;
    }

    if (!promedioAcademico) {
    alert("El promedio académico es obligatorio");
    return;
    }


    // Validar formato de correo electrónico
    const mailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!mailRegex.test(mail)) {
    alert("El correo electrónico no tiene un formato válido");
    return;
    }

    
    // Validar que el promedio académico sea un número entre 0 y 10
    const promedioNum = Number(promedioAcademico);
    if (
      !promedioAcademico ||
      isNaN(promedioNum) ||
      promedioNum < 0 ||
      promedioNum > 10
    ) {
      alert("El promedio académico debe ser un número entre 0 y 10");
      return;
    }


    // Construir objeto para enviar al backend
      const data = {
      nombreCompleto: nombre + " " + apellido,
      mail: mail,
      institucionEducativa: institucionEducativa,
      carrera: carrera,
      promedioAcademico: promedioNum,
      habilidades: document.getElementById("habilidades").value.trim(),
      experienciaLaboral: document.getElementById("experienciaLaboral").value.trim(),
    };


    // Enviar datos al servidor
    try {
      const res = await fetch("http://localhost:8080/candidatos/alta", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      });

      const text = await res.text();
      respuesta.innerText = "Respuesta del servidor: " + text;
      form.reset(); // limpia el formulario después de enviar
    } catch (error) {
      console.error(error);
      respuesta.innerText = "Error al enviar los datos";
    }
  });


    // Maneja el evento para listar todos los candidatos
    btnListar.addEventListener("click", async () => {
    try {
      const res = await fetch("http://localhost:8080/candidatos/lista");
      const candidatos = await res.json();

      listaCandidatos.innerHTML = "";

      if (candidatos.length === 0) {
        listaCandidatos.innerText = "No hay candidatos registrados.";
        return;
      }

      const ul = document.createElement("ul");
      candidatos.forEach(c => {
        const li = document.createElement("li");
        li.innerText = `${c.nombreCompleto} - ${c.institucionEducativa} - ${c.carrera} - Promedio: ${c.promedioAcademico}`;
        ul.appendChild(li);
      });
      listaCandidatos.appendChild(ul);
    } catch (error) {
      console.error(error);
      listaCandidatos.innerText = "Error al obtener la lista de candidatos";
    }
  });


    // Buscar candidato por ID
    btnBuscar.addEventListener("click", async () => {
    const id = inputId.value.trim();
    if (!id) {
      respuesta.innerText = "Por favor, ingresa un ID válido";
      return;
    }

    try {
      const res = await fetch(`http://localhost:8080/candidatos/${id}`);
      if (!res.ok) {
        respuesta.innerText = "Candidato no encontrado";
        return;
      }
      const candidato = await res.json();

      respuesta.innerText = `
      ID: ${candidato.id}
      Nombre Completo: ${candidato.nombreCompleto}
      Mail: ${candidato.mail}
      Institución Educativa: ${candidato.institucionEducativa}
      Carrera: ${candidato.carrera}
      Promedio Académico: ${candidato.promedioAcademico}
      Habilidades: ${candidato.habilidades}
      Experiencia Laboral: ${candidato.experienciaLaboral}
      `;
    } catch (error) {
      console.error(error);
      respuesta.innerText = "Error al obtener candidato";
    }
  });


    // Generar reporte PDF y abrirlo en nueva pestaña
    btnGenerarReporte.addEventListener("click", async () => {
    try {
      const res = await fetch("http://localhost:8080/candidatos/reporte");
      if (!res.ok) throw new Error("Error al generar el reporte");

      const blob = await res.blob(); // recibimos el PDF como blob
      const url = URL.createObjectURL(blob);

      window.open(url); // Abre nueva pestaña

    } catch (error) {
      console.error(error);
      alert("No se pudo generar el reporte");
    }
  });


    // Descargar archivo JSON con todos los candidatos
    const btnDescargarJSON = document.getElementById("btnDescargarJSON");

    btnDescargarJSON.addEventListener("click", async () => {
      try {
        const res = await fetch("http://localhost:8080/candidatos/json");
        if (!res.ok) throw new Error("No se pudo descargar el JSON");

        const blob = await res.blob();
        const url = URL.createObjectURL(blob);
        
        const a = document.createElement("a");
        a.href = url;
        a.download = "candidatos.json";
        document.body.appendChild(a);
        a.click();
        a.remove();

      } catch (error) {
        alert(error.message);
        console.error(error);
      }
    });

});