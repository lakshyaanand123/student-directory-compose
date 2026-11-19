package com.mycompany.studentdirectory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mycompany.studentdirectory.data.StudentDao
import com.mycompany.studentdirectory.data.StudentEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(
    private val studentDao: StudentDao
) : ViewModel() {

    val students: StateFlow<List<StudentEntity>> = studentDao
        .observeAllStudents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addStudent(name: String, course: String) {
        val cleanName = name.trim()
        val cleanCourse = course.trim()
        if (cleanName.isEmpty() || cleanCourse.isEmpty()) return

        viewModelScope.launch {
            studentDao.insertStudent(
                StudentEntity(
                    name = cleanName,
                    course = cleanCourse
                )
            )
        }
    }

    class Factory(private val studentDao: StudentDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
                return StudentViewModel(studentDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}