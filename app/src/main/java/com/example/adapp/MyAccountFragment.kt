package com.example.adapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adapp.model.Response
import com.example.adapp.presenter.AccountPresenter
import com.example.adapp.presenter.FirebaseCallback
import com.google.firebase.auth.FirebaseAuth
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
class MyAccountFragment : Fragment(), AccountPresenter.View {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var accountPresenter: AccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        accountPresenter = AccountPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logoutB.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            activity!!.finish()
            startActivity(Intent(requireContext(),MainActivity::class.java))

        }
        getResponseUsingCallback()
        super.onViewCreated(view, savedInstanceState)
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

    private fun print(response: Response){
        response.user?.let { user ->
            userDetailsT.setText("${user.username}\n${user.email}")
        }
    }

    private fun getResponseUsingCallback(){
        accountPresenter.getAccountDetails(object : FirebaseCallback{
            override fun onResponse(response: Response) {
                print(response)
            }
        })
    }

    override fun sendToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }
}