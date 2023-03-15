package com.purushotham.feedapp.fragments


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.purushotham.feedapp.AppConstants
import com.purushotham.feedapp.R
import com.purushotham.feedapp.adapter.FeedAdapter
import com.purushotham.feedapp.databinding.FragmentHomeBinding
import com.purushotham.feedapp.models.Feeds
import java.io.*


class HomeFragment : Fragment(), AddPostPopupFragment.DialogNextButtonClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popUpFragment: AddPostPopupFragment
    private lateinit var adapter: FeedAdapter
    private lateinit var mList: MutableList<Feeds>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getFeeds()
        registerEvents()
    }

    private fun getFeeds() {
        //databaseRef = FirebaseDatabase.getInstance().getReference("post")
        binding.pbLoading.visibility = View.VISIBLE
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    mList.clear()
                    for (feedSnapShot in snapshot.children) {
                        val feed = feedSnapShot.getValue(Feeds::class.java)
                        mList.add(feed!!)
                    }
                    adapter.notifyDataSetChanged()
                    binding.pbLoading.visibility = View.GONE
                    binding.tvDefaultMessage.visibility = View.GONE
                }else{
                    binding.tvDefaultMessage.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                binding.pbLoading.visibility = View.GONE
            }

        })

    }

    private fun registerEvents() {
        binding.fabAddPost.setOnClickListener {

            popUpFragment = AddPostPopupFragment()
            popUpFragment.setListener(this)
            popUpFragment.show(childFragmentManager, "AddPostPopupFragment")
        }
        binding.ivLogout.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Alert")
            builder.setMessage("Are you sure want to Logout?")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                dialog.dismiss()
                auth.signOut()
                isUserLoggedIn()
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()

        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef =
            FirebaseDatabase.getInstance().reference
                .child(auth.currentUser?.uid.toString())
                .child("post")
        binding.rcvPost.setHasFixedSize(true)
        binding.rcvPost.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = FeedAdapter(mList)
        binding.rcvPost.adapter = adapter
    }

    override fun onSaveTask(
        title: String,
        etPostTitle: TextInputEditText,
        description: String,
        etPostDescription: TextInputEditText,
        imageUri: Uri?
    ) {


        try {
            val imageStream: InputStream? = requireActivity().getContentResolver().openInputStream(
                imageUri!!
            )
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            val baos = ByteArrayOutputStream()
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            val encodedString: String = Base64.encodeToString(b, Base64.DEFAULT)

            Log.d("check string", encodedString.toString())

            val map: HashMap<String, String> = hashMapOf(
                "post_title" to title,
                "post_description" to description,
                "lat" to AppConstants.LAT_VALUE,
                "lng" to AppConstants.LANG_VALUE,
                "img" to encodedString,
                "created_time" to System.currentTimeMillis().toString()
            )
            databaseRef.push().setValue(map).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Posted successfully", Toast.LENGTH_SHORT).show()
                    etPostTitle.text = null
                    etPostDescription.text = null
                } else {
                    Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
                popUpFragment.dismiss()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    private fun isUserLoggedIn() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {


        } else {

            navController.navigate(R.id.action_homeFragment_to_signinFragment)
        }
    }


}