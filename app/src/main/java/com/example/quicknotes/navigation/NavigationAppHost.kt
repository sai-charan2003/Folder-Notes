package com.example.quicknotes.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quicknotes.screens.editscreen
import com.example.quicknotes.screens.folderscreen

import com.example.quicknotes.screens.home
import com.example.quicknotes.screens.newnote


@SuppressLint("SuspiciousIndentation")
@Composable

fun NavigationAppHost(navController: NavHostController) {
    val context= LocalContext.current
    NavHost(navController = navController, startDestination = Destinations.folderscreen.route, enterTransition = { EnterTransition.None}, exitTransition = { ExitTransition.None}){
        composable(Destinations.Home.route,  arguments = listOf(navArgument("folderid"){
            type= NavType.IntType
        })){

            it.arguments?.getInt("folderid")?.let { it2 -> home(navController, folderid = it2) }
        }
        composable(Destinations.folderscreen.route, ){

            folderscreen(navController)
        }
        composable(Destinations.newnote.route,
            arguments = listOf(navArgument("folderid"){
                type= NavType.IntType
            })){
            it.arguments?.getInt("folderid")?.let { it2 -> newnote(navController, folderid = it2) }

        }
        composable(route=Destinations.editnote.route,
            arguments = listOf(navArgument("id"){
            type= NavType.IntType
        },
                navArgument("folderid"){
                    type=NavType.IntType
                }


            )){
            val noteid=it.arguments?.getInt("id")
            val folderid=it.arguments?.getInt("folderid")
            if (noteid != null) {
                if (folderid != null) {
                    editscreen(navHostController = navController, id = noteid, folderid = folderid)
                }
            }


        }
    }

}