# Examen – Registro de Estudiantes (Kotlin + Volley + PHP + MySQL)

## Estructura
- `servidor/` → copiar a `C:\xampp\htdocs\api_estudiantes\` (con la subcarpeta `config/`; el `.sql` no es necesario copiarlo)
- `servidor/estudiantes.sql` → ejecutar en phpMyAdmin (crea `bd_examen` y la tabla `estudiantes`)
- `android/MainActivity.kt` y `android/activity_main.xml` → copiar al proyecto de Android Studio

## Configuración Android
1. `build.gradle (app)`: `implementation("com.android.volley:volley:1.2.1")`
2. `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <application android:usesCleartextTraffic="true" ... >
   ```
3. Cambiar el `package` en `MainActivity.kt`. En celular real, reemplazar `10.0.2.2` por la IP de tu PC.

## Endpoints (todos POST form-data, responden JSON)
| Archivo | Parámetros | Respuesta |
|---|---|---|
| insertar.php | codigo, nombre, apellido, edad, carrera | `{estado, mensaje}` |
| actualizar.php | codigo, nombre, apellido, edad, carrera | `{estado, mensaje}` |
| consultar.php | codigo (opcional; vacío = todos) | `{estado, estudiantes:[...]}` |
| eliminar.php | codigo | `{estado, mensaje}` |

## Guion sugerido para el video
1. Mostrar phpMyAdmin: base `bd_examen`, tabla `estudiantes` y sus columnas.
2. Explicar los 4 PHP (PDO + sentencias preparadas, respuesta JSON) y `config/database.php`.
3. Explicar `MainActivity.kt`: Volley `StringRequest` POST, `getParams()`, parseo con `JSONObject`.
4. Demostrar en la app: insertar → se ve en la lista y en phpMyAdmin; consultar por código;
   tocar un item, editar y actualizar; eliminar; probar errores (código duplicado, campos vacíos).
