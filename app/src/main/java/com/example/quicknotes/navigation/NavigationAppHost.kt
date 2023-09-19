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

import com.example.quicknotes.screens.home
import com.example.quicknotes.screens.newnote


@SuppressLint("SuspiciousIndentation")
@Composable

fun NavigationAppHost(navController: NavHostController) {
    val context= LocalContext.current
    NavHost(navController = navController, startDestination = Destinations.Home.route, enterTransition = { EnterTransition.None}, exitTransition = { ExitTransition.None}){
        composable(Destinations.Home.route, ){

            home(navController)
        }
        composable(Destinations.newnote.route,enterTransition = {
            fadeIn(
                animationSpec = tween(
                    200, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = LinearEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }){
            newnote(navController)

        }
        composable(route=Destinations.editnote.route, enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = LinearEasing),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = LinearEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },arguments = listOf(navArgument("id"){
            type= NavType.IntType
        })){
            Log.d("TAG", it.arguments?.getInt("id").toString())
            it.arguments?.getInt("id")?.let { it1 -> editscreen(navController,id = it1) }
        }
    }

}