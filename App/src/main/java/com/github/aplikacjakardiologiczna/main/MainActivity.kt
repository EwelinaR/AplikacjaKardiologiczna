package com.github.aplikacjakardiologiczna.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.heart.HeartFragment
import com.github.aplikacjakardiologiczna.notification.NotificationUtils
import com.github.aplikacjakardiologiczna.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setPresenter(MainPresenter(this))
        presenter.onViewCreated()

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_heart -> {
                    presenter.onHeartTabClicked()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_tasks -> {
                    presenter.onTasksTabClicked()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        if(!NotificationUtils().isAlarmUp(this))
            NotificationUtils().setNotification(this@MainActivity)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showHeartView() {
        val fragment = HeartFragment()
        showFragment(fragment)
    }

    override fun showTasksView() {
        val fragment = TasksFragment()
        showFragment(fragment)
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
    }
}
