package com.example.quicknotes.navigation



sealed class Destinations(val route: String){
    object Home: Destinations("home")
    object newnote: Destinations("newnote")
    object editnote:Destinations("editnote/{id}")
}
