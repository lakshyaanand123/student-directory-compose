package com.mycompany.studentdirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mycompany.studentdirectory.data.AppDatabase
import com.mycompany.studentdirectory.ui.StudentDirectoryScreen
import com.mycompany.studentdirectory.ui.StudentViewModel
import com.mycompany.studentdirectory.ui.theme.StudentDirectoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val dao = database.studentDao()

        setContent {
            StudentDirectoryTheme {
                val studentViewModel: StudentViewModel = viewModel(
                    factory = StudentViewModel.Factory(dao)
                )
                val students = studentViewModel.students.collectAsStateWithLifecycle().value

                StudentDirectoryScreen(
                    students = students,
                    onAddStudent = { name, course ->
                        studentViewModel.addStudent(name = name, course = course)
                    }
                )
            }
        }
    }
}