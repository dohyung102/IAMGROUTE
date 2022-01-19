package com.ssafy.groute.src.main.home

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.ssafy.groute.R

private const val TAG = "AreaFilterAdapter"
class AreaFilterAdapter (item:ArrayList<Place>) : RecyclerView.Adapter<AreaFilterAdapter.AreaViewHolder>(),
    Filterable {
    private var unFilteredList = item
    private var filteredList = item
    private var context:Context?=null
    var list = mutableListOf<Place>()
    var isHeart = false
    inner class AreaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val heartlottie = itemView.findViewById<LottieAnimationView>(R.id.area_abtn_heart)

        fun bindInfo(data : Place){
            Glide.with(itemView)
                .load(data.img)
                .into(itemView.findViewById(R.id.areaPlace_iv_img))

            itemView.findViewById<TextView>(R.id.areaPlace_tv_name).text =data.name
            itemView.findViewById<TextView>(R.id.areaPlace_tv_content).text = data.content
//            itemView.findViewById<TextView>(R.id.areaPlace_rb_rating) = data.review
            itemView.findViewById<TextView>(R.id.areaPlace_tv_info).text = data.info

            heartlottie.setOnClickListener {
                val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
                animator.addUpdateListener { animation ->
                    heartlottie.progress = animation.animatedValue as Float
                }
                animator.start()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_area_place_item,parent,false)
        return AreaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.apply {
            bindInfo(filteredList[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,list[position].name)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
    interface ItemClickListener{
        fun onClick(view:View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    override fun getFilter(): Filter {
        return object  : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //event
                val charString = constraint.toString()
                Log.d(TAG, "performFiltering: ${charString}")
                filteredList = if(charString.isEmpty()){
                    unFilteredList
                }else{
                    val filteringList = ArrayList<Place>()
                    for(item in unFilteredList!!){
//                        Log.d(TAG, "performFiltering: ${item}")
                        if(item!!.info.contains(charString)) filteringList.add(item)
                    }
                    filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                Log.d(TAG, "performFiltering: ${filterResults.values}")
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Place>
                notifyDataSetChanged()
            }

        }
    }

}