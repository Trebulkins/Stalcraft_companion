package com.example.stalcraft_companion.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.databinding.ActivityMainBinding
import com.example.stalcraft_companion.ui.NetworkUtils.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), UpdateDialog.UpdateListener {
    private var progressDialog: ProgressDialog? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ItemViewModel
    private var isLandscape = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]

        checkForUpdates()
    }

    private fun checkForUpdates() {
        if (!isNetworkAvailable(this)) {
            showError("No internet connection")
            loadData()
            return
        }

        lifecycleScope.launch {
            val needsUpdate = viewModel.checkForUpdates()
            if (needsUpdate) {
                showUpdateDialog()
            } else {
                loadData()
            }
        }
    }

    private fun loadData() {
        viewModel.items.observe(this) { items ->
            if (items.isNotEmpty()) {
                showMainFragment()
            }
        }
    }

    override fun onUpdateConfirmed() {
        showProgressDialog()
        viewModel.progress.observe(this) { (current, total) ->
            progressDialog?.updateProgress(current, total)
        }

        viewModel.performUpdate()
        viewModel.items.observe(this) { items ->
            if (items.isNotEmpty()) {
                progressDialog?.dismiss()
                showMainFragment()
            }
        }
    }

    override fun onUpdateCancelled() {
        loadData()
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showUpdateDialog() {
        UpdateDialog().show(supportFragmentManager, "UpdateDialog")
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog.newInstance()
        progressDialog?.show(supportFragmentManager, "ProgressDialog")
    }

    private fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MainFragment.newInstance())
            .commit()
    }
}