package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodapp.repository.firebase.UserRepository
import com.example.foodapp.repository.firebase.entity.RecipeType
import com.example.foodapp.repository.firebase.entity.UserPreference
import com.example.foodapp.repository.model.Recipe
import com.example.foodapp.repository.repositories.RecipeRepository
import com.example.foodapp.viewmodel.utils.plusAssign
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {
    val userPreferenceSubject = BehaviorSubject.create<UserPreference?>()
    val firebaseUserSubject = BehaviorSubject.create<FirebaseUser?>()
    val likedRecipeIdListSubject = BehaviorSubject.createDefault<List<Int>>(listOf())
    val savedRecipeListSubject = BehaviorSubject.createDefault<List<Recipe>>(listOf())
    private var userReference: DatabaseReference? = null
    private val compositeDisposable = CompositeDisposable()

    init {
        initData()
    }

    fun initData() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            firebaseUserSubject.onNext(firebaseUser)
        }
        if (firebaseUser != null && firebaseUser.email != null && firebaseUser.email?.trim { it <= ' ' } != "") {
            userReference = userRepository.getUser(firebaseUser.email)
            fetchRecipes(RecipeType.LIKED)
            fetchRecipes(RecipeType.SAVED)
            fetchUserPreferences()
        }
    }

    fun clearData() {
        userPreferenceSubject.onNext(null)
        likedRecipeIdListSubject.onNext(listOf())
        savedRecipeListSubject.onNext(listOf())
    }

    fun fetchUserPreferences() {
        userReference?.also { userReference ->
            userRepository.getUserPreference(userReference) { ur ->
                userPreferenceSubject.onNext(ur)
            }
        }
    }

    fun setUserPreferences(referenceData: UserPreference) {
        userReference?.also { userReference ->
            userRepository.setUserPreference(userReference, referenceData, null)
        }
    }

    fun fetchRecipes(recipeType: RecipeType) {
        userReference?.also { userReference ->
            userRepository.getRecipes(userReference,
                recipeType,
                if (recipeType == RecipeType.LIKED) { likedRecipeIds ->
                    likedRecipeIdListSubject.onNext(likedRecipeIds)
                } else { savedRecipeIds ->
                    compositeDisposable += Observable.fromIterable(savedRecipeIds)
                        .concatMapEagerDelayError({
                            recipeRepository.getRecipeById(it).subscribeOn(Schedulers.io())
                        }, true)
                        .scan(listOf<Recipe>()) { list, recipe -> list + recipe }
                        .subscribeOn(Schedulers.io())
                        .subscribe(savedRecipeListSubject::onNext)
                })
        }
    }

    fun setRecipes(recipeType: RecipeType, recipeList: MutableList<Recipe>) {
        userReference?.also { userReference ->
            userRepository.setRecipes(
                userReference,
                recipeType,
                recipeList.stream().map { obj: Recipe -> obj.id }.collect(Collectors.toList()),
                null
            )
            if (recipeType == RecipeType.SAVED) {
                savedRecipeListSubject.onNext(recipeList)
            } else {
                likedRecipeIdListSubject.onNext(recipeList.map { obj: Recipe -> obj.id })
            }
        }
    }

    fun addNewRecipe(recipeType: RecipeType, recipe: Recipe) {
        userReference?.also { userReference ->
            if (recipeType == RecipeType.SAVED) {
                if (savedRecipeListSubject.value?.contains(recipe) == true) return
            } else {
                val likedIds = likedRecipeIdListSubject.value
                if (likedIds != null && likedIds.contains(recipe.id)) return
            }
            try {
                userRepository.addRecipe(userReference, recipeType, recipe.id, null)
                if (recipeType == RecipeType.SAVED) {
                    savedRecipeListSubject.onNext(
                        savedRecipeListSubject.value?.plus(recipe) ?: listOf(recipe)
                    )
                } else {
                    likedRecipeIdListSubject.onNext(
                        likedRecipeIdListSubject.value?.plus(recipe.id) ?: listOf(recipe.id)
                    )
                }
            } catch (e: Exception) {
                Log.e("FoodApp", e.toString())
            }
        }
    }

    fun removeRecipe(recipeType: RecipeType, recipe: Recipe) {
        userReference?.also { userReference ->
            try {
                userRepository.deleteRecipeById(
                    userReference,
                    recipeType,
                    recipe.id
                ) { task ->
                    if (task.isSuccessful && recipeType == RecipeType.SAVED) {
                        savedRecipeListSubject.onNext(savedRecipeListSubject.value?.minus(recipe))
                    } else {
                        likedRecipeIdListSubject.onNext(
                            likedRecipeIdListSubject.value?.minus(
                                recipe.id
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("FoodApp", e.toString())
            }
        }
    }
}