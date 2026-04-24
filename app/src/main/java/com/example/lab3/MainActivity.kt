

package com.example.mad302lab03

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Course: MAD302 - Android Development
 * Lab: Lab 3
 * Name: Ishmeet Singh
 * Student ID: A00202436
 *
 * Description:
 * This app demonstrates asynchronous data fetching using coroutines,
 * runtime permission handling (Location), and error handling.
 * It simulates an API call and displays results in the UI.
 */
/**
 * MainActivity is the main and only screen of this lab app.
 * It handles button clicks, permission requests, async operations,
 * and displaying results to the user.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var btnFetchData: Button
    private lateinit var tvResult: TextView

    /**
     * Requests location permission at runtime.
     * If granted, the app starts fetching data.
     * If denied, a message is shown in the TextView and Toast.
     */
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchData()
            } else {
                tvResult.text = "Permission denied. Please allow location permission to fetch data."
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Called when the activity is first created.
     * Initializes views and sets the button click listener.
     *
     * @param savedInstanceState Previously saved state of the activity, if available.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFetchData = findViewById(R.id.btnFetchData)
        tvResult = findViewById(R.id.tvResult)

        btnFetchData.setOnClickListener {
            checkPermissionAndProceed()
        }
    }

    /**
     * Checks whether location permission is already granted.
     * If yes, fetches data directly.
     * Otherwise, requests permission from the user.
     */
    private fun checkPermissionAndProceed() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchData()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Simulates fetching data from an API using a coroutine.
     * Displays loading text, waits for 2 seconds, and then either
     * shows successful data or a simulated network error.
     */
    private fun fetchData() {
        lifecycleScope.launch {
            try {
                tvResult.text = "Loading data..."

                // Simulate network/API delay
                delay(2000)

                val result = getMockData()
                tvResult.text = result

            } catch (e: Exception) {
                tvResult.text = "Error: ${e.message}"
                Toast.makeText(
                    this@MainActivity,
                    "Simulated network failure",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Simulates receiving data from an API.
     * Randomly throws an exception to demonstrate error handling.
     *
     * @return A success message string.
     * @throws Exception if a simulated network failure occurs.
     */
    private fun getMockData(): String {
        val shouldFail = Random.nextBoolean()

        if (shouldFail) {
            throw Exception("Network failure simulated. Please try again.")
        }

        return "Data fetched successfully. Mock API response received."
    }
}