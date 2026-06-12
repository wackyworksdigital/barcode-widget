package digital.wackyworks.barcodewidget

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BarcodeRendererTest {

    @Test
    fun `accepts digits`() {
        assertTrue(BarcodeRenderer.canEncode("0123456789"))
    }

    @Test
    fun `accepts letters and symbols`() {
        assertTrue(BarcodeRenderer.canEncode("Waitrose-LOC_42/A!"))
    }

    @Test
    fun `rejects non-ascii characters`() {
        assertFalse(BarcodeRenderer.canEncode("héllo"))
    }
}
