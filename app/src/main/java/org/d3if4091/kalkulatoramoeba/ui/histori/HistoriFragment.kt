package org.d3if4091.kalkulatoramoeba.ui.histori

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.d3if4091.kalkulatoramoeba.R
import org.d3if4091.kalkulatoramoeba.databinding.FragmentHistoriBinding
import org.d3if4091.kalkulatoramoeba.db.AmoebaDb
import org.d3if4091.kalkulatoramoeba.db.AmoebaEntity

class HistoriFragment : Fragment() {

    private lateinit var binding: FragmentHistoriBinding
    private lateinit var myAdapter: HistoriAdapter

    private val viewModel: HistoriViewModel by lazy {
        val db = AmoebaDb.getInstance(requireContext())
        val factory = HistoriViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HistoriViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoriBinding.inflate(layoutInflater,
            container, false)
        return binding.root
    }

    private var simpleCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(
        ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            myAdapter.getAmoebaEntity(viewHolder.adapterPosition)?.let { hapusData(it, viewHolder.adapterPosition) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myAdapter = HistoriAdapter()
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = myAdapter
            setHasFixedSize(true)
        }
        viewModel.data.observe(viewLifecycleOwner) {
            binding.emptyView.visibility = if (it.isEmpty())
                View.VISIBLE else View.GONE
            myAdapter.submitList(it)
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    fun hapusData(amoeba: AmoebaEntity, position: Int) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setPositiveButton("Yes"){ _, _ -> viewModel.hapusData(amoeba)}
        builder.setNegativeButton("No") { _, _ -> dialogCancel(position)}
        builder.setTitle("Yakin ingin hapus data tersebut?")
        builder.setMessage("Data yang telah dihapus tidak dapat dikembalikan.")
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun dialogCancel(position: Int){
        myAdapter.notifyItemChanged(position)
    }
}