<?php
// Ubicación: htdocs/api_estudiantes/actualizar.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once "config/database.php";

$codigo   = trim($_POST["codigo"]   ?? "");
$nombre   = trim($_POST["nombre"]   ?? "");
$apellido = trim($_POST["apellido"] ?? "");
$edad     = trim($_POST["edad"]     ?? "");
$carrera  = trim($_POST["carrera"]  ?? "");

if ($codigo === "" || $nombre === "" || $apellido === "" || $carrera === "" || !ctype_digit($edad)) {
    echo json_encode(["estado" => "error", "mensaje" => "Todos los campos son obligatorios (edad numérica)"]);
    exit;
}

try {
    $db = (new Database())->getConnection();
    $existe = $db->prepare("SELECT id FROM estudiantes WHERE codigo = :codigo");
    $existe->execute([":codigo" => $codigo]);
    if ($existe->rowCount() === 0) {
        echo json_encode(["estado" => "error", "mensaje" => "No existe un estudiante con ese código"]);
        exit;
    }
    $stmt = $db->prepare("UPDATE estudiantes SET nombre = :nombre, apellido = :apellido,
                          edad = :edad, carrera = :carrera WHERE codigo = :codigo");
    $stmt->execute([":nombre" => $nombre, ":apellido" => $apellido, ":edad" => (int)$edad,
                    ":carrera" => $carrera, ":codigo" => $codigo]);
    echo json_encode(["estado" => "ok", "mensaje" => "Estudiante actualizado"]);
} catch (PDOException $e) {
    echo json_encode(["estado" => "error", "mensaje" => "Error en la base de datos"]);
}
?>
