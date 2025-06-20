# Simple_human (Sistema de  Registro de Candidatos)

Este programa permite registrar y gestionar candidatos en una base de datos. Las funcionalidades principales incluyen:
- Alta de nuevos candidatos
- Consulta de candidatos por ID
- Listado de todos los candidatos
- Generación de reportes en PDF
- Descarga de datos en formato JSON

### 1. Para utilizar el programa son necesarios los siguientes requisitos:

- **IDE(Backend):** Intellij IDEA
- **IDE(Frontend):** Visual Studio Code
- **Versión de Java:** 24
- **Gestor de dependencias:** Maven
- **Base de datos:** MySql
- **Extensión para Visual Studio Code:** Live Server

### 2. Configuración para Base de Datos

- **Usuario:** root
- **Contraseña:** root
- **Base de datos:** simple_human

**Script para crear la base:**

CREATE SCHEMA `simple_human` ;

CREATE TABLE `candidatos` (
  `idCandidatos` int NOT NULL AUTO_INCREMENT,
  `mail` char(50) NOT NULL,
  `nombreCompleto` varchar(50) NOT NULL,
  `institucionEducativa` varchar(50) NOT NULL,
  `carrera` varchar(50) NOT NULL,
  `promedioAcademico` decimal(3,1) NOT NULL,
  `habilidades` varchar(300) DEFAULT NULL,
  `experienciaLaboral` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`idCandidatos`),
  UNIQUE KEY `mailCandidatos_UNIQUE` (`mail`)
)

### 3. Cómo ejecutar el proyecto

**Backend (Spring Boot)**
- Abrí la carpeta del proyecto backend (simple_human) en IntelliJ IDEA.
- Ejecutá la clase **SimpleHumanApplication** ubicada en **org.candidatos.servicios.SimpleHumanApplication**.

**Frontend (HTML + JavaScript)**
- Abrí el archivo **index.html** con Visual Studio Code.
- Hacé clic derecho sobre el archivo y seleccioná **"Open with Live Server"** (necesitás tener instalada la extensión Live Server).
- Se te abre una pestaña con la interfaz del programa.
