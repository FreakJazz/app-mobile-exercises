<?php
// Ubicación: htdocs/api_contactos/consultar.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once "config/database.php";

try {
    $db = (new Database())->getConnection();
    $stmt = $db->query("SELECT id, nombre, telefono, foto FROM contactos ORDER BY nombre");
    echo json_encode(["estado" => "ok", "contactos" => $stmt->fetchAll(PDO::FETCH_ASSOC)]);
} catch (PDOException $e) {
    echo json_encode(["estado" => "error", "mensaje" => "Error en la base de datos"]);
}
?>
