package com.example.laboratorio07

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current

    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val dataUser = remember { mutableStateOf("") }

    val db = remember { crearDatabase(context) }
    val dao = remember { db.userDao() }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Users DB") },
                actions = {
                    // Agregar usuario
                    IconButton(onClick = {
                        // Ejecutar agregar desde la topbar
                        coroutineScope.launch {
                            if (firstName.isBlank() && lastName.isBlank()) {
                                snackbarHostState.showSnackbar("Rellena firstName o lastName para agregar")
                            } else {
                                val user = User(0, firstName, lastName)
                                AgregarUsuario(user = user, dao = dao)
                                // limpiar campos después de agregar
                                firstName = ""
                                lastName = ""
                                snackbarHostState.showSnackbar("Usuario agregado")
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar")
                    }

                    // Listar usuarios
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val data = getUsers(dao = dao)
                            dataUser.value = data
                            snackbarHostState.showSnackbar("Usuarios listados")
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.List, contentDescription = "Listar")
                    }

                    // Eliminar último usuario
                    IconButton(onClick = {
                        coroutineScope.launch {
                            deleteLastUser(dao)
                            val data = getUsers(dao = dao)
                            dataUser.value = data
                            snackbarHostState.showSnackbar("Último usuario eliminado (si existía)")
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar último")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID (solo lectura)") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mantengo el botón Eliminar aquí también (opcional)
            Button(
                onClick = {
                    coroutineScope.launch {
                        deleteLastUser(dao)
                        val data = getUsers(dao)
                        dataUser.value = data
                        snackbarHostState.showSnackbar("Último usuario eliminado (si existía)")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar Último Usuario", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = dataUser.value, fontSize = 18.sp)
        }
    }
}

fun crearDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        UserDatabase::class.java,
        "user_db"
    ).build()
}

suspend fun getUsers(dao: UserDao): String {
    var rpta = ""
    val users = dao.getAll()
    users.forEach { user ->
        val fila = "${user.uid}: ${user.firstName ?: ""} - ${user.lastName ?: ""}\n"
        rpta += fila
    }
    return rpta
}

suspend fun AgregarUsuario(user: User, dao: UserDao) {
    try {
        dao.insert(user)
    } catch (e: Exception) {
        Log.e("User", "Error: insert: ${e.message}")
    }
}

suspend fun deleteLastUser(dao: UserDao) {
    try {
        val lastUser = dao.getLastUser()
        lastUser?.let {
            dao.delete(it)
        }
    } catch (e: Exception) {
        Log.e("User", "Error: delete: ${e.message}")
    }
}
