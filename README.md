
# Trabajo Final Programacion II - 2025

**Alumno**: Torres Burgos Maria Victoria \
**Legajo**: 62092 \
**Año**: 4to
--- 
## Sistema Integral de Venta de Entradas

Este proyecto constituye la implementación del Trabajo Final para la cátedra de Programación II. Consiste en una solución para la venta de entradas a eventos, integrando sincronización de datos externos, gestión de concurrencia mediante bloqueos y una interfaz móvil nativa.

## Descripción del Proyecto

La aplicación permite a los usuarios autenticarse, explorar un catálogo de eventos sincronizado desde una fuente externa, seleccionar asientos de forma interactiva en un mapa y completar el proceso de compra mediante la asignación de entradas. El sistema garantiza la integridad de los datos en entornos concurrentes y mantiene la consistencia de la sesión en múltiples dispositivos.


## Arquitectura del Sistema

El sistema implementa una **Arquitectura Hexagonal** en las entidades `Evento` y `Venta` y una arquitectura **MVVM (Model-View-ViewModel)** en el cliente móvil. Esta separación permite que la lógica de negocio permanezca aislada de las tecnologías de infraestructura (Base de Datos, APIs externas, Frameworks).

### Sincronización Automática de Eventos

El sistema implementa un mecanismo de sincronización en tiempo real para garantizar que los datos locales coincidan exactamente con los del servicio de la cátedra.

## Componentes 

### Backend

Desarrollado en Java utilizando Spring Boot, es el núcleo de la aplicación. Sus funciones principales incluyen:

* **Gestión de Seguridad:** Implementación de autenticación mediante **JWT (JSON Web Tokens)**.
* **Persistencia:** Gestión de datos relacionales en MySQL mediante JPA/Hibernate.
* **Sincronización:** Procesos programados para replicar eventos y precios desde la API de la Cátedra hacia la base de datos local.
* **Orquestación de Infraestructura:** Uso de `spring-boot-docker-compose` para el despliegue automático de contenedores.

### Proxy

**Acceso exclusivo:** Es el único componente del sistema con autorización y acceso técnico para conectarse a los servicios adicionales de la cátedra: **Kafka** y **Redis**.

**Intermediario:** Actúa como puente o intermediario exclusivo entre el Backend del alumno y los servicios externos de la cátedra.

### Interacción con Redis (Gestión de Asientos)

**Consulta de ubicaciones:** El proxy accede al servicio Redis de la cátedra para obtener y mantener actualizada la información sobre la ubicación de los asientos de cada evento.

**Estado de asientos:** Consulta y reporta el estado de los asientos (libres, vendidos, seleccionados o bloqueados) cada vez que el Backend lo solicite.

### Interacción con Kafka (Sincronización de Eventos)

**Suscripción a tópicos:** El proxy debe estar suscrito al tópico de Kafka de la cátedra para recibir notificaciones automáticas sobre cambios en los eventos.

**Lectura de cambios:** Debe procesar la información de Kafka cuando se agreguen nuevos eventos, expiren por tiempo, se cancelen o se modifiquen sus datos.

**Notificación al Backend:** Una vez que recibe una actualización desde Kafka, el proxy informa estos cambios al Backend.


**Comunicación:** Toda la comunicación entre el proxy y el backend esta hecha mediante **JSON**.


**Autenticación:** El proxy esta configurado con sus credenciales y el acceso esta autenticado mediante **tokens JWT**.


### Cliente Móvil 

Desarrollado de forma nativa con **Kotlin**:

* **Consumo de API:** Integración con el backend mediante **Ktor Client**.
* **Gestión de Estado:** Manejo de navegación compleja, desde la selección del evento hasta la compra de entradas.

## Prerrequisitos

Para la correcta ejecución del sistema, debe contar con:

* **Docker y Docker Compose:** En ejecución.
* **Java JDK 21.**
* **Android Studio:** Versión Ladybug o superior.

## Cómo Ejecutar

### 1. Preparación del Backend

```
cd backend/
./mvnw clean package -DskipTests
cd ..
```

### 2. Preparación del Proxy

```
cd proxy/
./mvnw clean package -DskipTests
cd ..
```
### 3. Despliegue con Docker

Levantar la infraestructura completa (MySQL, Redis, Kafka, Proxy, Backend)
```
docker compose up --build
```

### 3. Ejecutar la Aplicación Móvil

1. Abra el proyecto en Android Studio.
2. Sincronice Gradle y presione **Run**.
3. Ejecutar en un emulador o dispositivo físico.
