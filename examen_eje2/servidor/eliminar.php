<?php
// Ubicación: htdocs/api_contactos/eliminar.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once "config/database.php";

$id = trim($_POST["id"] ?? "");
if (!ctype_digit($id)) {
    echo json_encode(["estado" => "error", "mensaje" => "El id es obligatorio"]);
    exit;
}

try {
    $db = (new Database())->getConnection();
    $stmt = $db->prepare("DELETE FROM contactos WHERE id = :id");
    $stmt->execute([":id" => (int)$id]);
    echo json_encode($stmt->rowCount() > 0
        ? ["estado" => "ok", "mensaje" => "Contacto eliminado"]
        : ["estado" => "error", "mensaje" => "No existe el contacto"]);
} catch (PDOException $e) {
    echo json_encode(["estado" => "error", "mensaje" => "Error en la base de datos"]);
}
?>
