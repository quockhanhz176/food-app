package com.example.foodapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.function.Consumer

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val recipeViewModel: RecipeViewModel by viewModels({ requireActivity() })
    var onUserMenuItemCLickListener: OnUserMenuItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        bindAction()
        return binding.root
    }

    private var onSearchListener: Consumer<String>? = null
    fun setOnSearchListener(onSearchListener: Consumer<String>?) {
        this.onSearchListener = onSearchListener
    }

    private fun bindAction() {
        binding.searchSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                recipeViewModel.setSearchParams(query, null, null, null, null)
                if (onSearchListener != null) onSearchListener!!.accept(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        binding.userIconIv.setOnClickListener { view: View? ->
            val popup = PopupMenu(context, view)
            popup.menuInflater.inflate(R.menu.user_menu, popup.menu)
            MenuCompat.setGroupDividerEnabled(popup.menu, true)
            popup.show()
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.homeI -> {
                        onUserMenuItemCLickListener?.onHomeClick()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.savedRecipesI -> {
                        onUserMenuItemCLickListener?.onSavedRecipesClick()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.userSettingsI -> {
                        onUserMenuItemCLickListener?.onUserSettingsClick()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.logOutI -> {
                        onUserMenuItemCLickListener?.onLogOutClick()
                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
        }
    }

    open class OnUserMenuItemClickListener {
        open fun onHomeClick() {}
        open fun onSavedRecipesClick() {}
        open fun onUserSettingsClick() {}
        open fun onLogOutClick() {}
    }
}