package com.peshale.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.peshale.nmedia.R
import com.peshale.nmedia.databinding.ActivityNewPostBinding
import com.peshale.nmedia.utils.Utils

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etInputArea.requestFocus()
        binding.fabConfirmation.setOnClickListener {
            val intent = Intent()
            if (binding.etInputArea.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.etInputArea.text.toString()
                val videoLink = binding.etPostVideoLink.text.toString()
                if (videoLink != "" && !Utils.urlValidChecker(videoLink)) {
                    Toast.makeText(
                        this@NewPostActivity,
                        getString(R.string.error_url_validation),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    intent.putExtra("content", content)
                    intent.putExtra("videoLink", videoLink)
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
            }
        }
    }
}