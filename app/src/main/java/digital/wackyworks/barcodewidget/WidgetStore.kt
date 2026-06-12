package digital.wackyworks.barcodewidget

import android.content.Context
import android.content.SharedPreferences

/** Persists the code entered for each widget slot. */
object WidgetStore {

    private const val FILE = "barcode_widget"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    private fun key(widgetId: Int, slot: Slot) = "code_${widgetId}_${slot.name}"

    fun code(context: Context, widgetId: Int, slot: Slot): String =
        prefs(context).getString(key(widgetId, slot), "") ?: ""

    fun setCode(context: Context, widgetId: Int, slot: Slot, code: String) {
        prefs(context).edit().putString(key(widgetId, slot), code).apply()
    }

    fun clear(context: Context, widgetId: Int) {
        prefs(context).edit()
            .remove(key(widgetId, Slot.TOP))
            .remove(key(widgetId, Slot.BOTTOM))
            .apply()
    }
}
