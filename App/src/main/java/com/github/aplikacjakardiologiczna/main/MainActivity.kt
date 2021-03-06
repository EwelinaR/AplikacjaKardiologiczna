package com.github.aplikacjakardiologiczna.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.R
import com.github.aplikacjakardiologiczna.heart.HeartFragment
import com.github.aplikacjakardiologiczna.login.LoginActivity
import com.github.aplikacjakardiologiczna.notification.NotificationUtils
import com.github.aplikacjakardiologiczna.tasks.TasksFragment
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.drawer_navigation

class MainActivity : AppCompatActivity(), MainContract.View {

    companion object {
        const val EXTRA_SHOW_TASKS = "EXTRA_SHOW_TASKS"
    }

    private lateinit var presenter: MainContract.Presenter
    private lateinit var toggle: ActionBarDrawerToggle
    private val heartFragment = HeartFragment()
    private val tasksFragment = TasksFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setPresenter(
            MainPresenter(
                this,
                AppSettings(this),
                NotificationUtils(this)
            )
        )
        presenter.onViewCreated()

        setupBottomNavigation()
        setupDrawerNavigation()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        intent.extras?.let {
            if (it.containsKey(EXTRA_SHOW_TASKS)) {
                val shouldShowTasks = intent.getBooleanExtra(EXTRA_SHOW_TASKS, false)
                if (shouldShowTasks) bottom_navigation.selectedItemId = R.id.navigation_tasks
            }
        }
        super.onResume()
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

    override fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
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
                R.id.navigation_logout -> {
                    presenter.logout()
                }
            }
            drawer_layout?.closeDrawer(GravityCompat.START)
            true
        }
    }
}
