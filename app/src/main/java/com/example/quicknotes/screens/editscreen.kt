package com.example.quicknotes.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.quicknotes.ViewModel
import com.example.quicknotes.database.Note
import kotlinx.coroutines.delay

import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editscreen( navHostController: NavHostController,id:Int,folderid:Int) {
    var loading by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        delay(500)
        loading = true
    }

    var title by remember {
        mutableStateOf("")
    }
    var body by remember {
        mutableStateOf("")
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



    viewmodel.getNoteById(id).observeAsState().value?.let {
        title = it.title
        body = it.body
    }


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    if (loading) {
        Scaffold(modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Edit Note") },
                    actions = {
                        IconButton(onClick = {
                            viewmodel.delete(
                                Note(
                                    id,
                                    title,
                                    body,
                                    folderid
                                )
                            );navHostController.popBackStack()
                        }) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")

                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navHostController.popBackStack()

                            },
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")

                        }
                    },
                    scrollBehavior = scrollBehavior

                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    viewmodel.update(
                        Note(
                            id,
                            title,
                            body,
                            folderid
                        )
                    );navHostController.popBackStack()
                },) {
                    Icon(imageVector = Icons.Filled.Save, contentDescription = "Save");
                    Text(text = "Save", modifier = Modifier.padding(start = 5.dp))

                }
            }


        ) {
            LazyColumn(modifier = Modifier.padding(it)) {
                item {


                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = MaterialTheme.typography.headlineLarge.copy(LocalContentColor.current),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 5.dp, end = 5.dp, bottom = 30.dp, top = 8.dp),


                        cursorBrush = SolidColor(LocalContentColor.current),


                        decorationBox = { innerTextField ->
                            Box() {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "Title",
                                        style = MaterialTheme.typography.headlineLarge
                                    )
                                }
                                innerTextField()

                            }
                        },
                    )
                    Divider(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        thickness = 0.5.dp
                    )


                    BasicTextField(
                        value = body,
                        onValueChange = { body = it },
                        textStyle = MaterialTheme.typography.titleLarge.copy(LocalContentColor.current),
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp, top = 15.dp)
                            .fillMaxSize(),
                        cursorBrush = SolidColor(LocalContentColor.current),

                        decorationBox = { innerTextField ->
                            Box() {
                                if (body.isEmpty()) {
                                    Text(text = "Body", style = MaterialTheme.typography.titleLarge)
                                }
                                innerTextField()

                            }
                        },
                    )
                }
            }

        }


    }
    else{
        Column(modifier=Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator()

        }
    }
}


