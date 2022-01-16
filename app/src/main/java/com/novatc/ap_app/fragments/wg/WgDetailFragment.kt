package com.novatc.ap_app.fragments.wg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import com.google.zxing.BarcodeFormat

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.novatc.ap_app.R
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.Wg
import com.novatc.ap_app.viewModels.wg.WgDetailViewModel
import kotlinx.android.synthetic.main.fragment_wg_detail.view.*


@AndroidEntryPoint
class WgDetailFragment : Fragment() {

    private val model: WgDetailViewModel by viewModels()
    private val args by navArgs<WgDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wg_detail, container, false)
        model.getWg(args.wgId)
        model.getWgRequest.observe(this, { request ->
            if (request.status == Request.Status.SUCCESS) {
                val wg: Wg = request.data!!
                generateQrCode(view, wg.id)
                view.wgDetailWgName.text = wg.name
                view.wgDetailWgSlogan.text = wg.slogan
            }
        })
        setupLeaveWgListener(view)
        return view
    }

    private fun generateQrCode(view: View, wgId: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(wgId, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode: ImageView = view.wgDetailQrCode as ImageView
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
        }
    }

    private fun setupLeaveWgListener(view: View) {
        view.wgDetailWgLeaveButton.setOnClickListener {
            model.leaveWg()
            model.leaveWgRequest.observe(this, { request ->
                if (request.status == Request.Status.SUCCESS) {
                    Toast.makeText(context!!, R.string.wg_list_leave_success, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(WgDetailFragmentDirections.actionWgDetailFragmentToFragmentProfile())
                } else {
                    Toast.makeText(context!!, request.message!!, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

}