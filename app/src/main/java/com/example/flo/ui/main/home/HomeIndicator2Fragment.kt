package com.example.flo.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeIndicator2Binding

class HomeIndicator2Fragment : Fragment() {

    lateinit var binding : FragmentHomeIndicator2Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeIndicator2Binding.inflate(inflater, container, false)

        return binding.root
    }
}