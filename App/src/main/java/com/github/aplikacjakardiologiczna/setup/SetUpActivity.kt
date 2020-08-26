package com.github.aplikacjakardiologiczna.setup

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.main.MainActivity
import com.github.aplikacjakardiologiczna.model.ErrorMessage
import com.github.aplikacjakardiologiczna.model.database.AppDatabase
import com.github.aplikacjakardiologiczna.model.database.repository.GroupRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_set_up.buttonConfirm
import kotlinx.android.synthetic.main.activity_set_up.editTextUsername
import kotlinx.android.synthetic.main.activity_set_up.linearLayoutSetUp
import kotlinx.android.synthetic.main.activity_set_up.spinnerGroup


class SetUpActivity : AppCompatActivity(), SetUpContract.View {

    private lateinit var presenter: SetUpContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up)

        val db = AppDatabase.getInstance(this)

        buttonConfirm.setOnClickListener {
            presenter.onConfirmButtonPressed(
                editTextUsername.text.toString(),
                spinnerGroup.selectedItemPosition
            )
        }

        setPresenter(
            SetUpPresenter(
                this,
                AppSettings(this),
                GroupRepository.getInstance(db.groupDao())
            )
        )

        presenter.onViewCreated()
    }

    override fun onGroupsLoaded(groups: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.item_spinner, groups)
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner)
        spinnerGroup.adapter = adapter
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

    override fun setPresenter(presenter: SetUpContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
