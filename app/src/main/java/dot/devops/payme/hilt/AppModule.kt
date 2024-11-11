package dot.devops.payme.hilt
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dot.devops.payme.data.shareed.MyPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideMyPreference(sharedPreferences: SharedPreferences): MyPreference {
        return MyPreference(sharedPreferences)
    }
}
