package dot.devops.payme.data.shareed

import android.content.SharedPreferences
import javax.inject.Inject

class MyPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {
    var pinCode: String by sharedPreferences.string()
    var token: String by sharedPreferences.string()
    var phoneNumber: String by sharedPreferences.string()
    var email: String by sharedPreferences.string()
    var language: String by sharedPreferences.string("uz")
    var lastErrorTimestamp: Long by sharedPreferences.long()
    var errorAttempts: Int by sharedPreferences.int()
}
