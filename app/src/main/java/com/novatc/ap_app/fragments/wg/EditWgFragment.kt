package com.novatc.ap_app.fragments.wg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import com.google.zxing.BarcodeFormat

import android.graphics.Bitmap
import android.widget.ImageView
import com.novatc.ap_app.R
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_edit_wg.view.*


@AndroidEntryPoint
class EditWgFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_wg, container, false)
        generateQrCode(view)
        return view
    }

    private fun generateQrCode(view: View) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap("www.wikipedia.de", BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode: ImageView = view.img_wg_invitation_code as ImageView
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
        }
    }

}