package dot.devops.payme.home.main

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication:Application() {
    companion object{
        lateinit var fbAuth: FirebaseAuth
        lateinit var dbReference: DatabaseReference
        const val TOKEN="token"
    }
    override fun onCreate() {
        super.onCreate()
        fbAuth= Firebase.auth
        dbReference= FirebaseDatabase.getInstance("https://payme-b0070-default-rtdb.firebaseio.com/").reference
    }
}