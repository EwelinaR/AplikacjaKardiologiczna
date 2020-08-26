package com.github.aplikacjakardiologiczna.setup

import com.github.aplikacjakardiologiczna.AppSettings
import com.github.aplikacjakardiologiczna.model.ErrorMessage
import com.github.aplikacjakardiologiczna.model.database.Result
import com.github.aplikacjakardiologiczna.model.database.entity.Group
import com.github.aplikacjakardiologiczna.model.database.repository.GroupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SetUpPresenter(
    view: SetUpContract.View,
    private val settings: AppSettings,
    private val groupRepository: GroupRepository,
    private val uiContext: CoroutineContext = Dispatchers.Main
) : SetUpContract.Presenter, CoroutineScope {

    private var view: SetUpContract.View? = view
    private var job: Job = Job()
    private var groups = ArrayList<Group>()

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    override fun onViewCreated() {
        loadGroups()
    }

    override fun onConfirmButtonPressed(username: String, groupPosition: Int) {
        if (username.isNotEmpty()) {
            settings.username = username
            settings.groupId = groups[groupPosition].id
            view?.showMain()
        } else {
            view?.showValidationError(ErrorMessage.VALIDATION_ERROR_EMPTY_USERNAME)
        }
    }

    override fun onDestroy() {
        this.view = null
    }

    private fun loadGroups(): Job = launch {
        when (val result = groupRepository.getAllGroups()) {
            is Result.Success<List<Group>> -> onGroupsLoaded(result.data)
            is Result.Error -> {
                //TODO Show a snackbar/toast saying that something went wrong
            }
        }
    }

    private fun onGroupsLoaded(groups: List<Group>) {
        this.groups = ArrayList(groups)
        view?.onGroupsLoaded(groups.map {it.name})
    }
}
