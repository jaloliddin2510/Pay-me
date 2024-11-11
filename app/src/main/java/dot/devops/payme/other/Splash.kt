package dot.devops.payme.other

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dot.devops.payme.R
import dot.devops.payme.home.main.MainActivity

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.statusBarColor=ContextCompat.getColor(this,R.color.payMeBackground)
         Handler(Looper.getMainLooper()).postDelayed({
             startActivity(Intent(this, MainActivity::class.java))
         },1000)
    }
}