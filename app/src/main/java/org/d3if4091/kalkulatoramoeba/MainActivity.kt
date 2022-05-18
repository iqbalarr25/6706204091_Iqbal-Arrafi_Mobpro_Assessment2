package org.d3if4091.kalkulatoramoeba

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import org.d3if4091.kalkulatoramoeba.databinding.ActivityMainBinding
import org.d3if4091.kalkulatoramoeba.model.HasilAmoeba
import org.d3if4091.kalkulatoramoeba.ui.hitung.HitungViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: HitungViewModel by lazy {
        ViewModelProvider(this)[HitungViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnHitung.setOnClickListener{hitungAmoeba()}
        binding.btnReset.setOnClickListener{reset()}
        viewModel.getHasilAmoeba().observe(this) { showResult(it) }
    }

    private fun hitungAmoeba(){
        val jumlahAwalAmoeba = binding.jumlahAwalAmoebaInp.text.toString()
        //jumlah awal amoeba validation textview validation
        if(TextUtils.isEmpty(jumlahAwalAmoeba)){
            Toast.makeText(this, R.string.jumlah_awal_amoeba_kosong, Toast.LENGTH_LONG).show()
            return
        }
        val jumlahPembelahanAmoeba = binding.jumlahPembelahanAmoebaInp.text.toString()
        //jumlah pembelahan amoeba textview validation
        if(TextUtils.isEmpty(jumlahPembelahanAmoeba)){
            Toast.makeText(this, R.string.jumlah_pembelahan_amoeba_kosong, Toast.LENGTH_LONG).show()
            return
        }
        val rentangWaktu = binding.rentangWaktuInp.text.toString()
        //rentang waktu textview validation
        if(TextUtils.isEmpty(rentangWaktu)){
            Toast.makeText(this, R.string.rentang_waktu_membelah_kosong, Toast.LENGTH_LONG).show()
            return
        }
        val jangkaWaktu = binding.jangkaWaktuInp.text.toString()
        //jangka waktu textview validation
        if(TextUtils.isEmpty(jangkaWaktu)){
            Toast.makeText(this, R.string.jangka_waktu_kosong, Toast.LENGTH_LONG).show()
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
        binding.hasil.text = Math.round(Math.floor(hasil.hasilAmoeba.toDouble())).toString()
    }
    //dialog box konfirmasi sebelum reset halaman
    private fun reset(){
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
        builder.setPositiveButton("Yes"){dialog, which -> runReset()}
        builder.setNegativeButton("No") {dialog, which -> dialog.cancel()}
        builder.setTitle("Yakin ingin reset halaman?")
        builder.setMessage("Halaman yang direset tidak dapat dikembalikan.")
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
    }
    //untuk melakukan reset tampilan ke awal aplikasi dijalankan
    private fun runReset(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnHitung.setOnClickListener{hitungAmoeba()}
        binding.btnReset.setOnClickListener{reset()}
    }
}