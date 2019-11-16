package com.home.gifttest.ui.dashboard.games

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.home.gifttest.R

class ChooseGameFragment : Fragment() {

    companion object {
        fun newInstance() = ChooseGameFragment()
    }

    private lateinit var viewModel: ChooseGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_game_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChooseGameViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
