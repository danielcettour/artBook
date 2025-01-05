package com.example.artbook.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.artbook.R
import com.example.artbook.databinding.ArtDetailFragmentBinding

class ArtDetailFragment : Fragment(R.layout.art_detail_fragment) {

    private var fragmentBinding: ArtDetailFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = ArtDetailFragmentBinding.bind(view)
        fragmentBinding = binding
        setListeners()
        setBackPressedCallback()
    }

    private fun setListeners() {
        fragmentBinding?.ivChosenArt?.setOnClickListener {
            /** dos formas de navegar */
            findNavController().navigate(ArtDetailFragmentDirections.actionArtDetailFragmentToImageApiFragment())
            // findNavController().navigate(R.id.imageApiFragment)
        }
    }

    /**
     * un callback custom para manejar eventos al presionar botón hacia atrás
     */
    private fun setBackPressedCallback() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("////", "back is pressed!")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}
