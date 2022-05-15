package com.automotivecodelab.websockettest.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.automotivecodelab.websockettest.databinding.MainFragmentBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private var binding: MainFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onStart() {
        requireContext()
        viewModel.connect()
        super.onStart()
    }

    override fun onStop() {
        viewModel.disconnect()
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            val adapter = Adapter()
            rv.adapter = adapter
            lifecycleScope.launch {
                //repeatOnLifecycle
                viewModel.messages.collect { messages ->
                    adapter.setData(messages)
                    rv.scrollToPosition(messages.size - 1)
                }
            }
            button.setOnClickListener {
                val input = editText.text.toString()
                viewModel.sendMessage(input)
                editText.text.clear()
            }
        }
    }
}