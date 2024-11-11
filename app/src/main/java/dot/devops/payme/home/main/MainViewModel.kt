package dot.devops.payme.home.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dot.devops.payme.base.BaseViewModel
import dot.devops.payme.data.shareed.MyPreference
import dot.devops.payme.events.NavGraphEvent
import dot.devops.payme.other.share
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefsProvider: MyPreference
) : BaseViewModel() {

    private val navGraphEvent = Channel<NavGraphEvent>()

    fun setNavGraphEvent(event: NavGraphEvent) = viewModelScope.launch {
        navGraphEvent.send(event)
    }

    fun setNavGraphEvent() = viewModelScope.launch {
        if (prefsProvider.token.isNotEmpty()) {
            navGraphEvent.send(NavGraphEvent.PinCode)
        } else {
            navGraphEvent.send(NavGraphEvent.Home)
        }
    }

    fun observeNavGraphEvent(): Flow<NavGraphEvent> {
        return navGraphEvent.consumeAsFlow().share(viewModelScope)
    }
}