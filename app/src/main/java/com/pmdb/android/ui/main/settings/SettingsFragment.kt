package com.pmdb.android.ui.main.settings

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pmdb.android.BR
import com.pmdb.android.R
import com.pmdb.android.data.model.firestore.User
import com.pmdb.android.databinding.FragmentSettingsBinding
import com.pmdb.android.ui.base.BaseFragment
import com.pmdb.android.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(),
  SettingsNavigator {

  override val viewModel: SettingsViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_settings

  @Inject
  lateinit var firebaseAuth: FirebaseAuth

  @Inject
  lateinit var firebaseFirestore: FirebaseFirestore

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.setNavigator(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    showSettingsPreferences()
    setUp()
  }

  private fun showSettingsPreferences() {
    val settingsPreferencesFragment = SettingsPreferencesFragment()
    settingsPreferencesFragment.setSettingsScrollListener { isFirstItemVisible ->
      viewDataBinding?.includeGoogleSync?.layoutSync?.elevation =
        if (isFirstItemVisible) 0f else 24f
    }
    childFragmentManager.beginTransaction()
      .replace(R.id.container_settings_preferences, settingsPreferencesFragment)
      .commit()
  }

  private fun setUp() {
    setUpGoogleSignIn()
  }

  /**
   * نمایش دیالوگ اطلاعات اپلیکیشن
   */
  fun openAboutDialog() {
    val action = SettingsFragmentDirections.actionSettingsFragmentToAboutDialog()
    findNavController().navigate(action)
  }

  private fun setUpGoogleSignIn() {
    viewDataBinding?.let {

      it.includeGoogleSync.includePreferenceCategory.apply {
        findViewById<TextView>(android.R.id.title).text = getString(R.string.google_sync)
        findViewById<ImageView>(android.R.id.icon).gone()
        findViewById<TextView>(android.R.id.summary).gone()
      }
      viewModel.setFirebaseUser(firebaseAuth.currentUser)
      it.includeGoogleSync.signIn.setOnClickListener {

        // لیست روش هایی که کاربر میتواند با آنها لاگین کند
        val providers = arrayListOf(
          AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        val signInIntent = AuthUI.getInstance()
          .createSignInIntentBuilder()
          .setAvailableProviders(providers)
          .setLogo(R.mipmap.ic_launcher)
          .build()
        signInLauncher.launch(signInIntent)
      }

      it.includeGoogleSync.signOut.setOnClickListener {
        AuthUI.getInstance()
          .signOut(requireContext())
          .addOnCompleteListener { task ->
            if (task.isSuccessful) viewModel.setFirebaseUser(null)
          }
      }
    }
  }

  private val signInLauncher = registerForActivityResult(
    FirebaseAuthUIActivityResultContract()
  ) { onSignInResult(it) }

  private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
    val response = result.idpResponse
    val user = FirebaseAuth.getInstance().currentUser
    if (result.resultCode == Activity.RESULT_OK && response != null && user != null) {
      if (response.isNewUser) {
        // ساخت کاربر جدید در دیتابیس براساس ID دریافت شده از لاگین
        val newUser = User(user.uid, user.displayName, user.email, user.photoUrl.toString())
        firebaseFirestore.collection("users").document(user.uid).set(newUser)
      }
      viewModel.setFirebaseUser(user)
    }
  }

}