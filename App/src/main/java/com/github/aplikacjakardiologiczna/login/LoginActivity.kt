package com.github.aplikacjakardiologiczna.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.main.MainActivity
import com.github.aplikacjakardiologiczna.model.ErrorMessage
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.buttonConfirm
import kotlinx.android.synthetic.main.activity_login.editTextUsername
import kotlinx.android.synthetic.main.activity_login.linearLayoutSetUp


class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonConfirm.setOnClickListener {
            presenter.onConfirmButtonPressed(editTextUsername.text.toString())
        }

        val dynamoDb = DatabaseManager(this)
        setPresenter(
            LoginPresenter(
                this,
                AppSettings(this),
                UserTaskRepository(dynamoDb)
            )
        )
    }

    override fun showMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun showValidationError(errorMessage: ErrorMessage) {
        Snackbar
            .make(linearLayoutSetUp, errorMessage.stringResourceId, Snackbar.LENGTH_LONG)
            .show()
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
