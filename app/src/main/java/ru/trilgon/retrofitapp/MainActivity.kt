package ru.trilgon.retrofitapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import ru.trilgon.retrofitapp.databinding.ActivityMainBinding
import java.lang.Exception

const val BASE_URL = "https://cat-fact.herokuapp.com"

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"

    private lateinit var biding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)

        biding.btnNewFact.setOnClickListener {
            getCurrentData()
        }

    }

    private fun getCurrentData() {


        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getCatFact().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d(TAG, data.text)

                    withContext(Dispatchers.Main) {
                        biding.apply {
                            textFact.text = data.text
                            textFact.visibility = View.VISIBLE
                            textPreview.visibility = View.INVISIBLE
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.warn_smth_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}