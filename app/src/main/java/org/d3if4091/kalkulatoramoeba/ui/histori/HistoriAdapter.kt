package org.d3if4091.kalkulatoramoeba.ui.histori

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if4091.kalkulatoramoeba.R
import org.d3if4091.kalkulatoramoeba.databinding.ItemHistoriBinding
import org.d3if4091.kalkulatoramoeba.db.AmoebaEntity
import org.d3if4091.kalkulatoramoeba.model.hitungAmoeba

class HistoriAdapter() : ListAdapter<AmoebaEntity, HistoriAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<AmoebaEntity>() {
                override fun areItemsTheSame(
                    oldData: AmoebaEntity, newData: AmoebaEntity
                ): Boolean {
                    return oldData.id == newData.id
                }
                override fun areContentsTheSame(
                    oldData: AmoebaEntity, newData: AmoebaEntity
                ): Boolean {
                    return oldData == newData
                }
            }
    }

    class ViewHolder(private val binding: ItemHistoriBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: AmoebaEntity) = with(binding){
            val hasilAmoeba = item.hitungAmoeba()
            tvAmoebaAwal.text = root.context.getString(R.string.amoeba_awal, hasilAmoeba.awalAmoeba.toString())
            tvPembelahanAmoeba.text = root.context.getString(R.string.pembelahan_amoeba, hasilAmoeba.pembelahanAmoeba.toString())
            tvRentangWaktu.text = root.context.getString(R.string.rentang_waktu, hasilAmoeba.rentangWaktu.toString())
            tvJangkaWaktu.text = root.context.getString(R.string.jangka_waktu, hasilAmoeba.jangkaWaktu.toString())
            tvHasilAmoeba.text = root.context.getString(R.string.hasil_amoeba, hasilAmoeba.hasilAmoeba.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoriBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            Log.d("HistoriAdapter", getItem(position).id.toString())
        }
        holder.bind(getItem(position))
    }

    fun getAmoebaEntity(position: Int): AmoebaEntity? {
        return getItem(position)
    }
}