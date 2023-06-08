package com.aakash.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.aakash.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"   //Metric Unit View
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"   //US Unit View
    }
    private var currentVisibleView: String = METRIC_UNITS_VIEW //A variable tp hold a value to make a selected view visible

    private var binding : ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //set title for action bar
        setSupportActionBar(binding?.toolbarBmiActivity)

        if (supportActionBar != null){
            //this for back button
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            //set title
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{_, checkedId:Int ->

            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE //METRIC Weight UNITS VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE //METRIC Height UNITS VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE //make weight view Gone
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.GONE //make height feet Visible.Gone
        binding?.tilMetricUsUnitHeightInch?.visibility = View.GONE //make height Inch Visible.Gone

        binding?.etMetricUnitHeight?.text!!.clear() //height value is cleared if it is a
        binding?.etMetricUnitWeight?.text!!.clear() //height value is cleared if it is a

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }
    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW //Current View is updated here.
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE //METRIC Weight UNITS INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE //METRIC Height UNITS INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE //make weight view VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE //make height feet Visible.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE //make height Inch Visible.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear() //weight value is cleared.
        binding?.etUsMetricUnitsHeightFeet?.text!!.clear() //height value is cleared.
        binding?.etUsMetricUnitsHeightInch?.text!!.clear() //height value is cleared.

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun displayBMIResult(bmi: Float){

        val bmiLabel : String
        val bmiDescription : String

        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of yourself! Workout maybe!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of yourself! Workout maybe!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class | (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }else{
            bmiLabel = "Obese Class | (Vey Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

            // is not display 12.22222 so use this to display 12.22
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }

    private fun validateMetricUnits():Boolean{
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }

    private fun calculateUnits(){
        if (currentVisibleView == METRIC_UNITS_VIEW){
            if (validateMetricUnits()){
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat() /100
                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue*heightValue)


                displayBMIResult(bmi)
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
            }
        }else{
            if (validateUsUnits()){
                val usUnitHeightValueFeet: String = binding?.etUsMetricUnitsHeightFeet?.text.toString()
                val usUnitHeightValueInch: String = binding?.etUsMetricUnitsHeightInch?.text.toString()
                val usUnitWeightValue: Float = binding?.etUsMetricUnitWeight?.text.toString().toFloat()

                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue*heightValue))

                displayBMIResult(bmi)
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUsUnits():Boolean{
        var isValid = true

        when{
            binding?.etUsMetricUnitWeight?.text.toString().isEmpty()->{
                isValid = false
            } binding?.etUsMetricUnitsHeightFeet?.text.toString().isEmpty()->{
                isValid = false
            }binding?.etUsMetricUnitsHeightInch?.text.toString().isEmpty()->{
                isValid = false
            }
        }
        return isValid
    }
}