package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.przyklad.firebase_yt_29_08.databinding.ActivityChoiceTeacherBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChoiceTeacher : AppCompatActivity() {

    private lateinit var binding : ActivityChoiceTeacherBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChoiceTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.readdataBtn.setOnClickListener {

            val userName : String = binding.etusername.text.toString()
            if  (userName.isNotEmpty()){

                readData(userName)



            }else{

                Toast.makeText(this,"Please enter the Teacher",Toast.LENGTH_SHORT).show()

            }

        }

    }

    private fun readData(userName: String) {

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(userName).get().addOnSuccessListener {

            if (it.exists()){

                val teachername = binding.etusername.text.toString()

                Toast.makeText(this,"Successfuly Read",Toast.LENGTH_SHORT).show()

                binding.etusername.text.clear()
                //binding.tvFirstName.text = firstname[0].toString()
                //binding.tvLastName.text = lastName.toString()
                //binding.tvAge.text = age.toString()
                println(teachername)
                val intent = Intent(this, ChoiceClass::class.java)
                intent.putExtra("key1", teachername)
                startActivity(intent)



            }else{

                Toast.makeText(this,"Teacher Doesn't Exist",Toast.LENGTH_SHORT).show()


            }

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


        }



    }
}