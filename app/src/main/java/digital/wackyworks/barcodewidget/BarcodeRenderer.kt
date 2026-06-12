package digital.wackyworks.barcodewidget

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.oned.Code128Writer

/** Renders text as a Code 128 barcode bitmap. */
object BarcodeRenderer {

    const val WIDTH = 600
    const val HEIGHT = 140

    /** Returns null if the content can't be encoded (e.g. non-ASCII characters). */
    fun render(content: String, width: Int = WIDTH, height: Int = HEIGHT): Bitmap? {
        val matrix = try {
            Code128Writer().encode(
                content,
                BarcodeFormat.CODE_128,
                width,
                height,
                mapOf(EncodeHintType.MARGIN to 8)
            )
        } catch (e: IllegalArgumentException) {
            return null
        }
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }

    fun canEncode(content: String): Boolean = try {
        Code128Writer().encode(content, BarcodeFormat.CODE_128, 1, 1)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}
