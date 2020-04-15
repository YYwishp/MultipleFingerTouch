package com.gyx.multiplefingertouch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	private val tag = "MainActivity"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)





		square.setOnMultiTouchListener(object :SquareLinearLayout.MultiClickListener{
			override fun onThirdPointClickListener() {
				Log.e(tag,"三指点击")

			}

			override fun onSingleClickListener() {
				Log.e(tag,"一指点击")
			}

			override fun onSecondPointClickListener() {
				Log.e(tag,"二指点击")

			}
		})

	}
}
