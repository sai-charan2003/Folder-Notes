package com.example.quicknotes

import android.content.Context
import androidx.compose.foundation.background

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import androidx.navigation.NavHostController

object newnotewidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {


        provideContent {
            Column {


                Image(
                    provider = ImageProvider(R.drawable.add_new_note,),
                    contentDescription = "Add New Note",
                    colorFilter = ColorFilter.tint(
                        colorProvider = ColorProvider(
                            Color(0xFFced4da)
                        )
                    ),
                    modifier = GlanceModifier.clickable(onClick = actionStartActivity<MainActivity>())
                )
            }


        }
    }
}
class newnote: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = newnotewidget
}
