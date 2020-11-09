package com.github.aplikacjakardiologiczna.heart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.aplikacjakardiologiczna.AppConstants
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.dynamodb.DatabaseManager
import com.github.aplikacjakardiologiczna.model.database.entity.UserInfo
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class HeartFragment : Fragment(), HeartContract.View, CoroutineScope {

    private lateinit var presenter: HeartContract.Presenter
    private lateinit var progressBar: CustomProgressBar

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPresenter(HeartPresenter(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setTasksPercent()

        val view = inflater.inflate(R.layout.fragment_heart, container, false)
        progressBar = CustomProgressBar(context)
        val frame: FrameLayout = view!!.findViewById(R.id.progress_layout)
        frame.addView(progressBar)

        return view
    }

    private fun setTasksPercent(): Job = launch {
        val userTaskRepository = UserTaskRepository(DatabaseManager(requireContext()))
        when (val result = userTaskRepository.getUserInfo()) {
            is Result.Success<UserInfo> -> onTaskDownloaded(result.data)
            is Result.Error -> {}
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onTaskDownloaded(userInfo: UserInfo) {
        val completed = userInfo.userTasks.filter { it.time != null }.count()
        val percent = (completed.toFloat()/AppConstants.TASKS_PER_DAY*100).toInt()

        progressBar.setProgressPercent(percent)
        val percentText: TextView = view!!.findViewById(R.id.percent)
        percentText.text = "$percent%"
    }

    override fun setPresenter(presenter: HeartContract.Presenter) {
        this.presenter = presenter
    }
}
