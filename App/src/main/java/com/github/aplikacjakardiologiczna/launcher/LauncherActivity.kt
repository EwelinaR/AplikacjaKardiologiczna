package com.github.aplikacjakardiologiczna.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.main.MainActivity

class LauncherActivity : AppCompatActivity(), LauncherContract.View {
    private lateinit var presenter: LauncherContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPresenter(LauncherPresenter(this, AppSettings(this)))
        presenter.redirect()
    }

    override fun showMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun showSetUp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun setPresenter(presenter: LauncherContract.Presenter) {
        this.presenter = presenter
    }
}
