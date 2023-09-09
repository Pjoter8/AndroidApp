package pl.przyklad.firebase_yt_29_08


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pl.przyklad.firebase_yt_29_08.databinding.ActivityAddWordsBinding
import android.content.Intent


class AddWords : AppCompatActivity() {
    private lateinit var binding: ActivityAddWordsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AddWordBtn.setOnClickListener {


            val PolishWord = binding.PolishWordAddET.text.toString()
            val EnglishWord = binding.EnglishWordAddET.text.toString()

            val classname = intent.getStringExtra("key32")
            val username = intent.getStringExtra("key29")

            if (PolishWord.isNotEmpty() && EnglishWord.isNotEmpty() && classname != null&& username!=null) {
                binding.EnglishWordAddET.text
                readData(classname, PolishWord, EnglishWord,username)

            } else {
                Toast.makeText(this@AddWords, "Uncomplete data", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onBackPressed() {
        val classname = intent.getStringExtra("key32")
        val username = intent.getStringExtra("key29")
        val intent = Intent(this, WordsList::class.java)
        intent.putExtra("key30", classname)
        intent.putExtra("key29", username)
        startActivity(intent)
    }

    private fun readData(classname: String, PolishWord: String, EnglishWord: String,username:String){

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Classes/" + classname + "/words")

        val newWord = mapOf(EnglishWord to PolishWord)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newIndex = dataSnapshot.childrenCount

                val newElementReference = reference.child(newIndex.toString())
                newElementReference.setValue(newWord)
                    .addOnSuccessListener {

                        Toast.makeText(this@AddWords, "Element added successfully", Toast.LENGTH_SHORT).show()
                        binding.PolishWordAddET.text.clear()
                        binding.EnglishWordAddET.text.clear()
                        val intent = Intent(this@AddWords, WordsList::class.java)
                        intent.putExtra("key30", classname)
                        intent.putExtra("key29", username)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@AddWords, "Error adding element: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Toast.makeText(this@AddWords, "Error reading list: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



