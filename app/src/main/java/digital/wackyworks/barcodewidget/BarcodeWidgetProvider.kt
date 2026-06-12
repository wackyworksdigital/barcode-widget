package digital.wackyworks.barcodewidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews

class BarcodeWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { updateWidget(context, appWidgetManager, it) }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        appWidgetIds.forEach { WidgetStore.clear(context, it) }
    }

    companion object {

        fun updateWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_barcode)
            bindSlot(context, views, widgetId, Slot.TOP, R.id.top_cell, R.id.top_barcode, R.id.top_text)
            bindSlot(context, views, widgetId, Slot.BOTTOM, R.id.bottom_cell, R.id.bottom_barcode, R.id.bottom_text)
            manager.updateAppWidget(widgetId, views)
        }

        private fun bindSlot(
            context: Context,
            views: RemoteViews,
            widgetId: Int,
            slot: Slot,
            cellId: Int,
            barcodeId: Int,
            textId: Int
        ) {
            val code = WidgetStore.code(context, widgetId, slot)
            val bitmap = if (code.isNotEmpty()) BarcodeRenderer.render(code) else null

            if (bitmap != null) {
                views.setViewVisibility(barcodeId, View.VISIBLE)
                views.setImageViewBitmap(barcodeId, bitmap)
                views.setTextViewText(textId, code)
            } else {
                views.setViewVisibility(barcodeId, View.GONE)
                views.setTextViewText(textId, context.getString(R.string.tap_to_set))
            }

            val intent = Intent(context, EnterCodeActivity::class.java).apply {
                putExtra(EnterCodeActivity.EXTRA_WIDGET_ID, widgetId)
                putExtra(EnterCodeActivity.EXTRA_SLOT, slot.name)
                // Unique data URI so PendingIntents for different slots don't collide.
                data = Uri.parse("barcodewidget://slot/$widgetId/${slot.name}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(cellId, pendingIntent)
        }
    }
}
