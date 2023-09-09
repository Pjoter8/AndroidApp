package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pl.przyklad.firebase_yt_29_08.databinding.ActivityLoggedTeacherBinding



class LoggedTeacher : AppCompatActivity() {

    private  lateinit var binding: ActivityLoggedTeacherBinding
    private lateinit var  database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding= ActivityLoggedTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("key29")
        binding.AddClassBtn.setOnClickListener {

            val className = binding.className.text.toString()

            if (className.isNotEmpty()&&userName!=null) {
                readData(className, userName)
            }
            else {
                Toast.makeText(this, "Uncomplete data", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun readData(className: String, userName: String){
        database = FirebaseDatabase.getInstance().getReference("Classes")
        database.child(className).get().addOnSuccessListener {
            if (it.exists()){
                Toast.makeText(this,"This Class name is taken ",Toast.LENGTH_SHORT).show()
            }
            else{
                val ListOfWords = listOf(
                    mapOf("key1" to "value1"),
                )
                val newclass = Class(ListOfWords)
                database.child(className).setValue(newclass).addOnSuccessListener {
                    binding.className.text.clear()
                }
                addElementToList(userName, className)
                Toast.makeText(this,"Create new class ",Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun addElementToList(userName: String, className: String) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Users/$userName/className")

        // Odczytaj liczbę elementów w liście
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newIndex = dataSnapshot.childrenCount // Użyj liczby elementów jako nowego indeksu

                val newElementReference = reference.child(newIndex.toString())
                newElementReference.setValue(className)
                    .addOnSuccessListener {
                        // Tutaj możesz obsłużyć sukces
                        Toast.makeText(this@LoggedTeacher, "Element added successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoggedTeacher, Teacherlistclass::class.java)
                        intent.putExtra("key28", userName)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        // Tutaj możesz obsłużyć błąd
                        Toast.makeText(this@LoggedTeacher, "Error adding element: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Obsłuż błąd odczytu liczby elementów
                Toast.makeText(this@LoggedTeacher, "Error reading list: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}