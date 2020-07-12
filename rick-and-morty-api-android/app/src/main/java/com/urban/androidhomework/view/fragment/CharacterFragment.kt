package com.urban.androidhomework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.urban.androidhomework.view_model.CharacterViewModel
import com.urban.androidhomework.view.adapter.CharactersAdapter
import com.urban.androidhomework.R


class CharacterFragment : Fragment() {
    private var adapter: CharactersAdapter? = null

    private var errorLayout: LinearLayout? = null
    private var loadingLayout: LinearLayout? = null
    private var errorTextView: TextView? = null
    private var retryButton: Button? = null
    private var recyclerView: RecyclerView? = null

    private var navController: NavController? = null

    private val characterItemClickListener = CharactersAdapter.OnItemClickListener { position, _ ->
        val character = adapter!!.currentList!![position]
        val bundle = Bundle()
        bundle.putInt(getString(R.string.characterIDKey), character.id)

        navController!!.navigate(
                R.id.action_charactersListFragment_to_characterDetailsFragment,
                bundle
        )
    }

    private val viewModel: CharacterViewModel by lazy {
        ViewModelProviders.of(this).get(CharacterViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        errorLayout = view.findViewById(R.id.errorLayout)
        loadingLayout = view.findViewById(R.id.loadingLayout)
        errorTextView = view.findViewById(R.id.errorTextView)
        retryButton = view.findViewById(R.id.retryButton)
        recyclerView = view.findViewById(R.id.charactersRecyclerView)

        adapter = CharactersAdapter(context)
        adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        adapter!!.setOnItemClickListener(characterItemClickListener)
        recyclerView!!.adapter = adapter

        retryButton!!.setOnClickListener { _ -> reLoad()}
        val columns = resources.getInteger(R.integer.gallery_columns)
        recyclerView!!.layoutManager = StaggeredGridLayoutManager(columns, RecyclerView.VERTICAL)
        recyclerView!!.setHasFixedSize(true)

        viewModel.liveData.observe(viewLifecycleOwner, Observer { adapter!!.addList(it) })
        viewModel.throwableLiveData.observe(viewLifecycleOwner, Observer { throwable ->
            if(throwable == null) {
                onLoadSuccess()
            } else {
                onLoadFailure(throwable.message!!)
            }
        })
    }

    private fun onLoadSuccess() {
        recyclerView!!.visibility = View.VISIBLE
        errorLayout!!.visibility = View.GONE
        loadingLayout!!.visibility = View.GONE
    }

    private fun onLoadFailure(message: String) {
        recyclerView!!.visibility = View.INVISIBLE
        errorLayout!!.visibility = View.VISIBLE
        loadingLayout!!.visibility = View.GONE

        errorTextView!!.text = message
    }

    private fun reLoad(){
        recyclerView!!.visibility = View.INVISIBLE
        errorLayout!!.visibility = View.GONE
        loadingLayout!!.visibility = View.VISIBLE

        viewModel.loadCharacter()
    }
}