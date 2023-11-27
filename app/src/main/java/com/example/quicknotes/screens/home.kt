package com.example.quicknotes.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quicknotes.ViewModel
import com.example.quicknotes.database.Note
import com.example.quicknotes.database.foldername
import com.example.quicknotes.navigation.Destinations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun home(navController1: NavHostController,folderid:Int) {
    var loading by remember {
        mutableStateOf(false)
    }
    val haptic = LocalHapticFeedback.current
    var renamename by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        delay(500)
        loading = true
    }
    var renamefolder by remember {
        mutableStateOf(false)
    }
    var noteid by remember {
        mutableStateOf(0)
    }
    var notetitle by remember {
        mutableStateOf("")
    }
    var notebody by remember {
        mutableStateOf("")
    }
    var interactionSource = remember {
        MutableInteractionSource()
    }


    var context = LocalContext.current
    val viewmodel = viewModel<ViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ViewModel(
                    context
                ) as T
            }
        }
    )


    val folderdata by viewmodel.foldernames.observeAsState(initial = emptyList())
    val folderdatastore by viewmodel.getNotesInFolder(folderid)
        .observeAsState(initial = emptyList())
    val foldernames by viewmodel.getfolderbyid(folderid).observeAsState()
    val focusRequester = remember { FocusRequester() }
    var notesettings by remember {
        mutableStateOf(false)
    }





    if (loading) {



        Scaffold(
            topBar = {
                TopAppBar(title = {
                    foldernames?.let { Text(text = it.name,modifier=Modifier.clickable {
                        renamefolder=true

                    }) }
                },
                    actions = {
                        IconButton(onClick = {
                            viewmodel.delete(
                                foldername(
                                    folderid,
                                    folderdata[0].name
                                )
                            );navController1.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete Notes and Folder"
                            )

                        }

                    }


                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController1.navigate("newnote/${folderid}")

                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)

                }
            }

        ) {
            if (folderdatastore.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No Notes")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(folderdatastore.size) {notes->

                        OutlinedCard(
                             modifier = Modifier

                                 .pointerInput(true) {

                                     detectTapGestures(
                                         onTap = {
                                             val noteId = folderdatastore[notes].id

                                             navController1.navigate("editnote/$noteId/$folderid")

                                         },
                                         onLongPress = {
                                             haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                             noteid = folderdatastore[notes].id
                                             notebody = folderdatastore[notes].body
                                             notetitle = folderdatastore[notes].title
                                             notesettings = true

                                         },


                                     )

                                 }


                                 .height(250.dp)
                                 .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 10.dp)) {
                            Column() {
                                Text(
                                    text = folderdatastore[notes].title,
                                    modifier = Modifier
                                        .fillMaxWidth()

                                        .padding(bottom = 10.dp),
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    maxLines = 1
                                )

                                Text(
                                    text = folderdatastore[notes].body, modifier = Modifier
                                        .fillMaxWidth()

                                        .padding(5.dp), fontSize = 15.sp, maxLines = 10
                                )
                            }
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
                    onClick = { renamefolder = false;renamename = "" },
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
    if(notesettings){
        ModalBottomSheet(onDismissRequest = { notesettings=false }) {
            Column (modifier= Modifier
                .fillMaxWidth()
                .padding(10.dp)){


                FilledTonalButton(onClick = { viewmodel.delete(Note(noteid,notebody,notetitle,folderid));notesettings=false},modifier=Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Note"
                    )
                    Text("Delete note")

                }
                FilledTonalButton(onClick = {val noteId = noteid

                    navController1.navigate("editnote/$noteId/$folderid")
                    ;notesettings=false },modifier= Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = "Rename"
                    )
                    Text(text = "Edit note")

                }
            }


        }
    }
}



