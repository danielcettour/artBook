package com.example.artbook.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.artbook.R
import com.example.artbook.adapter.ImageRecyclerAdapter
import com.example.artbook.databinding.ApiImageSearchFragmentBinding
import com.example.artbook.utils.Status
import com.example.artbook.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageApiFragment @Inject constructor(
    val imageRecyclerAdapter: ImageRecyclerAdapter,
) : Fragment(R.layout.api_image_search_fragment) {

    // private val viewModel: ArtViewModel by activityViewModels()
    lateinit var viewModel: ArtViewModel

    private var fragmentBinding: ApiImageSearchFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]

        val binding = ApiImageSearchFragmentBinding.bind(view)
        fragmentBinding = binding

        setListeners()
        subscribeToObservers()
    }

    private fun setListeners() {
        var job: Job? = null

        fragmentBinding?.etSearch?.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        fragmentBinding?.rvArtsSearchResult?.adapter = imageRecyclerAdapter
        fragmentBinding?.rvArtsSearchResult?.layoutManager = GridLayoutManager(requireContext(), 3)

        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }
    }

    private fun subscribeToObservers() {
        viewModel.imageList.observe(
            viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        val urls = it.data?.hits?.map { imageResult -> imageResult.previewURL }
                        imageRecyclerAdapter.images = urls ?: listOf()
                        fragmentBinding?.pbImageSearch?.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                        fragmentBinding?.pbImageSearch?.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        fragmentBinding?.pbImageSearch?.visibility = View.VISIBLE
                    }
                }
            },
        )
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}
