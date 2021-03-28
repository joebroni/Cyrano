package com.corgrimm.goodhusband.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.corgrimm.goodhusband.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var isFABOpen: Boolean = false
    lateinit var fabDoneDeed : FloatingActionButton
    lateinit var fabAddReminder : FloatingActionButton
    lateinit var fabAdd3 : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        Thread(Runnable {
            // background work here ...
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
            }, 10000) //it will wait 10 sec before upate ui
        }).start()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showFABMenu() {
        isFABOpen = true
        fabDoneDeed.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fabAddReminder.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        fabAdd3.animate().translationY(-resources.getDimension(R.dimen.standard_155))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fabDoneDeed.animate().translationY(0f)
        fabAddReminder.animate().translationY(0f)
        fabAdd3.animate().translationY(0f)
    }
}