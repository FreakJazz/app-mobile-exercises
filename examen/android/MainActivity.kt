package com.example.estudiantes // TODO: cambia al package de tu app

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

data class Estudiante(
    val codigo: String, val nombre: String, val apellido: String,
    val edad: String, val carrera: String
) {
    override fun toString() = "$codigo - $nombre $apellido ($edad años)\n$carrera"
}

class MainActivity : AppCompatActivity() {

    companion object {
        // 10.0.2.2 = localhost del emulador. En celular real usa la IP de tu PC.
        private const val BASE = "http://10.0.2.2:8091/api_estudiantes/"
        private const val URL_INSERTAR = BASE + "insertar.php"
        private const val URL_ACTUALIZAR = BASE + "actualizar.php"
        private const val URL_CONSULTAR = BASE + "consultar.php"
        private const val URL_ELIMINAR = BASE + "eliminar.php"
    }

    private lateinit var etCodigo: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEdad: EditText
    private lateinit var etCarrera: EditText
    private lateinit var tvEstado: TextView
    private lateinit var lvEstudiantes: ListView
    private lateinit var queue: RequestQueue

    private val lista = mutableListOf<Estudiante>()
    private lateinit var adapter: ArrayAdapter<Estudiante>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCodigo = findViewById(R.id.etCodigo)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etEdad = findViewById(R.id.etEdad)
        etCarrera = findViewById(R.id.etCarrera)
        tvEstado = findViewById(R.id.tvEstado)
        lvEstudiantes = findViewById(R.id.lvEstudiantes)
        queue = Volley.newRequestQueue(this)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
        lvEstudiantes.adapter = adapter
        // Al tocar un estudiante de la lista se cargan sus datos en el formulario
        lvEstudiantes.setOnItemClickListener { _, _, pos, _ -> llenarFormulario(lista[pos]) }

        findViewById<Button>(R.id.btnInsertar).setOnClickListener { insertar() }
        findViewById<Button>(R.id.btnActualizar).setOnClickListener { actualizar() }
        findViewById<Button>(R.id.btnConsultar).setOnClickListener { consultar() }
        findViewById<Button>(R.id.btnEliminar).setOnClickListener { eliminar() }
        findViewById<Button>(R.id.btnListar).setOnClickListener { listar() }

        listar()
    }

    // ---------- Operaciones ----------

    private fun insertar() {
        val p = datosFormulario() ?: return
        enviar(URL_INSERTAR, p) { limpiar(); listar() }
    }

    private fun actualizar() {
        val p = datosFormulario() ?: return
        enviar(URL_ACTUALIZAR, p) { listar() }
    }

    private fun eliminar() {
        val codigo = etCodigo.text.toString().trim()
        if (codigo.isEmpty()) { etCodigo.error = "Ingresa el código"; return }
        enviar(URL_ELIMINAR, mapOf("codigo" to codigo)) { limpiar(); listar() }
    }

    /** Consulta por código; si el campo está vacío, lista todos. */
    private fun consultar() {
        val codigo = etCodigo.text.toString().trim()
        pedirLista(mapOf("codigo" to codigo)) { estudiantes ->
            if (codigo.isNotEmpty() && estudiantes.size == 1) llenarFormulario(estudiantes[0])
            if (estudiantes.isEmpty()) tvEstado.text = "No se encontraron estudiantes"
        }
    }

    private fun listar() = pedirLista(emptyMap()) { }

    // ---------- Red (Volley, POST form-data) ----------

    /** Para insertar / actualizar / eliminar: respuesta {estado, mensaje}. */
    private fun enviar(url: String, params: Map<String, String>, alExito: () -> Unit) {
        tvEstado.text = "Procesando..."
        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                try {
                    val json = JSONObject(response)
                    val mensaje = json.getString("mensaje")
                    tvEstado.text = mensaje
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                    if (json.getString("estado") == "ok") alExito()
                } catch (e: Exception) {
                    tvEstado.text = "Respuesta inválida del servidor"
                }
            },
            { error ->
                Log.e("Volley", error.toString())
                tvEstado.text = "Error de conexión"
            }) {
            override fun getParams(): Map<String, String> = params
        }
        queue.add(request)
    }

    /** Para consultar: respuesta {estado, estudiantes:[...]}. */
    private fun pedirLista(params: Map<String, String>, alTerminar: (List<Estudiante>) -> Unit) {
        val request = object : StringRequest(Request.Method.POST, URL_CONSULTAR,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.getString("estado") != "ok") {
                        tvEstado.text = json.getString("mensaje")
                    } else {
                        val arr = json.getJSONArray("estudiantes")
                        lista.clear()
                        for (i in 0 until arr.length()) {
                            val o = arr.getJSONObject(i)
                            lista.add(Estudiante(o.getString("codigo"), o.getString("nombre"),
                                o.getString("apellido"), o.getString("edad"), o.getString("carrera")))
                        }
                        adapter.notifyDataSetChanged()
                        tvEstado.text = "${lista.size} estudiante(s)"
                        alTerminar(lista.toList())
                    }
                } catch (e: Exception) {
                    tvEstado.text = "Respuesta inválida del servidor"
                }
            },
            { error ->
                Log.e("Volley", error.toString())
                tvEstado.text = "Error de conexión"
            }) {
            override fun getParams(): Map<String, String> = params
        }
        queue.add(request)
    }

    // ---------- Utilidades de formulario ----------

    private fun datosFormulario(): Map<String, String>? {
        val campos = listOf(etCodigo, etNombre, etApellido, etEdad, etCarrera)
        var ok = true
        for (c in campos) {
            if (c.text.toString().trim().isEmpty()) { c.error = "Obligatorio"; ok = false }
        }
        if (!ok) return null
        return mapOf(
            "codigo" to etCodigo.text.toString().trim(),
            "nombre" to etNombre.text.toString().trim(),
            "apellido" to etApellido.text.toString().trim(),
            "edad" to etEdad.text.toString().trim(),
            "carrera" to etCarrera.text.toString().trim()
        )
    }

    private fun llenarFormulario(e: Estudiante) {
        etCodigo.setText(e.codigo); etNombre.setText(e.nombre); etApellido.setText(e.apellido)
        etEdad.setText(e.edad); etCarrera.setText(e.carrera)
    }

    private fun limpiar() {
        listOf(etCodigo, etNombre, etApellido, etEdad, etCarrera).forEach { it.setText("") }
    }
}
