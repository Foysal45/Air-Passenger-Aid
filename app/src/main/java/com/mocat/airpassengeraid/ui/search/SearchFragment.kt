package com.mocat.airpassengeraid.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.databinding.FragmentHomeBinding
import com.mocat.airpassengeraid.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentSearchBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}