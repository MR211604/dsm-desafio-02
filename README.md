# Desafio 02 DSM - Gestion de Destinos

**Autor**: Leo Fernando Miranda Rodriguez

## Video Demostrativo

Pueden revisar detalladamente el funcionamiento de todas estas caracteristicas y operaciones CRUD de Firebase en el entorno real de la app en esta demo:
[Demostracion del Desafio 02 - DSM](https://youtu.be/GKcq6J-5ALM)

El proposito principal de esta aplicacion es demostrar el uso de Firebase para autenticacion y operaciones CRUD de destinos turisticos.

## Caracteristicas Principales (Operaciones CRUD con Firebase)

La aplicacion incluye una integracion completa con **Firebase Firestore** y **Firebase Authentication** para la gestion de datos:

- **Autenticacion de Usuarios**: Registro e inicio de sesion con correo electronico y contrasena mediante Firebase Authentication.
- **Crear (Create)**: Los usuarios pueden agregar nuevos destinos turisticos llenando un formulario en la aplicacion; estos datos se almacenan en las colecciones de Firestore.
- **Leer (Read)**: Visualizacion de todos los destinos almacenados en la base de datos (Firestore) procesados y representados dinamicamente en una lista de forma fluida.
- **Actualizar (Update)**: Permite a los usuarios seleccionar un destino existente previamente creado, modificar sus detalles como nombre o descripcion, y actualizar los cambios en el documento correspondiente de Firestore.
- **Eliminar (Delete)**: Facilita la eliminacion de cualquier nodo de destino especifico directamente desde la base de datos, reflejando el cambio de inmediato en la interfaz.

## Detalles Tecnicos

- **Lenguaje de programacion**: Kotlin.
- **Plataforma de backend**: Firebase (Cloud Firestore y Authentication).
