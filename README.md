# Eventos e Inscripciones

## Descripción

Aplicación web desarrollada para administrar eventos, actividades, inscripciones y asistencia de participantes. Permite gestionar la información de los eventos, controlar los cupos disponibles, administrar listas de espera y consultar estadísticas de participación.

El proyecto busca facilitar la organización y administración de eventos mediante una interfaz sencilla, intuitiva y adaptable a diferentes dispositivos.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Vaadin 25
- Maven
- Archivos de texto (.txt) para la persistencia de datos.

## Funcionalidades principales

### 1. Gestión de eventos

- Crear eventos.
- Consultar eventos registrados.
- Editar información de los eventos.
- Eliminar eventos.
- Consultar el estado de los eventos.

### 2. Gestión de actividades

- Crear actividades relacionadas con un evento.
- Consultar actividades.
- Editar actividades.
- Eliminar actividades.
- Establecer fechas y horarios de inicio y finalización.
- Validar que no existan cruces de horarios.

### 3. Gestión de inscripciones

- Registrar participantes en los eventos.
- Consultar participantes inscritos.
- Controlar la capacidad máxima de los eventos.
- Evitar inscripciones duplicadas.
- Administrar cancelaciones.
- Gestionar la lista de espera.

### 4. Gestión de asistencias

- Registrar la asistencia de los participantes a las actividades.
- Consultar los registros de asistencia.
- Actualizar la información de asistencia.

### 5. Estadísticas

- Consultar información sobre la ocupación de los eventos.
- Visualizar porcentajes de asistencia.
- Consultar información general sobre la participación.

### 6. Generación de certificados

- Consultar los participantes de cada evento.
- Calcular el porcentaje de asistencia.
- Generar certificados HTML para participantes que cumplan con el mínimo del 80% de asistencia.
- Descargar los certificados generados.

## Bonificaciones implementadas

### 1. Promoción automática de lista de espera

Cuando un evento alcanza su capacidad máxima, los nuevos participantes quedan registrados en lista de espera.

Cuando se cancela la inscripción de un participante que tiene un cupo confirmado, el sistema libera ese espacio y promociona automáticamente al primer participante de la lista de espera.

También se registra la fecha en la que se realiza la promoción.

**Cómo probar esta funcionalidad:**

1. Ingresar a la sección **Eventos** y seleccionar un evento que tenga una capacidad limitada.
2. Ingresar a la sección **Inscripciones**.
3. Registrar participantes hasta completar la capacidad máxima del evento.
4. Registrar un participante adicional para comprobar que queda en lista de espera.
5. Cancelar la inscripción de uno de los participantes que tenga un cupo confirmado.
6. Verificar que el primer participante de la lista de espera sea promocionado automáticamente.
7. Comprobar que su estado cambie a inscrito.
8. Verificar que se registre la fecha de promoción correspondiente.

### 2. Certificados de participación

El sistema permite generar certificados en formato HTML para los participantes que hayan asistido como mínimo al 80% de las actividades del evento.

El certificado contiene el nombre del participante, el nombre del evento, la fecha de realización y las horas asistidas.

**Cómo probar esta funcionalidad:**

1. Ingresar a la sección **Eventos** y seleccionar un evento que tenga actividades creadas.
2. Ingresar a la sección **Inscripciones** y registrar los participantes.
3. Ingresar a la sección **Asistencias** y seleccionar una actividad del evento.
4. Registrar la asistencia de los participantes, indicando quiénes asistieron y quiénes no.
5. Repetir el procedimiento con las demás actividades.
6. Asegurarse de que al menos un participante tenga una asistencia igual o superior al 80% y otro tenga un porcentaje inferior.
7. Ingresar a la sección **Certificados** y seleccionar el evento correspondiente.
8. Seleccionar al participante que cumple con el requisito de asistencia.
9. Presionar el botón **Generar certificado**.
10. Utilizar el enlace **Descargar certificado HTML** para obtener el documento.
11. Abrir el archivo descargado en un navegador y verificar la información del certificado.
12. Seleccionar al participante que no cumple el requisito y comprobar que el sistema no permita generar su certificado.

