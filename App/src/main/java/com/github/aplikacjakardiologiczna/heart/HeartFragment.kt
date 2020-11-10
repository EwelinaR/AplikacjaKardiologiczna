package com.github.aplikacjakardiologiczna.heart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.android.synthetic.main.fragment_heart.*


class HeartFragment : Fragment(), HeartContract.View {

    private lateinit var presenter: HeartContract.Presenter
    private lateinit var progressBar: CustomProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dynamoDb = DatabaseManager(requireContext())
        setPresenter(
            HeartPresenter(
                this,
                UserTaskRepository(dynamoDb)
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_heart, container, false)

        progressBar = CustomProgressBar(context)
        val frame: FrameLayout = view.findViewById(R.id.progress_layout)
        frame.addView(progressBar)

        presenter.onCreateView()

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun showProgressBar(percentValue: Int) {
        progressBar.setProgressPercent(percentValue)

        percent.text = "$percentValue%"
    }

    override fun setPresenter(presenter: HeartContract.Presenter) {
        this.presenter = presenter
    }
}
