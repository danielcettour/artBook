package com.example.artbook.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.artbook.R
import com.example.artbook.databinding.ArtsFragmentBinding

class ArtFragment : Fragment(R.layout.arts_fragment) {

    private var fragmentBindind: ArtsFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ArtsFragmentBinding.bind(view)
        fragmentBindind = binding
        setListeners()
    }

    private fun setListeners() {
        fragmentBindind?.fabAddArt?.setOnClickListener {
            /** dos formas de navegar */
            //  findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailFragment())
            findNavController().navigate(R.id.artDetailFragment)
        }
    }

    override fun onDestroyView() {
        fragmentBindind = null
        super.onDestroyView()
    }
}
