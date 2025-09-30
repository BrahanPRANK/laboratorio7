package com.example.laboratorio07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "access_db"
        ).build()

        setContent {
            CrudApp(db)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudApp(db: AppDatabase) {
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var lista by remember { mutableStateOf(listOf<UserAccess>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Control de Acceso Tecsup") },
                actions = {
                    Button(onClick = {
                        scope.launch {
                            db.userAccessDao().insert(
                                UserAccess(nombre = nombre, rol = rol, codigo = codigo)
                            )
                            lista = db.userAccessDao().getAll()
                            nombre = ""
                            rol = ""
                            codigo = ""
                        }
                    }) {
                        Text("Agregar")
                    }
                    Button(onClick = {
                        scope.launch {
                            lista = db.userAccessDao().getAll()
                        }
                    }) {
                        Text("Listar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )
            OutlinedTextField(
                value = rol,
                onValueChange = { rol = it },
                label = { Text("Rol (Alumno/Docente)") }
            )
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código/PIN") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Usuarios registrados:")
            lista.forEach {
                Text("${it.nombre} - ${it.rol} - ${it.codigo}")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                scope.launch {
                    val lastUser = db.userAccessDao().getLastUser()
                    if (lastUser != null) {
                        db.userAccessDao().delete(lastUser) // ✅ ahora funciona
                        lista = db.userAccessDao().getAll()
                    }
                }
            }) {
                Text("Eliminar último")
            }
        }
    }
}
