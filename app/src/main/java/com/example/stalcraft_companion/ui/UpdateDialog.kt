package com.example.stalcraft_companion.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.stalcraft_companion.data.ApiClient
import com.example.stalcraft_companion.databinding.DialogProgressBinding
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateDialog : DialogFragment() {
    interface UpdateListener {
        fun onUpdateConfirmed()
        fun onUpdateCancelled()
    }

    private var listener: UpdateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? UpdateListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Доступно обновление")
            .setMessage("Обнаружена новая версия базы данных (${getLastUpdateInfo()}). Хотите обновить?")
            .setPositiveButton("Обновить") { _, _ ->
                listener?.onUpdateConfirmed()
            }
            .setNegativeButton("Позже") { _, _ ->
                listener?.onUpdateCancelled()
            }
            .create()
    }

    private fun getLastUpdateInfo(): String {
        val repoInfo = try {
            runBlocking { ApiClient.githubApi.getRepoInfo() }
        } catch (e: Exception) {
            return ""
        }
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .parse(repoInfo.updatedAt.toString()) ?: Date()
            )
    }
}

class ProgressDialog : DialogFragment() {
    private lateinit var binding: DialogProgressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogProgressBinding.inflate(inflater, container, false)
        isCancelable = false
        return binding.root
    }

    fun updateProgress(current: Int, total: Int) {
        binding.apply {
            progressBar.max = total
            progressBar.progress = current
            progressText.text = "Загружено $current из $total"
        }
    }

    companion object {
        fun newInstance(): ProgressDialog {
            return ProgressDialog()
        }
    }
}