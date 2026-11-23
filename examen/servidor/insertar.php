<?php
// Ubicación: htdocs/api_estudiantes/insertar.php
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
    $stmt = $db->prepare("INSERT INTO estudiantes (codigo, nombre, apellido, edad, carrera)
                          VALUES (:codigo, :nombre, :apellido, :edad, :carrera)");
    $stmt->execute([":codigo" => $codigo, ":nombre" => $nombre, ":apellido" => $apellido,
                    ":edad" => (int)$edad, ":carrera" => $carrera]);
    echo json_encode(["estado" => "ok", "mensaje" => "Estudiante registrado"]);
} catch (PDOException $e) {
    $msg = $e->errorInfo[1] == 1062 ? "El código ya existe" : "Error en la base de datos";
    echo json_encode(["estado" => "error", "mensaje" => $msg]);
}
?>
