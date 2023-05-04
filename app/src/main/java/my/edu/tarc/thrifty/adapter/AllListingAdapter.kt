package my.edu.tarc.thrifty.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.thrifty.databinding.AllProductListBinding
import my.edu.tarc.thrifty.fragment.ListingFragmentDirections
import my.edu.tarc.thrifty.model.AddProductModel

class AllListingAdapter  (val context: Context, val list:ArrayList<AddProductModel>)
    : RecyclerView.Adapter<AllListingAdapter.ProductViewHolder>(){

    inner class ProductViewHolder(val binding:AllProductListBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = AllProductListBinding.inflate(LayoutInflater.from(context),parent ,false)
        return  ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)
        holder.binding.tvProName.text = data.productName
        holder.binding.tvCat.text = data.productCategory
        holder.binding.tvCarbonCount.text = data.carbon + "kg carbon saved"
        holder.binding.tvPrice.text =  "RM" + data.productSp

        holder.binding.btnDelete.setOnClickListener {
//            val dao = AppDatabase.getInstance(context).productDao()

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete item")
            builder.setMessage("Are you sure you want to delete this product?")
            builder.setPositiveButton("Yes") { _, _ ->
                val db = Firebase.firestore
                val storageRef = db.collection("products").document(list[position].productId!!)

                storageRef.delete()
                    .addOnSuccessListener {
                        Toast.makeText(context,"Product deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.d("MyApp", e.toString())
                    }
            }
            builder.setNegativeButton("No", null)
            val dialog = builder.create()
            dialog.show()
        }
        holder.binding.btnEdit.setOnClickListener {
            val action = ListingFragmentDirections.actionListingFragmentToEditListingFragment(data.productId!!)
            findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}