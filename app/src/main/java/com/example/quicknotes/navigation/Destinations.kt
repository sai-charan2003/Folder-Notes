package com.example.quicknotes.navigation



sealed class Destinations(val route: String){
    object Home: Destinations("home/{folderid}")
    object newnote: Destinations("newnote/{folderid}")
    object editnote:Destinations("editnote/{id}/{folderid}")
    object folderscreen:Destinations("folder")
}
