package digital.wackyworks.barcodewidget

import android.app.Activity
import android.os.Bundle

/** Minimal launcher screen that just explains how to add the widget. */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
