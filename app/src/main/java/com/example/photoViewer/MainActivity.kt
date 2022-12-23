package com.example.photoViewer


import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.photoViewer.databinding.ActivityMainBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import okhttp3.*
import androidx.annotation.RequiresApi as RequiresApi1


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    var bundle = Bundle()
    var TOKEN = "vk1.a.GkCTaK8JuNUKlQIIJUXRzvbDr3CZeAkRgbVQIjbNI3p5Iut_0mjRcI9yvrcDM6rZr3upANtimStyGa0znU_gDQ85Fl-LEH27COGAwkltvYtDfS2_H5amfxf4PPeNxawEYIJX73pjRHBk31tdmhXpvQNjbklBDryhmJuB0NiWjGrbXAZyidXqxyQAYBERjiQnWOMeJAtT9CQnHmY0R5kjkQ"

    @RequiresApi1(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        val authLauncher = VK.login(this) { result : VKAuthenticationResult ->
//            when (result) {
//                is VKAuthenticationResult.Success -> {
//                    // User passed authorization
//                    TOKEN = result.token.accessToken
//                }
//                is VKAuthenticationResult.Failed -> {
//                    // User didn't pass authorization
//                }
//            }
//        }
//        authLauncher.launch(arrayListOf(VKScope.WALL, VKScope.PHOTOS))
    }



    @RequiresApi1(Build.VERSION_CODES.N)
    fun asyncRun(url: String) : Future<String> {
        val request = Request.Builder()
            .url(url)
            .build()

        val future = CompletableFuture<String>()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                future.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    future.complete(response.body!!.string())
                }
            }
        })
        return future
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}