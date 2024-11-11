package dot.devops.payme.home.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import dot.devops.payme.R
import dot.devops.payme.data.shareed.MyPreference
import dot.devops.payme.home.choose_language.getLanguageByCode
import java.util.Locale
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesProvider: MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor=ContextCompat.getColor(this,R.color.payMeBackground)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
    }
    fun changeLanguage() {
        val language = preferencesProvider.language.getLanguageByCode()
        val config = resources.configuration
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}