package com.purushotham.feedapp.fragments

import android.annotation.SuppressLint
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
import com.purushotham.feedapp.databinding.FragmentSigninBinding


class SigninFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSigninBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun registerEvents() {

        binding.tvSingUp.setOnClickListener {
            navControl.navigate(R.id.action_signinFragment_to_signUpFragment)
        }
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty() ){
                binding.pbLoading.visibility = View.VISIBLE

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                binding.pbLoading.visibility = View.GONE
                                Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                                navControl.navigate(R.id.action_signinFragment_to_homeFragment)
                            }else{
                                binding.pbLoading.visibility = View.GONE
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            binding.pbLoading.visibility = View.GONE
                            Toast.makeText(context, it?.message, Toast.LENGTH_SHORT).show()
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