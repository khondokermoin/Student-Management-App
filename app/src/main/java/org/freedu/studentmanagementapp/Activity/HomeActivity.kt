package org.freedu.studentmanagementapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.freedu.studentmanagementapp.R
import org.freedu.studentmanagementapp.adaptar.StudentAdaptar
import org.freedu.studentmanagementapp.databinding.ActivityHomeBinding
import org.freedu.studentmanagementapp.databinding.StudentListBinding
import org.freedu.studentmanagementapp.models.Student
import org.freedu.studentmanagementapp.viewmodels.StudentViewModel

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel:StudentViewModel
    private lateinit var studentAdaptar: StudentAdaptar
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
        viewModel.loadStudents()
        binding.addBtn.setOnClickListener {
            startActivity(Intent(this, AddUpdateActivity::class.java))
        }

    }

    private fun observeViewModel() {
        viewModel.students.observe(this) {
            studentAdaptar = StudentAdaptar(it, this::editStudent, this::deleteStudent)
            binding.studentRv.adapter = studentAdaptar
            studentAdaptar.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        studentAdaptar = StudentAdaptar(emptyList(), this::editStudent, this::deleteStudent)
        binding.studentRv.layoutManager = LinearLayoutManager(this)
        binding.studentRv.adapter = studentAdaptar
    }

    private fun editStudent(student: Student) {
        val intent = Intent(this, AddUpdateActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)
    }

    private fun deleteStudent(s: String) {
        viewModel.deleteStudent(s)
    }
}