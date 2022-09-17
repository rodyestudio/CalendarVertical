package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class TestAct: AppCompatActivity() {

    lateinit var datePicker:AppDatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(this).inflate(
            R.layout.frame,null,false
        )
        setContentView(
            view
        )
        datePicker = view.findViewById<AppDatePicker>(R.id.dtp)
        datePicker.fillData(
            arrayListOf<String>("a","b","c"),
            arrayListOf<DateData>().apply {
                add(DateData(Calendar.getInstance(), arrayListOf("1","2")))
                add(DateData(Calendar.getInstance().apply {
                                                          add(Calendar.DATE,1)
                }, arrayListOf("3","4")))
                add(DateData(Calendar.getInstance().apply {
                    add(Calendar.DATE,2)
                }, arrayListOf("3","4")))

            }
        )
        view.findViewById<AppDatePicker>(R.id.dtp).addOnClickListener(object:OnItemClickListener{
            override fun onItemClick(date: Calendar, data: List<String>?) {
                Log.e("iLOG","${date} ${data}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.test,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.tgl){
            val picker = MaterialDatePicker.Builder.datePicker()
            picker.setTitleText("Pilih Tanggal")
            val dialog = picker.build()
            dialog.addOnPositiveButtonClickListener{
                datePicker.focusTo(it)
            }
            dialog.show(supportFragmentManager,picker.toString())
        }
        return true
    }

}