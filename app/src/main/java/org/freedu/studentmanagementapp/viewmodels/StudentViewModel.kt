package org.freedu.studentmanagementapp.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.studentmanagementapp.models.Student
import org.freedu.studentmanagementapp.repositories.StudentRepository

class StudentViewModel:ViewModel() {
    private val repository = StudentRepository()

    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

    fun loadStudents() {
        repository.getStudents { students ->
            _students.value = students
        }
    }

    fun deleteStudent(studentId: String) {
        repository.deleteStudent(studentId) { success ->
            if (success) {
                loadStudents()
            }
        }
    }

    fun addStudent(student: Student, onComplete: (Boolean) -> Unit) {
        repository.addStudent(student) { success ->
            if (success) {
                loadStudents()
            }
            onComplete(success)
        }
    }

    fun updateStudent(student: Student, onComplete: (Boolean) -> Unit) {
        repository.updateStudent(student) { success ->
            if (success) {
                loadStudents()
            }
            onComplete(success)
        }
    }
}