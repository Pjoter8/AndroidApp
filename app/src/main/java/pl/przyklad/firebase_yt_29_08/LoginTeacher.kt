package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pl.przyklad.firebase_yt_29_08.databinding.ActivityLoginTeacherBinding

class LoginTeacher : AppCompatActivity() {
    private lateinit var binding: ActivityLoginTeacherBinding
    private lateinit var  database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginTeacherBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.textViewLogin.setOnClickListener {
            val intent = Intent(this, RegisterTeacher::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if(username.isNotEmpty() && password.isNotEmpty()){
                readDataTeacher(username, password)
            }
            else{
                Toast.makeText(this,"Uncomplete data", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun readDataTeacher (username: String, password: String){
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(username).get().addOnSuccessListener {
            if (it.exists()){
                val passwordDB = it.child("password").value as String
                if (passwordDB == password){
                    binding.username.text.clear()
                    binding.password.text.clear()
                    Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Teacherlistclass::class.java)
                    intent.putExtra("key28", username)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Wrong password",Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(this,"Teacher Doesn't Exist",Toast.LENGTH_SHORT).show()
            }
        }
    }
}