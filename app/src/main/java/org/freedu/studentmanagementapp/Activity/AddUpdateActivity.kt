package org.freedu.studentmanagementapp.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import org.freedu.studentmanagementapp.R
import org.freedu.studentmanagementapp.databinding.ActivityAddUpdateBinding
import org.freedu.studentmanagementapp.models.Student
import org.freedu.studentmanagementapp.viewmodels.StudentViewModel

class AddUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUpdateBinding
    private lateinit var viewModel: StudentViewModel
    private var student: Student? = null
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddUpdateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        student = intent.getParcelableExtra("student")

        student?.let {
            populateFields(it)
        }

        binding.profileImage.setOnClickListener {
            selectImage()
        }

        binding.addBtn.setOnClickListener {
            saveStudent()
        }


    }

    private fun populateFields(student: Student) {
        getDownloadUrl(student.studentId) { uri ->
            if (uri != null) {
                Glide.with(this).load(uri).into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.person)
            }
        }
        binding.fullNameEt.setText(student.fullName)
        binding.studentIdEt.setText(student.studentId)
        binding.subjectEt.setText(student.subject)
        binding.addressEt.setText(student.address)
        binding.emailEt.setText(student.email)
        binding.phoneEt.setText(student.phone)
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun saveStudent() {
        val fullName = binding.fullNameEt.text.toString()
        val studentId = binding.studentIdEt.text.toString()
        val subject = binding.subjectEt.text.toString()
        val address = binding.addressEt.text.toString()
        val email = binding.emailEt.text.toString()
        val phone = binding.phoneEt.text.toString()

        if (student == null && imageUri != null) {
            uploadImage(studentId) { imageUrl ->
                val newStudent = Student(imageUrl, fullName, studentId, subject, address, email, phone)
                viewModel.addStudent(newStudent) { success ->
                    if (success) {
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            student?.let {
                val updatedStudent = it.copy(
                    fullName = fullName,
                    studentId = studentId,
                    subject = subject,
                    address = address,
                    email = email,
                    phone = phone
                )
                viewModel.updateStudent(updatedStudent) { success ->
                    if (success) {
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun uploadImage(studentId: String, onComplete: (String?) -> Unit) {
        imageUri?.let { uri ->
            val storageReference = FirebaseStorage.getInstance().reference.child("profileImages/$studentId")
            storageReference.putFile(uri)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                        onComplete(downloadUri.toString())
                    }
                }
                .addOnFailureListener {
                    onComplete(null)
                }
        } ?: onComplete(null)
    }

    private fun getDownloadUrl(studentId: String, onComplete: (Uri?) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference.child("profileImages/$studentId")
        storageReference.downloadUrl
            .addOnSuccessListener { uri ->
                onComplete(uri)
            }
            .addOnFailureListener { exception ->
                if (exception is StorageException && exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    onComplete(null)
                } else {
                    onComplete(null)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                imageUri = it
                binding.profileImage.setImageURI(it)
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
