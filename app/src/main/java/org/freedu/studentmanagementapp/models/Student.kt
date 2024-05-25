package org.freedu.studentmanagementapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val profileImage: String? ="",
    var fullName:String ="",
    var studentId:String = "",
    var subject:String = "",
    var address:String = "",
    var email:String = "",
    var phone:String = ""
):Parcelable
