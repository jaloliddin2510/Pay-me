package dot.devops.payme.events

sealed interface NavGraphEvent {
    data object Home:NavGraphEvent
    data object Auth:NavGraphEvent
    data object PinCode:NavGraphEvent
}