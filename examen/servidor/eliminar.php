<?php
// Ubicación: htdocs/api_estudiantes/eliminar.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once "config/database.php";

$codigo = trim($_POST["codigo"] ?? "");
if ($codigo === "") {
    echo json_encode(["estado" => "error", "mensaje" => "El código es obligatorio"]);
    exit;
}

try {
    $db = (new Database())->getConnection();
    $stmt = $db->prepare("DELETE FROM estudiantes WHERE codigo = :codigo");
    $stmt->execute([":codigo" => $codigo]);
    echo json_encode($stmt->rowCount() > 0
        ? ["estado" => "ok", "mensaje" => "Estudiante eliminado"]
        : ["estado" => "error", "mensaje" => "No existe un estudiante con ese código"]);
} catch (PDOException $e) {
    echo json_encode(["estado" => "error", "mensaje" => "Error en la base de datos"]);
}
?>
