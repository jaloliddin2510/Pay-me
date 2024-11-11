package dot.devops.payme.pin_code

import dot.devops.payme.data.shareed.MyPreference
import javax.inject.Inject

class IsRegister@Inject constructor(
    private var preference: MyPreference
) {
    fun registerPin(pin: String) {
        preference.pinCode = pin
    }

    fun isValidPin(pin: String): Boolean {
        return preference.pinCode == pin
    }
}