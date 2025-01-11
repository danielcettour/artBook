package com.example.artbook.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.artbook.R
import com.example.artbook.databinding.ArtDetailFragmentBinding
import com.example.artbook.utils.Status
import com.example.artbook.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailFragment @Inject constructor(private val glide: RequestManager) : Fragment(R.layout.art_detail_fragment) {

    private var fragmentBinding: ArtDetailFragmentBinding? = null
    lateinit var viewModel: ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]
        val binding = ArtDetailFragmentBinding.bind(view)
        fragmentBinding = binding
        subscribeToObservers()
        setListeners()
        setBackPressedCallback()
    }

    private fun setListeners() {
        fragmentBinding?.ivChosenArt?.setOnClickListener {
            /** dos formas de navegar */
            findNavController().navigate(ArtDetailFragmentDirections.actionArtDetailFragmentToImageApiFragment())
            // findNavController().navigate(R.id.imageApiFragment)
        }
        fragmentBinding?.btSave?.setOnClickListener {
            viewModel.makeArt(
                fragmentBinding?.etName?.text.toString(),
                fragmentBinding?.etArtist?.text.toString(),
                fragmentBinding?.etYear?.text.toString(),
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.selectedImageUrl.observe(
            viewLifecycleOwner,
            Observer { url ->
                println(url)
                fragmentBinding?.let {
                    glide.load(url).into(it.ivChosenArt)
                }
            },
        )

        viewModel.insertArtMessage.observe(
            viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireActivity(), "Success", Toast.LENGTH_LONG).show()
                        findNavController().navigateUp()
                        viewModel.resetInsertArtMsg()
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                    }

                    Status.LOADING -> {
                    }
                }
            },
        )
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
