package com.purushotham.feedapp.fragments


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.purushotham.feedapp.R
import com.purushotham.feedapp.databinding.FragmentAddPostPopupBinding

class AddPostPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddPostPopupBinding
    private var imageUri: Uri? = null

    private lateinit var listener: DialogNextButtonClickListener
    fun setListener(listener: DialogNextButtonClickListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvent()

    }

    private fun registerEvent() {
        binding.btnPost.setOnClickListener {
            val postTitle = binding.etPostTitle.text.toString()
            val postDescription = binding.etPostDescription.text.toString()
            if(postTitle.isNotEmpty() && postDescription.isNotEmpty()){
                if (imageUri != null && !imageUri!!.equals(Uri.EMPTY)) {
                    listener.onSaveTask(postTitle, binding.etPostTitle, postDescription, binding.etPostDescription, imageUri)
                } else {
                    Toast.makeText(context, "Please upload image", Toast.LENGTH_SHORT).show()
                }


            }else{
                Toast.makeText(context, "Please Enter All details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivClose.setOnClickListener{
            dismiss()
        }
        binding.btnUploadImage.setOnClickListener {

            resultLauncher.launch("image/*")
        }
    }
    interface DialogNextButtonClickListener{
        fun onSaveTask(
            title: String,
            etPostTitle: TextInputEditText,
            description: String,
            etPostDescription: TextInputEditText,
            imageUri: Uri?
        )
    }
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){

        imageUri = it
        binding.ivPost.setImageURI(it)
    }


}