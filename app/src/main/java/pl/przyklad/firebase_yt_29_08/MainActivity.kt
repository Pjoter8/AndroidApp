package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.przyklad.firebase_yt_29_08.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.teacher.setOnClickListener {
            val intent = Intent(this, LoginTeacher::class.java)
            startActivity(intent)
        }
        binding.student.setOnClickListener {
            val intent = Intent(this, ChoiceTeacher::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        moveTaskToBack(true)
        finish()
    }
}