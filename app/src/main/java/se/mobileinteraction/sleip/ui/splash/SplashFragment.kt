package se.mobileinteraction.sleip.ui.splash

import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.util.ScreenUtils


class SplashFragment : Fragment(R.layout.fragment_splash) {

    val viewModel by viewModel<SplashViewModel>()

    override fun onResume() {
        super.onResume()
        img_logo.apply {
            translationY = ScreenUtils.dipsToPixel(120f)
            animate().alpha(1f).setStartDelay(500).setDuration(300).start()
            animate()
                .setInterpolator(DecelerateInterpolator())
                .translationY(0f)
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setStartDelay(800)
                .setDuration(800)

                .withEndAction {
                    navigateToDetails()
                }.start()
        }
    }

    private fun navigateToDetails() {
        val navTo = if (viewModel.repo.readTokenCache()?.token != null)
            SplashFragmentDirections.actionSplashFragmentToNavHostFragment().actionId
        else
            SplashFragmentDirections.actionSplashFragmentToLoginFragment().actionId
        // to not backPressed to startup fragment (Splash)
        findNavController().navigate(
            navTo,
            null,
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
        )
    }
}
