package org.d3if4091.kalkulatoramoeba.ui.hitung

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.d3if4091.kalkulatoramoeba.R
import org.d3if4091.kalkulatoramoeba.databinding.FragmentHitungBinding
import org.d3if4091.kalkulatoramoeba.model.HasilAmoeba
import kotlin.math.floor
import kotlin.math.roundToInt

class HitungFragment : Fragment() {

    private lateinit var binding: FragmentHitungBinding

    private val viewModel: HitungViewModel by lazy {
        ViewModelProvider(requireActivity())[HitungViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHitungBinding.inflate(inflater, container, false)
        binding = FragmentHitungBinding.inflate(layoutInflater)
        binding.btnHitung.setOnClickListener{hitungAmoeba()}
        binding.btnReset.setOnClickListener{reset()}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getHasilAmoeba().observe(requireActivity()) { showResult(it) }
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
        )
    }

    private fun showResult(hasil: HasilAmoeba?){
        if(hasil == null) return
        //fungsi ini untuk membulatkan kebawah dan menghilangkan koma pada angka
        binding.hasil.text = floor(hasil.hasilAmoeba.toDouble()).roundToInt().toString()
    }

    //dialog box konfirmasi sebelum reset halaman

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

}