## Cómo ejecutar el proyecto

### Requisitos previos

- Tener instalado Java 21.
- Tener instalado Git (si se desea clonar el repositorio).
- Contar con un editor de código como Visual Studio Code o IntelliJ IDEA.

### Pasos para ejecutar

1. Descargar o clonar el repositorio:

```bash
git clone https://github.com/MatiazOrtiz/eventos-inscripciones.git
```

2. Abrir la carpeta del proyecto en Visual Studio Code o IntelliJ IDEA.

3. Abrir una terminal en la carpeta principal del proyecto.

4. Ejecutar el siguiente comando:

```powershell
.\mvnw.cmd spring-boot:run
```

5. Esperar a que la aplicación termine de iniciar.

6. Abrir el navegador e ingresar a:

```text
http://localhost:8080
```

## Manual de usuario

La aplicación cuenta con un menú lateral que permite navegar entre las diferentes secciones.

- **Eventos:** permite crear, consultar, editar y eliminar eventos.
- **Actividades:** permite administrar las actividades relacionadas con cada evento.
- **Inscripciones:** permite registrar participantes, consultar sus estados y gestionar las listas de espera.
- **Asistencias:** permite registrar y consultar la asistencia de los participantes a las actividades.
- **Estadísticas:** permite consultar los resultados de ocupación y asistencia.
- **Certificados:** permite seleccionar un evento, consultar sus participantes y generar certificados para quienes cumplan el requisito mínimo de asistencia.

## Persistencia de datos

La aplicación utiliza archivos de texto (.txt) para almacenar la información de los eventos, actividades, inscripciones y asistencias.

Los datos se conservan al cerrar y volver a ejecutar el programa.

En el primer inicio, el sistema crea datos de ejemplo para facilitar las pruebas de las funcionalidades.

## Diseño responsive

La aplicación cuenta con un diseño adaptable a diferentes tamaños de pantalla, permitiendo utilizar sus funcionalidades desde computadores, tabletas y dispositivos móviles.

## Capturas de pantalla

### 1. Gestión de eventos

![Vista de eventos](imagenes/Vista%20Principal.png)
![Vista de eventos](imagenes/Vista%20gestion%20de%20eventos%201.png)
![Vista de eventos](imagenes/vista%20gestion%20de%20eventos%202.png)
![Vista de eventos](imagenes/vista%20gestion%20de%20eventos%203.png)

### 2. Gestión de actividades

![Vista de actividades](imagenes/Vista%20gestion%20de%20actividades%201.png)
![Vista de actividades](imagenes/Vista%20gestion%20de%20actividades%202.png)

### 3. Gestión de inscripciones

![Vista de inscripciones](imagenes/Vista%20gestion%20de%20inscripciones.png)

### 4. Gestión de asistencias

![Vista de asistencias](imagenes/Vista%20gestion%20de%20asistencias.png)

### 5. Estadísticas

![Vista de estadísticas](imagenes/Vista%20gestion%20Estadisticas.png)

### 6. Generación de certificados

![Vista de certificados](imagenes/Vista%20gestion%20certificados.png)

## Estructura general del proyecto

El proyecto está organizado en clases que permiten separar las vistas, los modelos de datos y el manejo de archivos.

- **Vistas:** contienen las interfaces y funcionalidades de cada sección.
- **Clases de datos:** representan los eventos, actividades, inscripciones y asistencias.
- **Clases de archivos:** se encargan de guardar y cargar la información mediante archivos de texto.
- **MainLayout:** contiene el menú principal y la estructura de navegación.

## Estado del proyecto

Finalizado.

## Autor

Matías Ortiz Ceballos

## Fecha de entrega

11 de noviembre de 2026