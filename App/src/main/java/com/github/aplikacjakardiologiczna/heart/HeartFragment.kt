package com.github.aplikacjakardiologiczna.heart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository


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
    override fun showProgressBar(percent: Int) {
        progressBar.setProgressPercent(percent)

        val percentText: TextView = view!!.findViewById(R.id.percent)
        percentText.text = "$percent%"
    }

    override fun setPresenter(presenter: HeartContract.Presenter) {
        this.presenter = presenter
    }
}
