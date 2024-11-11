package dot.devops.payme.home.choose_language

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dot.devops.payme.base.BaseViewModel
import dot.devops.payme.data.shareed.MyPreference
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val preference: MyPreference
) : BaseViewModel() {

    private var _language= Channel<Language>()
    val language=_language.receiveAsFlow()

    fun setLanguage(language: String)=viewModelScope.launch{
        if (language!=preference.language){
            preference.language=language
            _language.send(preference.language.getLanguageByCode())
        }

    }
}
fun String.getLanguageByCode(): Language = when (this) {
    Language.UZBEK.code -> Language.UZBEK
    Language.RUSSIAN.code -> Language.RUSSIAN
    Language.ENGLISH.code -> Language.ENGLISH
    else -> Language.UZBEK
}