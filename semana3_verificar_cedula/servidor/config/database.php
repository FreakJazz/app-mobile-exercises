<?php
// Ubicación: htdocs/mi_api/config/database.php  (clase de conexión del PDF)
class Database {
    private $host = "localhost";
    private $db   = "bd_app";
    private $user = "root";
    private $pass = "";
    public $conn;

    public function getConnection() {
        $this->conn = null;
        try {
            $this->conn = new PDO(
                "mysql:host={$this->host};dbname={$this->db};charset=utf8mb4",
                $this->user, $this->pass
            );
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            echo json_encode(["estado" => "error", "mensaje" => "No hay conexión a la base de datos"]);
            exit;
        }
        return $this->conn;
    }
}
?>
