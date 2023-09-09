package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pl.przyklad.firebase_yt_29_08.databinding.ActivityChoiceClassBinding
import pl.przyklad.firebase_yt_29_08.databinding.ActivityQuizBinding
import android.view.View


interface OnWordListReceivedListener {
    fun onWordListReceived(wordList: List<Map<String, String>>)
}
class Quiz : AppCompatActivity() {



    private lateinit var binding : ActivityQuizBinding
    private lateinit var database : DatabaseReference
    private var index = 0
    private var points = 0
    private var control = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.translateword.visibility = View.INVISIBLE
        binding.checkwordBtn.setOnClickListener {


            val teachername=intent.getStringExtra("key2")
            val className=intent.getStringExtra("key3")
            val intent = Intent(this, EndQuiz::class.java)


            if (className != null && teachername != null) {
                readData(className, teachername, object : OnWordListReceivedListener {
                    override fun onWordListReceived(wordList: List<Map<String, String>>) {
                        var size :Int= wordList.size
                        val translateword : String = binding.translateword.text.toString()

                        if (index != 0 && index < size){
                            if(control ==1){
                                if (wordList[index].values.firstOrNull() ==  translateword){
                                    points++
                                    binding.quizEnglish.text = "CORRECT! " +wordList[index].values.firstOrNull() ?: "Brak klucza"
                                    index++
                                    control--
                                    binding.checkwordBtn.text = "Next"
                                    binding.translateword.text.clear()
                                    binding.translateword.visibility = View.INVISIBLE
                                }
                                else{
                                    binding.quizEnglish.text =""
                                    binding.quizEnglish.text = "WRONG! Correct: "+wordList[index].values.firstOrNull() ?: "Brak klucza"
                                    index++
                                    control--
                                    binding.checkwordBtn.text = "Next"
                                    binding.translateword.text.clear()
                                    binding.translateword.visibility = View.INVISIBLE
                                }
                            }
                            else{
                                binding.quizPolish.text = wordList[index].keys.firstOrNull() ?: "Brak klucza"
                                binding.quizEnglish.text = ""
                                control++
                                binding.checkwordBtn.text = "Check"
                                binding.translateword.visibility = View.VISIBLE
                            }
                        }
                        if(index>=size && control==1){
                            intent.putExtra("key8", points.toString())
                            intent.putExtra("key9", size.toString())
                            intent.putExtra("key6", teachername)
                            intent.putExtra("key7", className)
                            startActivity(intent)
                        }
                        if(index>=size && control==0){
                                control++
                            binding.checkwordBtn.text = "End"
                        }

                        if(index==0 && control ==1){
                            if (wordList[index].values.firstOrNull()==  translateword){
                                points++
                                binding.quizEnglish.text = "CORRECT! " +wordList[index].values.firstOrNull() ?: "Brak klucza"
                                index++
                                control--
                                binding.checkwordBtn.text = "Next"
                                binding.translateword.text.clear()
                                binding.translateword.visibility = View.INVISIBLE
                            }
                            else{
                                binding.quizEnglish.text =""
                                binding.quizEnglish.text = "WRONG! Correct: "+wordList[index].values.firstOrNull() ?: "Brak klucza"
                                index++
                                control--
                                binding.checkwordBtn.text = "Next"
                                binding.translateword.text.clear()
                                binding.translateword.visibility = View.INVISIBLE
                            }
                        }
                        if (index == 0 && control ==0){

                            binding.quizPolish.text = wordList[index].keys.firstOrNull() ?: "Brak klucza"
                            binding.quizEnglish.text = ""
                            control++
                            binding.checkwordBtn.text = "Check"
                            binding.translateword.visibility = View.VISIBLE
                        }
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChoiceTeacher::class.java)
        startActivity(intent)
    }
    private fun readData(className: String, teachername: String, listener: OnWordListReceivedListener) {

        database = FirebaseDatabase.getInstance().getReference("Classes")
        database.child(className).get().addOnSuccessListener {dataSnapshot ->

            if (dataSnapshot.exists()) {
                val wordlist = dataSnapshot.child("words").value as List<Map<String, String>>
                listener.onWordListReceived(wordlist)
            } else {
                Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}