package com.novatc.ap_app.fragments.wg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_wg.view.*
import android.widget.Toast

import com.journeyapps.barcodescanner.ScanContract

import com.journeyapps.barcodescanner.ScanOptions

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanIntentResult
import com.novatc.ap_app.activities.QrScannerActivity
import com.novatc.ap_app.adapter.WgAdapter
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.Wg
import com.novatc.ap_app.viewModels.wg.WgViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class WgFragment : Fragment(), WgAdapter.OnWgClickListener {

    @ExperimentalCoroutinesApi
    private val model: WgViewModel by viewModels()
    private var wgList: List<Wg> = emptyList()
    private lateinit var wgId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_wg, container, false)
        setupScanInvitationListener(view)
        fillWgList(view)
        setupOnJoinWgListener(view)
        return view
    }

    @ExperimentalCoroutinesApi
    private fun setupScanInvitationListener(view: View) {
        val barcodeLauncher = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents != null) {
                joinWg(result.contents)
            }
        }
        view.scanInvitationQrCodeButton.setOnClickListener {
            val options = ScanOptions()
            options.captureActivity = QrScannerActivity::class.java
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt("Scan a barcode")
            options.setCameraId(0) // Use a specific camera of the device
            options.setBeepEnabled(false)
            options.setBarcodeImageEnabled(true)
            options.setOrientationLocked(false)
            barcodeLauncher.launch(options)
        }
    }

    override fun onItemClick(position: Int) {
        val wg = wgList[position]
        view?.wgJoinButton?.isEnabled = true
        view?.wgJoinButton?.alpha = 1.0F
        wgId = wg.id
    }

    private fun joinWg(wgId: String) {
        model.joinWg(wgId)
        model.joinWgRequest.observe(this, {request ->
            if (request.status == Request.Status.SUCCESS) {
                Toast.makeText(context!!, R.string.wg_list_join_success, Toast.LENGTH_SHORT).show()
                findNavController().navigate(WgFragmentDirections.actionWgFragmentToWgDetailFragment(wgId))
            } else {
                Toast.makeText(context!!, request.message!!, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupOnJoinWgListener(view: View) {
        view.wgJoinButton.setOnClickListener {
            joinWg(wgId)
        }
    }

    @ExperimentalCoroutinesApi
    private fun fillWgList(view: View) {
        val recyclerView: RecyclerView = view.wgJoinItems
        val wgAdapter = WgAdapter(this)
        recyclerView.adapter = wgAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        model.wgs.observe(this, {wgs ->
            wgAdapter.differ.submitList(wgs)
            wgList = wgs
        })
    }


}