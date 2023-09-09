package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.przyklad.firebase_yt_29_08.databinding.ActivityEndQuizBinding

class EndQuiz : AppCompatActivity() {
    private lateinit var binding: ActivityEndQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEndQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var points=intent.getStringExtra("key8")
        var size=intent.getStringExtra("key9")
        val teachername=intent.getStringExtra("key6")
        val className=intent.getStringExtra("key7")

        binding.ScoreResult.text = "Your Score: "+points+"/"+size

        binding.RestartBtn.setOnClickListener {
            val intent = Intent(this, Quiz::class.java)
            intent.putExtra("key2", teachername)
            intent.putExtra("key3", className)
            startActivity(intent)
        }

        binding.HomePageBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
