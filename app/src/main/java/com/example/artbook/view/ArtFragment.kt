package com.example.artbook.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artbook.R
import com.example.artbook.adapter.ArtRecyclerAdapter
import com.example.artbook.databinding.ArtsFragmentBinding
import com.example.artbook.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(
    val artRecyclerAdapter: ArtRecyclerAdapter,
) : Fragment(R.layout.arts_fragment) {

    private var fragmentBindind: ArtsFragmentBinding? = null
    lateinit var viewModel: ArtViewModel

    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArt = artRecyclerAdapter.arts[layoutPosition]
            viewModel.deleteArt(selectedArt) // deletes from Room database
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]

        val binding = ArtsFragmentBinding.bind(view)
        fragmentBindind = binding
        subscribeToObservers()

        binding.rvArts.adapter = artRecyclerAdapter
        binding.rvArts.layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.rvArts)

        setListeners()
    }

    private fun setListeners() {
        fragmentBindind?.fabAddArt?.setOnClickListener {
            /** dos formas de navegar */
            //  findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailFragment())
            findNavController().navigate(R.id.artDetailFragment)
        }
    }

    private fun subscribeToObservers() {
        viewModel.artList.observe(
            viewLifecycleOwner,
            Observer {
                artRecyclerAdapter.arts = it
            },
        )
    }

    override fun onDestroyView() {
        fragmentBindind = null
        super.onDestroyView()
    }
}
