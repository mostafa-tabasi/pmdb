package com.pmdb.android.ui.main.settings

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.pmdb.android.BR
import com.pmdb.android.R
import com.pmdb.android.databinding.FragmentSettingsBinding
import com.pmdb.android.ui.base.BaseFragment
import com.pmdb.android.ui.main.MainActivity
import com.pmdb.android.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(),
  SettingsNavigator {

  override val viewModel: SettingsViewModel by viewModels()
  override val bindingVariable: Int get() = BR.viewModel
  override val layoutId: Int get() = R.layout.fragment_settings

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

      it.includeGoogleSync.signIn.setOnClickListener { signInWithGoogle() }
      it.includeGoogleSync.signOut.setOnClickListener { signOutFromGoogle() }
    }
  }

  private fun signInWithGoogle() {
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

  private fun signOutFromGoogle() {
    // اگر مشکلی در اتصال اینترنت وجود داشت، خطای آن چشمک بزند و فرایند ادامه پیدا نکند
    with((requireActivity() as MainActivity)) {
      if (isConnectionErrorShowing()) {
        blinkConnectionError()
        return
      }
    }

    AuthUI.getInstance()
      .signOut(requireContext())
      .addOnCompleteListener { task ->
        if (task.isSuccessful) viewModel.onGoogleSignOut()
      }
  }

  private val signInLauncher = registerForActivityResult(
    FirebaseAuthUIActivityResultContract()
  ) { viewModel.onGoogleSignInResult(it) }

}