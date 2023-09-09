package pl.przyklad.firebase_yt_29_08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.przyklad.firebase_yt_29_08.databinding.ActivityRegisterTeacherBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterTeacher : AppCompatActivity() {
    private  lateinit var binding: ActivityRegisterTeacherBinding
    private lateinit var  database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewReg.setOnClickListener {
            val intent = Intent(this, LoginTeacher::class.java)
            startActivity(intent)
        }
        binding.registerBtn.setOnClickListener {
            val username=binding.username.text.toString()
            val  email=binding.email.text.toString()
            val  password=binding.password.text.toString()
            val confirmpassword=binding.confirmpassword.text.toString()



            database=FirebaseDatabase.getInstance().getReference("Users")
            if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()){
                if(password == confirmpassword) {
                    readData(username, email, password)
                }
                else{
                    Toast.makeText(this,"Entered password is not equal",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Uncomplete data",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, LoginTeacher::class.java)
        startActivity(intent)
    }
    private fun readData(username: String, email: String, password: String){
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(username).get().addOnSuccessListener {
            if (it.exists()){
                Toast.makeText(this,"Teacher-name is already exist", Toast.LENGTH_SHORT).show()
            }
            else{
                val confirmpassword = password
                val  user=User(username,email,password,confirmpassword)
                database.child(username).setValue(user).addOnSuccessListener {
                    binding.username.text.clear()
                    binding.email.text.clear()
                    binding.password.text.clear()
                    binding.confirmpassword.text.clear()

                    Toast.makeText(this,"Create account Success ", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginTeacher::class.java)
                        startActivity(intent)
                }.addOnFailureListener{
                    Toast.makeText(this,"Failed ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}