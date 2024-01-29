package com.example.foodapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import autodispose2.androidx.lifecycle.scope
import autodispose2.autoDispose
import com.example.foodapp.R
import com.example.foodapp.repository.firebase.entity.RecipeType
import com.example.foodapp.repository.model.Recipe
import com.example.foodapp.ui.adapters.RecipeAdapter
import com.example.foodapp.ui.adapters.RecipeAdapter.RecipeViewHolder
import com.example.foodapp.viewmodel.RecipeViewModel
import com.example.foodapp.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.stream.Collectors

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private val recipeViewModel: RecipeViewModel by viewModels({ requireActivity() })
    private val userViewModel: UserViewModel by viewModels({ requireActivity() })
    private lateinit var surfVp2: ViewPager2
    private var surfVp2lastPosition = -1
    private var adapter: RecipeAdapter
    private var recipeDetailTransitionListener: MotionLayout.TransitionListener? = null

    init {
        adapter = RecipeAdapter(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
                surfVp2.isUserInputEnabled = false
                if (recipeDetailTransitionListener != null) recipeDetailTransitionListener?.onTransitionStarted(
                    motionLayout, startId, endId
                )
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float
            ) {
                if (recipeDetailTransitionListener != null) recipeDetailTransitionListener?.onTransitionChange(
                    motionLayout, startId, endId, progress
                )
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                surfVp2.isUserInputEnabled = true
                if (recipeDetailTransitionListener != null) recipeDetailTransitionListener?.onTransitionCompleted(
                    motionLayout, currentId
                )
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float
            ) {
                if (recipeDetailTransitionListener != null) recipeDetailTransitionListener?.onTransitionTrigger(
                    motionLayout, triggerId, positive, progress
                )
            }
        })
    }

    fun setRecipeDetailTransitionListener(recipeDetailTransitionListener: MotionLayout.TransitionListener?) {
        this.recipeDetailTransitionListener = recipeDetailTransitionListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        surfVp2 = inflater.inflate(R.layout.fragment_recipe, container, false) as ViewPager2
        setupViewPager()
        return surfVp2
    }

    private fun setupViewPager() {
        userViewModel.likedRecipeIdListSubject.autoDispose(viewLifecycleOwner.scope())
            .subscribe({ likedRecipeIdList: List<Int> ->
                adapter.setLikedRecipeIdList(
                    likedRecipeIdList
                )
            }, {})
        userViewModel.savedRecipeListSubject.autoDispose(viewLifecycleOwner.scope())
            .subscribe({ list: List<Recipe> ->
                adapter.setSavedRecipeIdList(list.stream().map { obj: Recipe -> obj.id }
                    .collect(Collectors.toList()))
            }, {})
        adapter.setOnLikeButtonCheckedChange { recipe: Recipe, newCheckState: Boolean ->
            if (newCheckState) {
                userViewModel.addNewRecipe(RecipeType.LIKED, recipe)
            } else {
                userViewModel.removeRecipe(RecipeType.LIKED, recipe)
            }
        }
        adapter.setOnSaveButtonCheckedChange { recipe: Recipe, newCheckState: Boolean ->
            if (newCheckState) {
                userViewModel.addNewRecipe(RecipeType.SAVED, recipe)
            } else {
                userViewModel.removeRecipe(RecipeType.SAVED, recipe)
            }
        }
        surfVp2.adapter = adapter
        surfVp2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            private var lastViewHolder: RecipeViewHolder? = null
            private val rv = surfVp2.getChildAt(0) as RecyclerView
            override fun onPageScrolled(
                position: Int, positionOffset: Float, @Px positionOffsetPixels: Int
            ) {
                if (positionOffset != 0f) {
                    if (lastViewHolder == null) {
                        lastViewHolder =
                            rv.findViewHolderForAdapterPosition(surfVp2lastPosition) as RecipeViewHolder?
                    }
                } else {
                    lastViewHolder?.resetLayout()
                    lastViewHolder = null
                    surfVp2lastPosition = position
                }
            }
        })
        val itemPosition = arguments?.getInt("position", -1) ?: -1
        val isSavedRecipeList = itemPosition != -1
        if (!isSavedRecipeList) {
            recipeViewModel.recipePagingDataSubject
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(viewLifecycleOwner.scope())
                .subscribe { recipePagingData ->
                    adapter.submitData(viewLifecycleOwner.lifecycle, recipePagingData)
                }
        } else {
            adapter.submitData(
                viewLifecycleOwner.lifecycle,
                PagingData.from<Recipe>(userViewModel.savedRecipeListSubject.value ?: arrayListOf())
            )
            surfVp2.post { surfVp2.setCurrentItem(itemPosition, false) }
        }
    }
}