package com.example.sakto_do


import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import kotlinx.coroutines.runBlocking

object TodoWidgetUpdater {

    fun update(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(
            ComponentName(context, TodoWidgetProvider::class.java)
        )
        ids.forEach {
            updateSingle(context, manager, it)
        }
    }

    fun updateSingle(
        context: Context,
        manager: AppWidgetManager,
        widgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.todo_widget)

        val db = TodoDatabase.getDatabase(context)
        val todos = runBlocking {
            db.todoDao().getAllTodosOnce()
        }

        val text = if (todos.isEmpty()) {
            "No tasks 🎉"
        } else {
            todos.take(5).joinToString("\n") { "• ${it.text}" }
        }

        views.setTextViewText(R.id.widget_content, text)
        manager.updateAppWidget(widgetId, views)
    }
}
