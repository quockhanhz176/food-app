package com.example.foodapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import autodispose2.androidx.lifecycle.scope
import autodispose2.autoDispose
import com.example.foodapp.R
import com.example.foodapp.repository.model.Recipe
import com.example.foodapp.ui.adapters.SavedRecipeAdapter
import com.example.foodapp.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy

@AndroidEntryPoint
class SavedRecipesFragment : Fragment() {
    private val adapter = SavedRecipeAdapter()
    private var savedRecipesRv: RecyclerView? = null
    private var layout: ConstraintLayout? = null
    private val userViewModel: UserViewModel by viewModels({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout =
            inflater.inflate(R.layout.fragment_saved_recipes, container, false) as ConstraintLayout
        bindView()
        setItemClickListener()
        return layout
    }

    private fun bindView() {
        savedRecipesRv = layout?.findViewById(R.id.savedRecipesRv)
        userViewModel.savedRecipeListSubject
            .toFlowable(BackpressureStrategy.LATEST)
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(viewLifecycleOwner.scope())
            .subscribe { list ->
                adapter.submitList(list)
            }
        savedRecipesRv?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        savedRecipesRv?.adapter = adapter
    }

    private fun setItemClickListener() {
        adapter.setItemOnClickListener { recipe: Recipe ->
            val recipeList: List<Recipe> = adapter.currentList
            val position = recipeList.indexOf(recipe)
            val recipeFragment = RecipeFragment()
            recipeFragment.setRecipeList(
                ArrayList(recipeList),
                if (position == -1) null else position
            )
            parentFragmentManager.beginTransaction().add(
                R.id.fragment_container,
                recipeFragment
            ).addToBackStack(null).commit()
        }
    }
}