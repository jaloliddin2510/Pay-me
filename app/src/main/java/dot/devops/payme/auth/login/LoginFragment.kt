package dot.devops.payme.auth.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dot.devops.payme.R
import dot.devops.payme.base.BaseFragment
import dot.devops.payme.data.shareed.MyPreference
import dot.devops.payme.databinding.FragmentLoginBinding
import dot.devops.payme.other.toast
import dot.devops.payme.pin_code.PinCodeActivity
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val vm: LoginViewModel by viewModels()
    @Inject
    lateinit var preference: MyPreference

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun setup() = with(binding) {
        btnContinue.setOnClickListener {
            vm.logIn(etEmail.text.toString(), etPassword.text.toString())
        }

        backSignUp.setOnClickListener {
            findNavController().navigateUp()
        }

        vm.logInEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is LoginUiEvent.Error -> {
                    requireContext().toast("Error")
                }
                LoginUiEvent.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is LoginUiEvent.Success -> {
                    event.token?.let { token ->
                        preference.token = token
                    }
                    progressBar.visibility = View.GONE
                    requireContext().toast("Success")
                    // Muvaffaqiyatli log in bo'lgandan keyin, kerak bo'lsa, boshqa fragmentga o'tish
                    val intent=Intent(requireContext(),PinCodeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
