package com.example.personas // TODO: cambia al package de tu app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VerificarCedulaActivity : AppCompatActivity() {

    companion object {
        // Constante para la URL (buena práctica del PDF). 10.0.2.2 = localhost del emulador
        private const val URL_VERIFICAR =
            "http://10.0.2.2/mi_api/usuarios/verificar_cedula.php"
        private const val LONGITUD_CEDULA = 10
    }

    private lateinit var etCedula: EditText
    private lateinit var btnVerificar: Button
    private lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificar_cedula)

        etCedula = findViewById(R.id.etCedula)
        btnVerificar = findViewById(R.id.btnVerificar)
        tvResultado = findViewById(R.id.tvResultado)

        btnVerificar.setOnClickListener { verificar() }
    }

    private fun verificar() {
        val cedula = etCedula.text.toString().trim()

        // 1. Validar ANTES de enviar: exactamente 10 dígitos
        if (!cedula.matches(Regex("\\d{$LONGITUD_CEDULA}"))) {
            etCedula.error = "La cédula debe tener exactamente $LONGITUD_CEDULA dígitos"
            tvResultado.text = "Cédula inválida"
            return
        }
        etCedula.error = null
        btnVerificar.isEnabled = false
        tvResultado.text = "Consultando..."

        // 2. Petición POST con Volley (misma estructura que el registro de usuario)
        val request = object : StringRequest(
            Request.Method.POST, URL_VERIFICAR,
            { response ->
                btnVerificar.isEnabled = true
                try {
                    val json = JSONObject(response)
                    if (json.getString("estado") == "ok") {
                        tvResultado.text = if (json.getBoolean("registrada"))
                            "La cédula $cedula YA está registrada"
                        else
                            "La cédula $cedula está DISPONIBLE"
                    } else {
                        tvResultado.text = json.getString("mensaje")
                    }
                } catch (e: Exception) {
                    tvResultado.text = "Respuesta inválida del servidor"
                }
            },
            { error ->
                btnVerificar.isEnabled = true
                Log.e("Volley", error.toString())
                tvResultado.text = "Error de conexión"
            }
        ) {
            override fun getParams(): Map<String, String> = mapOf("cedula" to cedula)
        }

        Volley.newRequestQueue(this).add(request)
    }
}
