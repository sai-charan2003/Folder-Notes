package com.example.quicknotes.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.glance.Image
import androidx.glance.ImageProvider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quicknotes.ViewModel
import com.example.quicknotes.database.foldername
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Scope
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun folderscreen(navController1: NavHostController) {




    var loading by remember {
        mutableStateOf(false)
    }
    var renamefolder by remember {
        mutableStateOf(false)
    }



    val snackbarHostState = remember { SnackbarHostState() }
    var context = LocalContext.current

//    val sharedPreferences:SharedPreferences=context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
//    val editor=sharedPreferences.edit()
//    editor.putBoolean("showsnackbar",true)
//    editor.apply()

    var itemheight by remember {
        mutableStateOf(0.dp)
    }
    var alertbox by remember {
        mutableStateOf(false)
    }
    var foldern by remember {
        mutableStateOf("")
    }
    var renamename by remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }

    val viewmodel = viewModel<ViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ViewModel(
                    context
                ) as T
            }
        }
    )
    var haptic= LocalHapticFeedback.current
    var showSnackbar by remember {
        mutableStateOf(viewmodel.sharedPreferences.getBoolean("showsnackbar",true))
    }
    var version by remember {
        mutableStateOf("0")
    }
    var pressoffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var isContextmenuvisible by remember {
        mutableStateOf(false)
    }
    var downloadlink by remember {
        mutableStateOf("null")
    }
    var newversion by remember {
        mutableStateOf(false)
    }
    var list = remember {
        mutableListOf<Any>()
    }
    var link by remember {
        mutableStateOf("null")
    }
    var showmenu by remember {
        mutableStateOf(
            false
        )
    }
    val density= LocalDensity.current

    var appversion= "2.0"
    var AlertDialog by remember {
        mutableStateOf(false)
    }


    var showbottomsheet by remember {
        mutableStateOf(false)
    }
    val movetonextscreen by remember {
        mutableStateOf(false)

    }
    var folderid by remember {
        mutableStateOf(0)

    }
    var foldername by remember {
        mutableStateOf("")
    }

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val reference = firebaseDatabase.reference

    reference.addValueEventListener(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {


            if (snapshot.exists()) {

                for (data in snapshot.children) {

                    data.value?.let { list.add(it) }

                }


                link = list[1].toString()
                version = list[2].toString()
                downloadlink = list[0].toString()


                if (appversion != version) {
                    newversion = true

                }


            }

        }

        override fun onCancelled(error: DatabaseError) {

        }
    })
    LaunchedEffect(key1 = Unit) {
        delay(500)
        loading = true

        Log.d("TAG", "folderscreen: $showSnackbar")

        if (showSnackbar) {
            if (version != appversion) {
                val result = snackbarHostState.showSnackbar(
                    "New Version Available", actionLabel = "Update",
                    duration = SnackbarDuration.Short
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        AlertDialog = true
                    }

                    SnackbarResult.Dismissed -> {
                        /* Handle snackbar dismissed */
                    }
                }







            }






        }


    }


    if (loading) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },


            topBar = {
                TopAppBar(title = {
                    Text(text = "Quick Notes")
                },
                    actions = {
                        IconButton(onClick = { showmenu = !showmenu }) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More")

                        }
                        DropdownMenu(expanded = showmenu, onDismissRequest = { showmenu = false }) {
                            DropdownMenuItem(
                                text = { Text(text = "Check for Updates") },
                                onClick = {
                                    AlertDialog = true
                                    showmenu = false

                                })
                            DropdownMenuItem(
                                text = { Text(text = "What's New") },
                                onClick = {
                                    showbottomsheet = true
                                    showmenu = false

                                })

                        }


                    }


                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { alertbox = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)

                }
            }



        ) {



            val folderdatastore by viewmodel.foldernames.observeAsState(initial = emptyList())
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier
                    .fillMaxSize()
                    .padding(it)


            ) {
                items(folderdatastore.size) {foldernumber->
                    Card(

                        modifier = Modifier








                            .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),

//                        onClick = {
//                            navController1.navigate("home/${folderdatastore[it].id}")
//
//                                  },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                        ),



                    ) {



                        Row(
                            modifier = Modifier
                                .pointerInput(true) {
                                    detectTapGestures(
                                        onTap = {
                                            navController1.navigate("home/${folderdatastore[foldernumber].id}")
                                        },
                                        onLongPress = {
                                            folderid = folderdatastore[foldernumber].id
                                            foldername = folderdatastore[foldernumber].name



                                            isContextmenuvisible = true
                                            pressoffset = DpOffset(it.x.toDp(), it.y.toDp())
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)


                                        }
                                    )

                                }


                                .fillMaxSize()
                                .padding(20.dp),

                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Folder,
                                contentDescription = "Folder",
                                modifier = Modifier.padding(end = 3.dp)
                            )
                            Text(
                                text = folderdatastore[foldernumber].name,


                                )


                        }


                    }

                }

            }
            if (alertbox) {
                AlertDialog(
                    onDismissRequest = { alertbox = false;foldern = "" },
                    confirmButton = {
                        Button(onClick = {
                            viewmodel.insertfolderdata(
                                foldername(
                                    id = 0,
                                    name = foldern
                                )
                            );alertbox = false;foldern = ""
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Create Folder")

                        }
                        OutlinedButton(
                            onClick = { alertbox = false;foldern = "" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Cancel")

                        }
                    },
                    title = {
                        Text(
                            text = "New folder",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        OutlinedTextField(value = foldern, onValueChange = {
                            foldern = it

                        }, modifier = Modifier.focusRequester(focusRequester))
                    },


                    )
                LaunchedEffect(focusRequester) {
                    awaitFrame()
                    focusRequester.requestFocus()
                }
            }


            if (AlertDialog) {
                ModalBottomSheet(onDismissRequest = { AlertDialog = false }) {
                    Column(modifier = Modifier.padding(start = 10.dp, end = 5.dp)) {

                        if (newversion) {
                            Text(
                                text = "New Version Available",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                textAlign = TextAlign.Center,
                                color = Color.Red
                            )
                        } else {
                            Text(
                                text = "You are up to date",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        val context = LocalContext.current
                        val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("$link")) }
                        if (newversion) {
                            Text(text = "New Version: $version",)
                        } else {
                            Text(text = "Version: $version",)

                        }
                        Row() {
                            Button(
                                onClick = { context.startActivity(intent) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Project On Github")

                            }

                        }

                        if (newversion) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Bottom
                            ) {


                                val context = LocalContext.current
                                val intent = remember {
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("$downloadlink")
                                    )
                                }

                                Button(onClick = {
                                    context.startActivity(intent)
                                }, modifier = Modifier.padding(bottom = 10.dp)) {
                                    Text(text = "Update")

                                }
                            }
                        }

                    }

                }
            }

            if (showbottomsheet) {
                ModalBottomSheet(onDismissRequest = { showbottomsheet = false }) {
                    Column(modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)) {
                        Text(
                            text = "🆕 New logo and new app name",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )


                        Text(
                            text = "📁 Folder based notes storing",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Text(
                            text = "💥 Improved animation",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Text(text = "📨 New update indicator when new version is released",modifier = Modifier.padding(bottom = 10.dp))
                        Text(text = "📲 Long press to show folder and notes settings",modifier = Modifier.padding(bottom = 10.dp))
                        Text(
                            text = "⬆️ New update UI with brand new what's new page",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )


                    }

                }
            }
            if(renamefolder){
                AlertDialog(
                    onDismissRequest = { renamefolder = false;renamename = "" },
                    confirmButton = {
                        Button(onClick = {
                            viewmodel.update(
                                foldername(
                                    id = folderid,
                                    name = renamename
                                )
                            );renamefolder = false;renamename = ""
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Rename folder")

                        }
                        OutlinedButton(
                            onClick = { renamefolder = false;foldern = "" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Cancel")

                        }
                    },
                    title = {
                        Text(
                            text = "Rename folder",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        OutlinedTextField(value = renamename, onValueChange = {
                            renamename = it

                        }, modifier = Modifier.focusRequester(focusRequester))
                    },


                    )
                LaunchedEffect(focusRequester) {
                    awaitFrame()
                    focusRequester.requestFocus()
                }
            }
            if(isContextmenuvisible){
                ModalBottomSheet(onDismissRequest = { isContextmenuvisible=false}) {
                    Column (modifier= Modifier
                        .fillMaxWidth()
                        .padding(10.dp)){


                        FilledTonalButton(onClick = { viewmodel.delete(foldername(folderid,foldername));isContextmenuvisible=false },modifier=Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete Folder"
                            )
                            Text("Delete folder")

                        }
                        FilledTonalButton(onClick = {renamefolder=true;isContextmenuvisible=false },modifier= Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.TextFields,
                                contentDescription = "Rename"
                            )
                            Text(text = "Rename folder")

                        }
                    }


                }
            }


        }


    }
    else{
        Column(modifier=Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator()

        }
    }
    DisposableEffect(key1 = Unit){
        onDispose {
            viewmodel.editor.putBoolean("showsnackbar",false).apply()
        }
    }







}







