package com.example.flo.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeIndicator1Binding

class HomeIndicator1Fragment : Fragment() {

    lateinit var binding : FragmentHomeIndicator1Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeIndicator1Binding.inflate(inflater, container, false)

        return binding.root
    }
}