<?php
// Ubicación: htdocs/mi_api/usuarios/verificar_cedula.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");

include_once "../config/database.php";

// Volley con getParams() envía form-data, por eso se lee con $_POST
$cedula = trim($_POST["cedula"] ?? "");

// Validación en el servidor: exactamente 10 dígitos
if (!preg_match('/^\d{10}$/', $cedula)) {
    echo json_encode(["estado" => "error",
        "mensaje" => "La cédula debe tener exactamente 10 dígitos"]);
    exit;
}

try {
    $db = (new Database())->getConnection();
    $stmt = $db->prepare("SELECT id FROM usuarios WHERE cedula = :cedula LIMIT 1");
    $stmt->bindParam(":cedula", $cedula);
    $stmt->execute();

    $registrada = $stmt->rowCount() > 0;
    echo json_encode([
        "estado"     => "ok",
        "registrada" => $registrada,
        "mensaje"    => $registrada ? "La cédula ya está registrada"
                                    : "La cédula está disponible"
    ]);
} catch (PDOException $e) {
    echo json_encode(["estado" => "error", "mensaje" => "Error en la base de datos"]);
}
?>
