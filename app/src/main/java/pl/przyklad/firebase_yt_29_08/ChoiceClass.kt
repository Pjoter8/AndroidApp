package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.przyklad.firebase_yt_29_08.databinding.ActivityChoiceClassBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChoiceClass : AppCompatActivity() {

    private lateinit var binding : ActivityChoiceClassBinding
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChoiceClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.readdataclassBtn.setOnClickListener {


            val className : String = binding.etclassname.text.toString()
            val teachername=intent.getStringExtra("key1")



            //val className : String = binding.etclassname.text.toString()


            if  (className.isNotEmpty()){
                if(teachername!=null) {
                    readData(className, teachername)
                }


            }else{

                Toast.makeText(this,"Please enter the Class",Toast.LENGTH_SHORT).show()

            }



        }

    }

    private fun readData(className: String, teachername: String) {

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(teachername).get().addOnSuccessListener {

            if (it.exists()) {

                val classnamelist = it.child("className").value as List<String>
                val className = binding.etclassname.text.toString()

                binding.etclassname.text.clear()
                if (classnamelist.contains(className)) {
                    Toast.makeText(this, "Open Class Succes", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, Quiz::class.java)
                    intent.putExtra("key2", teachername)
                    intent.putExtra("key3", className)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Class Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}