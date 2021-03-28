package com.corgrimm.goodhusband.ui.notifications

import androidx.lifecycle.*
import com.corgrimm.goodhusband.ReminderRepository
import com.corgrimm.goodhusband.models.Reminder
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: ReminderRepository) : ViewModel() {

    val allReminders: LiveData<List<Reminder>> = repository.allReminders.asLiveData()
    fun insert(reminder: Reminder) = viewModelScope.launch {
        repository.insert(reminder)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}

class ReminderViewModelFactory(private val repository: ReminderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}