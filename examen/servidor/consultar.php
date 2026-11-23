<?php
// Ubicación: htdocs/api_estudiantes/consultar.php
// Sin "codigo" -> lista todos. Con "codigo" -> solo ese estudiante.
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once "config/database.php";

$codigo = trim($_POST["codigo"] ?? "");

try {
    $db = (new Database())->getConnection();
    if ($codigo === "") {
        $stmt = $db->query("SELECT codigo, nombre, apellido, edad, carrera FROM estudiantes ORDER BY apellido, nombre");
    } else {
        $stmt = $db->prepare("SELECT codigo, nombre, apellido, edad, carrera FROM estudiantes WHERE codigo = :codigo");
        $stmt->execute([":codigo" => $codigo]);
    }
    echo json_encode(["estado" => "ok", "estudiantes" => $stmt->fetchAll(PDO::FETCH_ASSOC)]);
} catch (PDOException $e) {
    echo json_encode(["estado" => "error", "mensaje" => "Error en la base de datos"]);
}
?>
