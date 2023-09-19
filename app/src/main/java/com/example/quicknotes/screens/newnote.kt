package com.example.quicknotes.screens

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.quicknotes.ViewModel
import com.example.quicknotes.database.Note

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun newnote(navController: NavHostController){


    val context= LocalContext.current
    val viewmodel= viewModel<ViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ViewModel(
                    context
                ) as T
            }
        }
    )
    var body by remember {
        mutableStateOf("")
    }
    var title by remember {
        mutableStateOf("")
    }
    val scrollBehavior=TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (modifier= Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = { Text(text = "New Note") },
                navigationIcon = {
                    IconButton(onClick = {
                                         navController.popBackStack()

                    },) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        
                    }
                },
                scrollBehavior=scrollBehavior

            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                if(title=="" && body=="") {
                    navController.popBackStack()
             }
                else{
                    viewmodel.insert(Note(0, title, body))
                    navController.popBackStack()

                }
                },) {
                Icon(imageVector = Icons.Filled.Save, contentDescription = "Save");
                Text(text = "Save",modifier=Modifier.padding(start = 5.dp))

            }
        }



    ) {
        LazyColumn (modifier=Modifier.padding(it)){
            item {


                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(fontSize = 30.sp, color = LocalContentColor.current, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxSize().padding(start = 5.dp,end=5.dp, bottom = 30.dp, top = 8.dp),


                    cursorBrush = SolidColor(LocalContentColor.current),


                    decorationBox = { innerTextField ->
                        Box() {
                            if(title.isEmpty()){
                                Text(text = "Title", fontSize = 30.sp)
                            }
                            innerTextField()

                        }
                    },
                )
                Divider(modifier = Modifier.padding(start = 8.dp, end = 8.dp), thickness = 0.5.dp)


                BasicTextField(
                    value = body,
                    onValueChange = { body = it },
                    textStyle = TextStyle(color = LocalContentColor.current, fontSize = 25.sp),
                    modifier = Modifier.padding(start = 5.dp,end=5.dp,  top = 15.dp).fillMaxSize(),
                    cursorBrush = SolidColor(LocalContentColor.current),

                    decorationBox = { innerTextField ->
                        Box() {
                            if(body.isEmpty()){
                                Text(text = "Body", fontSize = 25.sp)
                            }
                            innerTextField()

                        }
                    },
                )
            }
        }

    }

}