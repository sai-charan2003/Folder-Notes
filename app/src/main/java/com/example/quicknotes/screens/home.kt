package com.example.quicknotes.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.quicknotes.ViewModel
import com.example.quicknotes.database.Note
import com.example.quicknotes.navigation.Destinations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun home(navController1: NavHostController){


    var context= LocalContext.current
    val viewmodel= viewModel<ViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ViewModel(
                    context
                ) as T
            }
        }
    )

    var title by rememberSaveable  {
        mutableStateOf("null")
    }
    val body by rememberSaveable  {
        mutableStateOf("null")
    }
    var totaldata by remember {
        mutableStateOf(0)
    }
    var newversion by remember {
        mutableStateOf(false)
    }
    var notedata= remember {
        mutableListOf<Note>()
    }
    var showmenu by remember {
        mutableStateOf(false)
    }
    var showbox by remember {
        mutableStateOf(false)
    }
    var link by remember {
        mutableStateOf("")
    }
    var version by remember {
        mutableStateOf("")
    }
    var downloadlink by remember {
        mutableStateOf("")
    }


    val noteDataState by viewmodel.allnotes.observeAsState(initial = emptyList())
    Log.d("TAG", "home: $noteDataState")
    val firebase=FirebaseDatabase.getInstance()
    var list = remember {
        mutableListOf<Any>()
    }

val reference=firebase.getReference()
reference.addValueEventListener(object:ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
        if(snapshot.exists()){
            for(data in snapshot.children){

                data.value?.let { list.add(it) }

            }
            link= list[1].toString()
            version= list[2].toString()
            downloadlink=list[0].toString()
            val appversion= "1"
            if(appversion!=version){
                newversion=true

            }

        }
    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }

})
    val note= Note(0,"HI","Bye")
    Scaffold(
        topBar = {
                 TopAppBar(title = { Text(text = "Quick Notes")
                 },
                     actions = {
                         IconButton(onClick = { showmenu=!showmenu }) {
                             Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More")

                         }
                         DropdownMenu(expanded = showmenu, onDismissRequest = { showmenu=false}) {
                             DropdownMenuItem(text = { Text(text = "Check for Updates") }, onClick = { showbox=true })

                             
                         }
                     }
                     


                 )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController1.navigate(Destinations.newnote.route)}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)

            }
        }

    ) {
      if(noteDataState.isEmpty()){
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Text(text = "No Notes")
          }
      }

        }
    if(showbox){
        androidx.compose.material3.AlertDialog(
            modifier = Modifier.fillMaxWidth(),

            onDismissRequest = { showbox=false },


            confirmButton = {
                val context = LocalContext.current
                val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("$downloadlink")) }

                if(newversion){

                    TextButton(onClick = {
                        context.startActivity(intent)
                    }) {
                        Text(text="Update")

                    }
                }
            },
            title = {
                if(newversion){
                    Text(text = "New Version Available", color = Color.Red, textAlign = TextAlign.Center)
                }
                else{
                    Text(text = "You are up to date")
                }

            },
            text = {


                Column (modifier =  Modifier.fillMaxWidth()) {
                    val context = LocalContext.current
                    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("$link")) }
                    Text(text = "Version: $version",modifier=Modifier.padding(bottom = 8.dp))
                    Text(text = "Link : ", modifier = Modifier.fillMaxWidth())
                    Text(text="$link",modifier=Modifier.clickable { context.startActivity(intent) },color= Color(0xFF2f81f7)
                    )
                }

            },




            )
    }


    }



