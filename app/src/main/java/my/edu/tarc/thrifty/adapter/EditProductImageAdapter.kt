package my.edu.tarc.thriftyadmin.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import my.edu.tarc.thrifty.R
import my.edu.tarc.thrifty.databinding.EditProductImageItemBinding
import my.edu.tarc.thrifty.databinding.LayoutCategoryItemBinding
import my.edu.tarc.thrifty.fragment.HomeFragmentDirections
import my.edu.tarc.thrifty.model.CategoryModel

class EditProductImageAdapter(var context: Context, val list : ArrayList<Uri>, val productId:String)
    : RecyclerView.Adapter<EditProductImageAdapter.EditProductImageViewHolder>(){
    inner class EditProductImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = EditProductImageItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProductImageViewHolder {
        return EditProductImageViewHolder(
            LayoutInflater.from(context).inflate(R.layout.edit_product_image_item,parent,false))
    }

    override fun onBindViewHolder(holder: EditProductImageViewHolder, position: Int) {
        val deleteButton = holder.binding.deleteImg
        Glide.with(context).load(list[position].toString()).into(holder.binding.prodImgEdit)

        deleteButton.setOnClickListener{
            val currentPosition = holder.bindingAdapterPosition

            Toast.makeText(context,"Image delete in adapter for $productId:$list[position]",Toast.LENGTH_SHORT).show()
            val deletedImgUrl = list[currentPosition] // Get the URL to delete
//            list.remove(list[position])
            list.removeAt(currentPosition) // Remove the item from the list
            notifyItemRemoved(position)
            notifyDataSetChanged()
            Log.d("MyApp",list.toString())


            Log.d("MyApp","deletedImgUrl: $deletedImgUrl")

            // Delete the file
            // Assume you have a reference to the document that contains the image URL
            val docRef =Firebase.firestore.collection("products").document(productId)
            // Assume the image URL is stored in an array field called "photos"
            docRef.update("productImages", FieldValue.arrayRemove(deletedImgUrl)) // Remove the URL from the array
                .addOnSuccessListener {
                    Log.d("MyApp", "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e ->
                    Log.w("MyApp", "Error updating document", e)
                }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}