package com.example.filmssurf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.filmssurf.R
import com.example.filmssurf.databinding.ActivityMainBinding
import com.example.filmssurf.viewmodel.TablesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel by viewModels<TablesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, viewModel.toString(), Toast.LENGTH_SHORT).show()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setupWithNavController(
            findNavController(R.id.navHostFragment)
        )

        binding.bottomNavigationView.setOnItemReselectedListener {
            /* NO OPERATIONS */
        }
    }
}