package dot.devops.payme.pin_code

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dot.devops.payme.R
import dot.devops.payme.data.shareed.MyPreference
import dot.devops.payme.databinding.ActivityPinCodeBinding
import dot.devops.payme.events.NavGraphEvent
import dot.devops.payme.home.main.MainViewModel
import dot.devops.payme.other.gone
import dot.devops.payme.other.vibrate
import dot.devops.payme.other.visible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PinCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinCodeBinding
    private val pinCodeViewModel: PinCodeViewModel by viewModels()
    private val sharedVM: MainViewModel by viewModels()

    @Inject
    lateinit var preference: MyPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        pinCodeViewModel.userLog()
    }

    private fun setupClickListeners()= with(binding){
        val buttonIds = listOf(
            btn1 to "1", btn2 to "2", btn3 to "3",
            btn4 to "4", btn5 to "5", btn6 to "6",
            btn7 to "7", btn8 to "8", btn9 to "9",
            btn0 to "0"
        )

        buttonIds.forEach { (button, digit) ->
            button.setOnClickListener {
                vibrate()
                pinCodeViewModel.addDigit(digit)
            }
        }

        btnClear.setOnClickListener {
            vibrate()
            pinCodeViewModel.removeLastDigit()
        }

        listOf(vwConfirm).forEach { button ->
            button.setOnClickListener {
                vibrate()
                pinCodeViewModel.handlePinCodeCompletion()
            }
        }

        logout.setOnClickListener {
            vibrate()
            pinCodeViewModel.handlePinCodeExit()
            navigateWithDelay(NavGraphEvent.Auth, 500)
        }
    }

    private fun setupObservers() {
        pinCodeViewModel.apply {
            pinCodeList.observe(this@PinCodeActivity) {
                updatePinCode(it)
            }

            fromRegister.observe(this@PinCodeActivity) {
                with(binding) {
                    if (it == false) {
                        logout.gone()
                        vwConfirm.gone()
                        vwTouch.visible()
                    } else {
                        logout.visible()
                        vwConfirm.gone()
                        vwTouch.visible()
                    }
                }
            }

            pinCodeEvent.observe(this@PinCodeActivity) {
                handlePinCodeEvent(it)
            }

            errorAttempts.observe(this@PinCodeActivity) {
                binding.errorAttempt.text =
                    if (it >= 1) getString(R.string.pin_mismatch, 5 - it) else ""
            }

            timerRunning.observe(this@PinCodeActivity) {
                binding.errorAttempt.text =
                    if (it) getString(R.string.pin_xato_1_daqiqa) else ""
                setButtonsEnabled(!it)
            }
        }
    }

    private fun handlePinCodeEvent(event: PinCodeEvent) {
        when (event) {
            PinCodeEvent.PinRegistered -> navigateWithDelay(NavGraphEvent.Home, 1000)
            PinCodeEvent.PinValidated -> lifecycleScope.launch {
                delay(200)
                pinCodeViewModel.clearPinCode()
                delay(200)
                checkPinAnim()
                navigateWithDelay(NavGraphEvent.Home, 1200)
            }

            PinCodeEvent.PinValidationFailed -> lifecycleScope.launch {
                delay(200)
                pinCodeViewModel.clearPinCode()
                delay(200)
                checkPinAnim()
                delay(600)
                errorPinAnim()
                delay(1000)
                pinCodeViewModel.clearPinCode()
            }
        }
    }

    private fun updatePinCode(pinCode: MutableList<String>) {
        with(binding) {
            val pinViews = listOf(view01, view02, view03, view04)
            pinViews.forEachIndexed { index, view ->
                view.setBackgroundResource(if (index < pinCode.size) R.drawable.bg_pin_active else R.drawable.bg_pin_inactive)
            }

            if (pinCode.size == 4) {
                if (pinCodeViewModel.fromRegister.value == true) {
                    pinCodeViewModel.handlePinCodeCompletion()
                } else {
                    vwConfirm.visible()
                    vwTouch.gone()
                }
            } else {
                vwConfirm.gone()
                vwTouch.visible()
            }
        }
    }

    private fun navigateWithDelay(event: NavGraphEvent, delay: Long) {
        lifecycleScope.launch {
            delay(delay)
            sharedVM.setNavGraphEvent(event)
        }
    }

    private fun checkPinAnim()= with(binding){
        val pinViews = listOf(view01, view02, view03, view04)
        pinViews.forEachIndexed { index, view ->
            Handler(Looper.getMainLooper()).postDelayed({
                view.setBackgroundResource(R.drawable.bg_pin_active)
            }, index * 100L)
        }
    }

    private fun errorPinAnim(
        view: View = binding.root,
        offset: Float = 100f,
        repeatCount: Int = 4,
        dampingRatio: Float? = null,
        duration: Long = 1000L,
        interpolator: Interpolator = AccelerateDecelerateInterpolator()
    ) {
        val defaultDampingRatio = dampingRatio ?: (1f / (repeatCount + 1))
        val animValues = List((repeatCount * 4) + 1) { i ->
            when (i % 4) {
                0 -> 0f
                1 -> -offset * (1 - defaultDampingRatio * (i / 4))
                2 -> 0f
                3 -> offset * (1 - defaultDampingRatio * (i / 4))
                else -> 0f
            }
        }.toFloatArray()

        ValueAnimator.ofFloat(*animValues).apply {
            addUpdateListener { view.translationX = it.animatedValue as Float }
            this.interpolator = interpolator
            this.duration = duration
            start()
        }
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        with(binding) {
            listOf(
                btn1, btn2, btn3,
                btn4, btn5, btn6,
                btn7, btn8, btn9,
                btn0, btnClear
            ).forEach {
                it.isEnabled = enabled
            }
        }
    }
}