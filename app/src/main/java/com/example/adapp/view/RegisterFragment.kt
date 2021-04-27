package com.example.adapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.model.User
import com.example.adapp.presenter.AuthPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class RegisterFragment : Fragment(),AuthPresenter.View {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var regPresenter:AuthPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        regPresenter= AuthPresenter(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        registerB.setOnClickListener {
            val username=usernameRegisterET.text.toString()
            val email=emailRegisterET.text.toString()
            val password=passwordRegisterET.text.toString()
            val confirmPassword=rePasswordRegisterET.text.toString()
            val phoneNo=phoneRegisterET.text.toString()
            val isMailValid=android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if(username.isNotEmpty() && email.isNotEmpty()
                && password.isNotEmpty() && confirmPassword.isNotEmpty()
                &&phoneNo.isNotEmpty())
            {
                if(password!=confirmPassword)
                {
                    Toast.makeText(requireActivity(),"Password and confirm Password Doesn't match!",Toast.LENGTH_SHORT).show()
                }
                else if(password.length<8)
                {
                    passwordRegisterET.setError("Password should have minimum 8 characters")
                }
                else if(!isMailValid)
                {
                    emailRegisterET.setError("Enter a valid Email!")
                }
                else if(phoneNo.length!=10)
                {
                    phoneRegisterET.setError("Enter a valid Phone Number!")
                }
                else
                {
                    val isCreated=regPresenter.createAccount(username,email,password,phoneNo)
                    if(isCreated)
                    {
                        Toast.makeText(activity,"Registration Done Successfully.. login to continue", Toast.LENGTH_SHORT).show()
                        val loginFrag=SignInFragment()
                        activity!!.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.parentL,loginFrag)
                            .commit()
                    }
                    else
                    {
                        Toast.makeText(activity,"Registration Failed", Toast.LENGTH_SHORT).show()

                    }
                }
            }
            else
            {
                Toast.makeText(requireActivity(),"Enter all the fields!",Toast.LENGTH_SHORT).show()
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }
    fun createAccount(username: String, email: String, password: String, phoneNo: String) {
        val fAuth= FirebaseAuth.getInstance()
        fAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){
                    task ->
                if(task.isSuccessful)
                {
                    val user=fAuth.currentUser
                    val userObj= User(username,email,password,phoneNo)
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.uid).setValue(userObj)
                    Toast.makeText(activity,"Registration Done Successfully.. login to continue", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    val signInFragment=SignInFragment()

                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.parentL,signInFragment)
                        .commit()

                }
                else
                {
                    Toast.makeText(activity,"Unable to complete Registration..", Toast.LENGTH_SHORT).show()
                }

            }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }


}