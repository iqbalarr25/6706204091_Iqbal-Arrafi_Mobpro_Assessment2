package org.d3if4091.kalkulatoramoeba.ui.hitung

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.d3if4091.kalkulatoramoeba.R
import org.d3if4091.kalkulatoramoeba.data.SettingDataStore
import org.d3if4091.kalkulatoramoeba.data.dataStore
import org.d3if4091.kalkulatoramoeba.databinding.FragmentHitungBinding
import org.d3if4091.kalkulatoramoeba.db.AmoebaDb
import org.d3if4091.kalkulatoramoeba.model.HasilAmoeba
import kotlin.math.floor
import kotlin.math.roundToInt

class HitungFragment : Fragment() {

    private lateinit var binding: FragmentHitungBinding
    private var saveDatabaseManager = true
    private lateinit var DatabaseDataStore: SettingDataStore

    private val viewModel: HitungViewModel by lazy {
        val db = AmoebaDb.getInstance(requireContext())
        val factory = HitungViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HitungViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHitungBinding.inflate(inflater, container, false)
        binding = FragmentHitungBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        binding.btnHitung.setOnClickListener{hitungAmoeba()}
        binding.btnReset.setOnClickListener{reset()}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DatabaseDataStore = SettingDataStore(requireContext().dataStore)
        DatabaseDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            value -> saveDatabaseManager = value
            activity?.invalidateOptionsMenu()
        }
        viewModel.getHasilAmoeba().observe(requireActivity()) { showResult(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        val saveDatabaseButton= menu.findItem(R.id.menu_save_database)
        setText(saveDatabaseButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_histori -> {
                findNavController().navigate(R.id.action_hitungFragment_to_historiFragment)
                return true
            }
            R.id.menu_about -> {
                findNavController().navigate(R.id.action_hitungFragment_to_aboutFragment)
                return true
            }
            R.id.menu_save_database -> {
                saveDatabaseManager = !saveDatabaseManager
                lifecycleScope.launch {
                    DatabaseDataStore.saveSettingToPreferencesStore(saveDatabaseManager, requireContext())
                }
                setText(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hitungAmoeba(){
        val jumlahAwalAmoeba = binding.jumlahAwalAmoebaInp.text.toString()
        //jumlah awal amoeba validation textview validation
        if(TextUtils.isEmpty(jumlahAwalAmoeba)){
            Toast.makeText(context, R.string.jumlah_awal_amoeba_kosong, Toast.LENGTH_LONG).show()
            return
        }
        val jumlahPembelahanAmoeba = binding.jumlahPembelahanAmoebaInp.text.toString()
        //jumlah pembelahan amoeba textview validation
        if(TextUtils.isEmpty(jumlahPembelahanAmoeba)){
            Toast.makeText(context, R.string.jumlah_pembelahan_amoeba_kosong, Toast.LENGTH_LONG).show()
            return
        }
        val rentangWaktu = binding.rentangWaktuInp.text.toString()
        //rentang waktu textview validation
        if(TextUtils.isEmpty(rentangWaktu)){
            Toast.makeText(context, R.string.rentang_waktu_membelah_kosong, Toast.LENGTH_LONG).show()
            return
        }
        val jangkaWaktu = binding.jangkaWaktuInp.text.toString()
        //jangka waktu textview validation
        if(TextUtils.isEmpty(jangkaWaktu)){
            Toast.makeText(context, R.string.jangka_waktu_kosong, Toast.LENGTH_LONG).show()
            return
        }

        viewModel.hitungAmoeba(
            jumlahAwalAmoeba.toFloat(),
            jumlahPembelahanAmoeba.toFloat(),
            rentangWaktu.toFloat(),
            jangkaWaktu.toFloat(),
            saveDatabaseManager
        )
    }

    private fun setText(menuItem: MenuItem){
        if(saveDatabaseManager){
            menuItem.setTitle(R.string.unsave_database)
        }else{
            menuItem.setTitle(R.string.save_database)
        }
    }

    private fun showResult(hasil: HasilAmoeba?){
        if(hasil == null) return
        //fungsi ini untuk membulatkan kebawah dan menghilangkan koma pada angka
        binding.hasil.text = floor(hasil.hasilAmoeba.toDouble()).roundToInt().toString()
        binding.btnShareButton.setOnClickListener{shareData()}
        binding.btnShareButton.visibility = View.VISIBLE
    }


    private fun reset(){
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setPositiveButton("Yes"){ _, _ -> runReset()}
        builder.setNegativeButton("No") { dialog, _ -> dialog.cancel()}
        builder.setTitle("Yakin ingin reset halaman?")
        builder.setMessage("Halaman yang direset tidak dapat dikembalikan.")
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
    }
    //untuk melakukan reset tampilan ke awal aplikasi dijalankan
    private fun runReset(){
        viewModel.deleteHasilAmoeba()
        findNavController().navigate(
            R.id.action_hitungFragment_self
        )
    }

    private fun shareData(){
        val message = getString(
            R.string.bagikan_template,
            viewModel.getHasilAmoeba().value?.awalAmoeba.toString(),
            viewModel.getHasilAmoeba().value?.pembelahanAmoeba.toString(),
            viewModel.getHasilAmoeba().value?.rentangWaktu.toString(),
            viewModel.getHasilAmoeba().value?.jangkaWaktu.toString(),
            viewModel.getHasilAmoeba().value?.hasilAmoeba.toString(),
            )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if(shareIntent.resolveActivity(
                requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }
}