package com.purushotham.feedapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.purushotham.feedapp.R
import com.purushotham.feedapp.databinding.FragmentSignUpBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun registerEvents() {
        binding.tvSingin.setOnClickListener {
            navControl.navigate(R.id.action_signUpFragment_to_signinFragment)
        }
         binding.btnSignUp.setOnClickListener {
             val email = binding.etEmail.text.toString()
             val password = binding.etPassword.text.toString()
             val cnfPassword = binding.etRePassword.text.toString()
             if(email.isNotEmpty() && password.isNotEmpty() && cnfPassword.isNotEmpty()){
                 if(password == cnfPassword){
                     binding.pbLoading.visibility = View.VISIBLE
                   auth.createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener {
                           if(it.isSuccessful){
                               Toast.makeText(context, "Registration Successfully", Toast.LENGTH_SHORT).show()
                               navControl.navigate(R.id.action_signUpFragment_to_homeFragment)
                           }else{
                               Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                           }
                           binding.pbLoading.visibility = View.GONE
                       }
                       .addOnFailureListener {
                           binding.pbLoading.visibility = View.GONE
                           Toast.makeText(context, it?.message, Toast.LENGTH_SHORT).show()
                       }
                 }else{
                     Toast.makeText(context, "Password & Confirm Password not match...!", Toast.LENGTH_SHORT).show()
                 }
             }else{
                 Toast.makeText(context, "Empty fields not allowed...!", Toast.LENGTH_SHORT).show()
             }

         }
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }


}