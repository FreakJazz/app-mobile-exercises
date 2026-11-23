# Examen Ejercicio 2 – Lista de Contactos (RecyclerView + Volley + PHP + MySQL)

## Estructura
- `servidor/` → copiar a `C:\xampp\htdocs\api_contactos\` (con `config/`) o usar Docker (ver abajo)
- `servidor/contactos.sql` → ejecutar en phpMyAdmin (crea `bd_contactos` y la tabla `contactos` con 6 registros)
- `android/` → `MainActivity.kt`, `activity_main.xml` e `item_contacto.xml` (los dos XML van en `res/layout/`)

## Configuración Android
1. `build.gradle (app)`:
   ```
   implementation("com.android.volley:volley:1.2.1")
   implementation("androidx.recyclerview:recyclerview:1.3.2")
   implementation("com.github.bumptech.glide:glide:4.16.0")
   ```
2. `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <application android:usesCleartextTraffic="true" ... >
   ```
3. Cambiar el `package` en `MainActivity.kt`. En celular real, reemplazar `10.0.2.2` por la IP de tu PC (el puerto de Docker es 8082).
4. Las fotos son URLs (pravatar.cc): el emulador necesita internet.

## Endpoints
| Archivo | Método | Parámetros | Respuesta |
|---|---|---|---|
| consultar.php | GET | – | `{estado, contactos:[{id,nombre,telefono,foto}]}` |
| eliminar.php | POST | id | `{estado, mensaje}` |

## Guion para el video
1. phpMyAdmin: base `bd_contactos`, tabla `contactos` (id, nombre, telefono, foto).
2. Explicar `consultar.php` y `eliminar.php` (PDO + sentencias preparadas, JSON) y `config/database.php`.
3. Explicar `MainActivity.kt`: Adapter/ViewHolder, Volley GET/POST, Glide para la foto, `AlertDialog` de confirmación.
4. Demo: lista con foto, nombre y teléfono → tocar eliminar → Cancelar (no pasa nada) →
   Eliminar (desaparece de la lista) → refrescar phpMyAdmin y comprobar que se borró de la BD.
5. Verificar que el link del video abre correctamente antes de entregar.

## Docker (backend para el video)
```
docker compose up -d --build
```
- API: http://localhost:8082/api_contactos/consultar.php (desde el emulador: `http://10.0.2.2:8082/api_contactos/`)
- MySQL para **DBeaver**: host `localhost`, puerto `3307`, usuario `root`, sin contraseña, base `bd_contactos`
  (si DBeaver pide, activar `allowPublicKeyRetrieval=true` en las propiedades del driver).
- Los puertos 80 y 3306 de esta PC los usa XAMPP, por eso se usan 8082 y 3307.
- `docker/mysql-entrypoint.sh` y el `Dockerfile` (Debian + Apache + PHP) existen porque las imágenes oficiales
  `php:apache` y los scripts de arranque de `mysql` se descargan corruptos (archivos vacíos) en este Docker.
