package org.freedu.studentmanagementapp.adaptar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.freedu.studentmanagementapp.databinding.StudentListBinding
import org.freedu.studentmanagementapp.models.Student

class StudentAdaptar(
    private val students: List<Student>,
    val onEdit: (Student) -> Unit,
    val onDelete: (String) -> Unit
) : RecyclerView.Adapter<StudentAdaptar.StudentViewHolder>(){
    class StudentViewHolder(val binding: StudentListBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(StudentListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return students.size
    }


    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        Glide.with(holder.itemView.context).load(student.profileImage).into(holder.binding.profileImage)
        holder.binding.fullNameTxt.text = student.fullName
        holder.binding.studentIdTxt.text = student.studentId
        holder.binding.subjectTxt.text = student.subject
        holder.binding.addressTxt.text = student.address
        holder.binding.emailTxt.text = student.email
        holder.binding.phoneTxt.text = student.phone

        holder.binding.editBtn.setOnClickListener {
            onEdit(student)
        }
        holder.binding.deleteBtn.setOnClickListener {
            onDelete(student.studentId)
        }
    }
}