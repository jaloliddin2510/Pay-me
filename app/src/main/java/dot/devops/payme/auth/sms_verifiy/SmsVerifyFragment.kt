package dot.devops.payme.auth.sms_verify

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dot.devops.payme.R
import dot.devops.payme.base.BaseFragment
import dot.devops.payme.data.shareed.MyPreference
import dot.devops.payme.databinding.FragmentSmsVerifyBinding
import dot.devops.payme.other.toast
import dot.devops.payme.pin_code.PinCodeActivity
import javax.inject.Inject

@AndroidEntryPoint
class SmsVerifyFragment : BaseFragment<FragmentSmsVerifyBinding>(R.layout.fragment_sms_verify) {
    @Inject
    lateinit var preference: MyPreference

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSmsVerifyBinding {
        return FragmentSmsVerifyBinding.inflate(inflater, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun setup() {
        // Telefon raqamini ko'rsatish
        binding.tvSmsNumber.text = " ${preference.phoneNumber}"

        // Orqaga qaytish tugmasini sozlash
        binding.backSignUp.setOnClickListener {
            // Bu yerga orqaga qaytish funksiyasini qo'shishingiz mumkin
        }

        // Foydalanuvchi emailini ko'rsatish
        preference.email = " ${FirebaseAuth.getInstance().currentUser?.email}"

        // Email tasdiqlashni tekshirish
        binding.btnContinue.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.reload()
                ?.addOnCompleteListener { task ->
                    if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                        // Email tasdiqlandi, PinCodeActivity-ga o'tish
                        val intent = Intent(requireContext(), PinCodeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        requireContext().toast("Email tasdiqlanmagan. Iltimos, tasdiqlang!")
                    }
                }
        }

        // SMS kodini kiritish inputlarini sozlash
        setupInput()
    }

    private fun setupInput() = with(binding) {
        // EditText-lar orasida TextWatcher va KeyListener larni qo'shish
        fun setTextWatcher(current: EditText, next: EditText?) {
            current.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        next?.requestFocus() // Keyingi inputga fokus
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        fun setKeyListener(current: EditText, previous: EditText?) {
            current.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && current.text.isEmpty()) {
                    previous?.requestFocus() // Oldingi inputga fokus
                }
                false
            }
        }

        // Fokusga qarab SMS qutilarining rangini o'zgartirish
        fun setFocusListener(editText: EditText, smsView: MaterialCardView) {
            editText.setOnFocusChangeListener { _, hasFocus ->
                smsView.strokeColor = ContextCompat.getColor(
                    requireContext(),
                    if (hasFocus) R.color.payMeColor else R.color.gray_white
                )
            }
        }

        // EditText va CardView larni list qilish
        val editTexts = listOf(et1, et2, et3, et4, et5, et6)
        val smsViews = listOf(sms1, sms2, sms3, sms4, sms5, sms6)

        // Har bir inputni boshqarish
        for (i in editTexts.indices) {
            val next = if (i < editTexts.size - 1) editTexts[i + 1] else null
            val previous = if (i > 0) editTexts[i - 1] else null
            setTextWatcher(editTexts[i], next)
            setKeyListener(editTexts[i], previous)
            setFocusListener(editTexts[i], smsViews[i])
        }
    }
}
