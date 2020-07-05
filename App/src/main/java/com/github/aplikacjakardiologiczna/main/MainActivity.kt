package com.github.aplikacjakardiologiczna.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.heart.HeartFragment
import com.github.aplikacjakardiologiczna.model.database.AppDatabase
import com.github.aplikacjakardiologiczna.model.database.repository.TaskRepository
import com.github.aplikacjakardiologiczna.model.database.repository.UserTaskRepository
import com.github.aplikacjakardiologiczna.notification.NotificationUtils
import com.github.aplikacjakardiologiczna.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.drawer_navigation

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var toggle: ActionBarDrawerToggle
    private val heartFragment = HeartFragment()
    private val tasksFragment = TasksFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getInstance(this)

        setPresenter(
            MainPresenter(
                this,
                AppSettings(this),
                TaskRepository.getInstance(db.taskDao()),
                UserTaskRepository.getInstance(db.userTaskDao())
            )
        )
        presenter.onViewCreated()

        val notify = NotificationUtils(this)
        if (!notify.isAlarmUp())
            notify.setAlarm()

        setupBottomNavigation()
        setupDrawerNavigation()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showHeartView() {
        showFragment(heartFragment)
    }

    override fun showTasksView() {
        showFragment(tasksFragment)
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    private fun setupBottomNavigation() {
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
    }

    private fun setupDrawerNavigation() {
        toggle = ActionBarDrawerToggle(
            this, drawer_layout, 0, 0
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawer_navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_heart -> {
                    presenter.onHeartTabClicked()
                }
                R.id.navigation_tasks -> {
                    presenter.onTasksTabClicked()
                }
            }
            drawer_layout?.closeDrawer(GravityCompat.START)
            true
        }
    }
}
