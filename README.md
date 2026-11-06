# Spring Boot API 1

**Alumno** · Héctor Plaza Garcia  
**Módulo** · Desarrollo Web en Entorno Servidor

La API gestiona un único nombre. `FirstController` lo guarda en la variable `nameStored` mientras la aplicación está en ejecución. Al reiniciar la aplicación, la variable vuelve a estar vacía. El proyecto usa Spring Boot 4.0.9 y requiere un JDK 17 o superior. El Maven Wrapper descarga Maven 3.9.15 y Maven descarga las dependencias la primera vez que se ejecuta.

## Compilar y probar

En Windows, ejecuta este comando desde la carpeta del proyecto.

```powershell
.\mvnw.cmd clean verify
```

En Linux o macOS, usa el script de shell.

```sh
sh ./mvnw clean verify
```

Las pruebas automatizadas con MockMvc comprueban la creación, la consulta, la actualización, el borrado, los casos sin nombre guardado, el alta duplicada y las entradas vacías o en blanco.

## Iniciar la aplicación

Después de compilar, ejecuta el JAR desde la misma carpeta.

```sh
java -jar target/spring-boot-api-1-1.0.0.jar
```

## Operaciones HTTP

La API responde en `http://localhost:8080/name`. Envía el nombre como cuerpo de texto plano con `Content-Type: text/plain; charset=UTF-8` para `POST` y `PUT`. El controlador recorta los espacios de los extremos y devuelve `400 Bad Request` si el nombre falta o solo contiene espacios en blanco.

| Método y ruta | Operación | Respuesta |
| --- | --- | --- |
| `POST /name` | Guarda el primer nombre. | `201 Created`, con la cabecera `Location: /name` y sin cuerpo. `409 Conflict` si ya hay un nombre. |
| `GET /name` | Consulta el nombre guardado. | `200 OK`, con `Hola ` seguido del nombre. `404 Not Found` si no hay nombre. |
| `PUT /name` | Sustituye el nombre guardado. | `200 OK`, con `Hola ` seguido del nombre nuevo. `404 Not Found` si no hay nombre. |
| `DELETE /name` | Borra el nombre guardado. | `204 No Content`. `404 Not Found` si no hay nombre. |

`DELETE /name` admite el parámetro de consulta `name` de forma opcional. La operación borra el único nombre guardado.

Con la aplicación recién iniciada, puedes probar el ciclo completo en PowerShell.

```powershell
curl.exe -i -X POST -H 'Content-Type: text/plain; charset=UTF-8' --data 'Ana' http://localhost:8080/name
curl.exe -i http://localhost:8080/name
curl.exe -i -X PUT -H 'Content-Type: text/plain; charset=UTF-8' --data 'Lucia' http://localhost:8080/name
curl.exe -i -X DELETE http://localhost:8080/name
curl.exe -i http://localhost:8080/name
```

El archivo `evidencia-http.txt` recoge las peticiones realizadas con `curl.exe` contra el JAR en ejecución y las respuestas recibidas. Durante esa prueba se asignó un puerto local libre automáticamente.
