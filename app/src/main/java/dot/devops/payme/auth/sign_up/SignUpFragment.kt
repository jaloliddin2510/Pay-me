package dot.devops.payme.auth.sign_up

import android.content.ContentValues.TAG
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dot.devops.payme.R
import dot.devops.payme.base.BaseFragment
import dot.devops.payme.data.shareed.MyPreference
import dot.devops.payme.databinding.FragmentSignUpBinding
import dot.devops.payme.other.toast
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    @Inject
    lateinit var preference: MyPreference
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    override fun setup() = with(binding) {
        backSignUp.setOnClickListener {
            findNavController().navigateUp()
        }
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.whitePayColor_darkPayColor
                        )
                    )
                    btnContinue.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_whitePayColor_darkPayColor
                        )
                    )
                    clearPhoneNumber.visibility = View.GONE
                } else {
                    btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.payMeColor
                        )
                    )
                    btnContinue.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    clearPhoneNumber.visibility = View.VISIBLE
                    clearPhoneNumber.setOnClickListener {
                        etEmail.text.clear()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.whitePayColor_darkPayColor
                        )
                    )
                    btnContinue.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_whitePayColor_darkPayColor
                        )
                    )
                    clearPassword.visibility = View.GONE
                } else {
                    btnContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.payMeColor
                        )
                    )
                    btnContinue.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    clearPassword.visibility = View.VISIBLE
                    clearPassword.setOnClickListener {
                        etPassword.text.clear()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        btnContinue.setOnClickListener {
            val email = etEmail.text.toString() // Telefon raqami o'rniga email
            val password = etPassword.text.toString()
            preference.email=email
            if (email.isEmpty() || password.length < 6) {
                requireContext().toast("Email yoki parol xato")
                return@setOnClickListener
            }

            btnContinue.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Tasdiqlash emailini jo'natish
                        sendEmailVerification()
                    } else {
                        progressBar.visibility = View.GONE
                        btnContinue.visibility = View.VISIBLE
                        requireContext().toast("Ro'yxatdan o'tish xato: ${task.exception?.message}")
                    }
                }
        }

    }

    private fun sendEmailVerification() = with(binding){
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    requireContext().toast("Tasdiqlash emaili yuborildi!")
                    // SMS Verify fragment o'rniga Email Verify uchun yangi ekranga o'ting
                   findNavController().navigate(R.id.action_signUpFragment_to_smsVerifyFragment)
                } else {
                    requireContext().toast("Email jo'natishda xato")
                    Log.d(TAG, ":::::::::::::::::::::::::::::::::;${task.exception?.message}")
                }
                progressBar.visibility = View.GONE
                btnContinue.visibility = View.VISIBLE
            }
    }

}