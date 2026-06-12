package digital.wackyworks.barcodewidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText

/** Small dialog shown when a widget slot is tapped. Enter a code, press Enter, done. */
class EnterCodeActivity : Activity() {

    companion object {
        const val EXTRA_WIDGET_ID = "widget_id"
        const val EXTRA_SLOT = "slot"
    }

    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var slot: Slot
    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        widgetId = intent.getIntExtra(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        slot = Slot.valueOf(intent.getStringExtra(EXTRA_SLOT) ?: Slot.TOP.name)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContentView(R.layout.activity_enter_code)
        setTitle(if (slot == Slot.TOP) R.string.title_top else R.string.title_bottom)

        input = findViewById(R.id.code_input)
        input.setText(WidgetStore.code(this, widgetId, slot))
        input.setSelection(input.text.length)

        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save()
                true
            } else {
                false
            }
        }
        findViewById<Button>(R.id.save_button).setOnClickListener { save() }
    }

    private fun save() {
        val code = input.text.toString().trim()
        if (code.isNotEmpty() && !BarcodeRenderer.canEncode(code)) {
            input.error = getString(R.string.error_unsupported)
            return
        }
        WidgetStore.setCode(this, widgetId, slot, code)
        BarcodeWidgetProvider.updateWidget(this, AppWidgetManager.getInstance(this), widgetId)
        finish()
    }
}
