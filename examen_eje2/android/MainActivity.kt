package com.example.contactos // TODO: cambia al package de tu app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject

data class Contacto(val id: String, val nombre: String, val telefono: String, val foto: String)

// ---------- Adapter del RecyclerView ----------
class ContactoAdapter(
    private val items: MutableList<Contacto>,
    private val onEliminar: (Contacto) -> Unit
) : RecyclerView.Adapter<ContactoAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val ivFoto: ImageView = v.findViewById(R.id.ivFoto)
        val tvNombre: TextView = v.findViewById(R.id.tvNombre)
        val tvTelefono: TextView = v.findViewById(R.id.tvTelefono)
        val btnEliminar: ImageButton = v.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_contacto, parent, false))

    override fun onBindViewHolder(h: VH, position: Int) {
        val c = items[position]
        h.tvNombre.text = c.nombre
        h.tvTelefono.text = c.telefono
        Glide.with(h.itemView).load(c.foto)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .circleCrop()
            .into(h.ivFoto)
        h.btnEliminar.setOnClickListener { onEliminar(c) }
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<Contacto>) {
        items.clear(); items.addAll(nuevos); notifyDataSetChanged()
    }

    fun quitar(c: Contacto) {
        val pos = items.indexOf(c)
        if (pos >= 0) { items.removeAt(pos); notifyItemRemoved(pos) }
    }
}

class MainActivity : AppCompatActivity() {

    companion object {
        // 10.0.2.2 = localhost del emulador. En celular real usa la IP de tu PC.
        private const val BASE = "http://10.0.2.2:8082/api_contactos/"
        private const val URL_CONSULTAR = BASE + "consultar.php"
        private const val URL_ELIMINAR = BASE + "eliminar.php"
    }

    private lateinit var tvEstado: TextView
    private lateinit var rvContactos: RecyclerView
    private lateinit var queue: RequestQueue
    private lateinit var adapter: ContactoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvEstado = findViewById(R.id.tvEstado)
        rvContactos = findViewById(R.id.rvContactos)
        queue = Volley.newRequestQueue(this)

        adapter = ContactoAdapter(mutableListOf()) { confirmarEliminar(it) }
        rvContactos.layoutManager = LinearLayoutManager(this)
        rvContactos.adapter = adapter

        cargarContactos()
    }

    // Diálogo de confirmación previo a eliminar
    private fun confirmarEliminar(c: Contacto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar contacto")
            .setMessage("¿Seguro que deseas eliminar a ${c.nombre}?")
            .setPositiveButton("Eliminar") { _, _ -> eliminar(c) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // GET de contactos (Web Service) -> JSON -> RecyclerView
    private fun cargarContactos() {
        val request = StringRequest(Request.Method.GET, URL_CONSULTAR,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.getString("estado") != "ok") {
                        tvEstado.text = json.getString("mensaje")
                    } else {
                        val arr = json.getJSONArray("contactos")
                        val nuevos = (0 until arr.length()).map {
                            val o = arr.getJSONObject(it)
                            Contacto(o.getString("id"), o.getString("nombre"),
                                o.getString("telefono"), o.getString("foto"))
                        }
                        adapter.actualizar(nuevos)
                        tvEstado.text = "${nuevos.size} contacto(s)"
                    }
                } catch (e: Exception) {
                    tvEstado.text = "Respuesta inválida del servidor"
                }
            },
            { error ->
                Log.e("Volley", error.toString())
                tvEstado.text = "Error de conexión"
            })
        queue.add(request)
    }

    // POST eliminar.php (id); si fue exitoso se quita del RecyclerView
    private fun eliminar(c: Contacto) {
        val request = object : StringRequest(Request.Method.POST, URL_ELIMINAR,
            { response ->
                try {
                    val json = JSONObject(response)
                    Toast.makeText(this, json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                    if (json.getString("estado") == "ok") {
                        adapter.quitar(c)
                        tvEstado.text = "${adapter.itemCount} contacto(s)"
                    }
                } catch (e: Exception) {
                    tvEstado.text = "Respuesta inválida del servidor"
                }
            },
            { error ->
                Log.e("Volley", error.toString())
                tvEstado.text = "Error de conexión"
            }) {
            override fun getParams(): Map<String, String> = mapOf("id" to c.id)
        }
        queue.add(request)
    }
}
