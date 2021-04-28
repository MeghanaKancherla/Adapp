package com.example.adapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.adapp.model.Response
import com.example.adapp.presenter.AuthPresenter
import com.example.adapp.presenter.FirebaseCallback
import com.example.adapp.presenter.MyAcountDataPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_my_account.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAccountFragment : Fragment(),MyAcountDataPresenter.View,FirebaseCallback,AuthPresenter.View {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var myAcountDataPresenter: MyAcountDataPresenter
    lateinit var authPresenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
        myAcountDataPresenter= MyAcountDataPresenter(this)
        myAcountDataPresenter.getAccountDetails(this)
        authPresenter=AuthPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val item=menu.add("Edit Details")
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title)
        {
            "Edit Details"->{
                accountUserNameET.isEnabled=true
                accountUserPhoneET.isEnabled=true
                cancelB.visibility=View.VISIBLE
                submitB.visibility=View.VISIBLE
                logoutB.visibility=View.INVISIBLE

            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        logoutB.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            activity!!.finish()
            startActivity(Intent(requireContext(),MainActivity::class.java))

        }
        cancelB.setOnClickListener{
            accountUserNameET.isEnabled=false
            accountUserPhoneET.isEnabled=false
            submitB.visibility=View.INVISIBLE
            cancelB.visibility=View.INVISIBLE
            logoutB.visibility=View.VISIBLE

        }
        submitB.setOnClickListener{
            displayAlertDialog()
            accountUserPhoneET.isEnabled=false
            accountUserNameET.isEnabled=false
            submitB.visibility=View.INVISIBLE
            cancelB.visibility=View.INVISIBLE
            logoutB.visibility=View.VISIBLE


        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun displayAlertDialog() {
        var builder= androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want make changes?")
        builder.setPositiveButton("No")  {
                dlg, i-> dlg.cancel()
        }
        builder.setNegativeButton("Yes") {dlg,i ->
            val changedUserName=accountUserNameET.text.toString()
            val changedPhoneNumber=accountUserPhoneET.text.toString()
           if(changedUserName.isEmpty())
           {
               accountUserNameET.setError("Username cannot be empty!")
           }
            if(changedPhoneNumber.isEmpty())
            {
                accountUserPhoneET.setError("Phone number cannot be empty")

            }
            if(changedPhoneNumber.length!=10)
            {
                accountUserPhoneET.setError("Enter a valid phone number!")
            }
            authPresenter.updateData(changedPhoneNumber,changedUserName)
        }
        builder.create().show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun sendToast(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(response: Response) {
        val userDetails=response.user!!
        accountUserEmailET.isEnabled=false
        accountUserNameET.isEnabled=false
        accountUserPhoneET.isEnabled=false
        accountUserPhoneET.setText(userDetails.phoneNumber)
        accountUserEmailET.setText(userDetails.email)
        accountUserNameET.setText(userDetails.username)
        accountPB.visibility=View.INVISIBLE
    }
}