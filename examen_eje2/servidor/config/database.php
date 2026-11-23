<?php
// Ubicación: htdocs/api_contactos/config/database.php
// Lee variables de entorno (Docker); si no existen usa los valores de XAMPP.
class Database {
    public function getConnection() {
        $host = getenv("DB_HOST") ?: "localhost";
        $db   = getenv("DB_NAME") ?: "bd_contactos";
        $user = getenv("DB_USER") ?: "root";
        $pass = getenv("DB_PASS") ?: "";

        $conn = new PDO("mysql:host=$host;dbname=$db;charset=utf8mb4", $user, $pass);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        return $conn;
    }
}
?>
