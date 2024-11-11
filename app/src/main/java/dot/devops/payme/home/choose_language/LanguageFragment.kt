package dot.devops.payme.home.choose_language

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dot.devops.payme.R
import dot.devops.payme.base.BaseFragment
import dot.devops.payme.databinding.FragmentLanguageBinding
import dot.devops.payme.home.main.MainActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LanguageFragment:BaseFragment<FragmentLanguageBinding>(R.layout.fragment_language) {

    private val vm: LanguageViewModel by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLanguageBinding {
        return FragmentLanguageBinding.inflate(inflater,container,false)
    }

    override fun setBackgroundColor(view: View, color: Int) {
        val colorBackground=R.color.payColor_payBack
        super.setBackgroundColor(view, colorBackground)
    }

    override fun setup() {
        activity?.window?.statusBarColor=ContextCompat.getColor(requireContext(),R.color.payColor_payBack)
    }

    override fun observe() {
        vm.language.onEach {
            (requireActivity() as? MainActivity)?.changeLanguage()
        }.launchIn(
            viewLifecycleOwner.lifecycleScope
        )
    }
    override fun clicks() = with(binding) {
        tilEn.setOnClickListener {
            vm.setLanguage(Language.ENGLISH.code)
            findNavController().navigate(R.id.action_languageFragment_to_loginFragment)
        }
        tilRu.setOnClickListener {
            vm.setLanguage(Language.RUSSIAN.code)
            findNavController().navigate(R.id.action_languageFragment_to_loginFragment)
        }
        tilUz.setOnClickListener {
            vm.setLanguage(Language.UZBEK.code)
            findNavController().navigate(R.id.action_languageFragment_to_loginFragment)
        }
    }
}