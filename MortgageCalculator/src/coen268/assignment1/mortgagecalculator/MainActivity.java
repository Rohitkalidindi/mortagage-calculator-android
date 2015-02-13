	package coen268.assignment1.mortgagecalculator;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	//Edit Text field where user enters the amount borrowed
	EditText et;

	//result to display Monthly payement
	//interest_disp to display annual interest
	TextView result,interest_disp;
	
	//SeekBar for user to input interest
	SeekBar seek;
	
	//Radio group with loan terms in years
	RadioGroup rgroup;
	RadioButton r1,r2,r3;
	
	//Check Box to include tax and insurance
	CheckBox tax;
	
	//calc button to calculate result
	//clr to reset all inputs from user to default
	Button calc,clr;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        et =(EditText) findViewById(R.id.editText);
        
        //Preventing user from entering 0's at the beginning of text and not more than 2 decimals on the end
        et.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            	//Storing amount entered in EditText view in txt string
            	String txt=et.getText().toString();
            
                //Preventing user from entering 0's at the beginning of text
                if (txt.matches("0") )
                {
                	et.setText("");
                }
               
                //When user enters decimal number with more than 1 decimal
                if(txt.length()>0 && txt.contains(".") && txt.charAt(txt.length()-1)!='.')
                {
             
                	//Splitting amount into two strings and checking how many numbers are there before and after the decimal
                	String[] part= txt.split("\\.");
	                String partA=part[0];
	            	String partB= part[1];
	            	
	            	//if length after decimal is more than 2 restring it to 2
	            	if(partB.length()>2)
	            	{
	            	et.setText(partA+"."+partB.charAt(0)+""+partB.charAt(1));
	            	et.setSelection(txt.length()-1);
	            	}
                }
                
                //when user enters no decimal
                else if(txt.length()>8 && txt.charAt(txt.length()-1)!='.')
                {
	               	et.setText(txt.charAt(0)+""+txt.charAt(1)+""+txt.charAt(2)+""+txt.charAt(3)+""+txt.charAt(4)+""+txt.charAt(5)+""+txt.charAt(6)+""+txt.charAt(7));
	               	et.setSelection(et.getText().toString().length());
	            }
                
            }
            @Override
            public void afterTextChanged(Editable arg0) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        }); 
        
        interest_disp=(TextView) findViewById(R.id.seek);
        
        //setting interest to default 10.0 when application starts
        interest_disp.setText("10.0");
        
        seek=(SeekBar) findViewById(R.id.seekBar1);
        //adding seek bar functionality when the bar is moved
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
        	 @Override 
        	   public void onProgressChanged(SeekBar seekBar, int progress, 
        	     boolean fromUser) {  
        		
        		//setting interest as double value by dividing progress(ranges from 0 to 200) by dividing it by 10.0
        		double interest=(double) (progress/10.0);
        	   
        		//displaying interest on the text field
        		interest_disp.setText(String.valueOf(interest)); 
        	   } 
        	   @Override 
        	   public void onStartTrackingTouch(SeekBar seekBar) { 
        	   } 

        	   @Override 
        	   public void onStopTrackingTouch(SeekBar seekBar) { 
        	   } 
        });
        
        //Initializing elements with xml
        rgroup=(RadioGroup) findViewById(R.id.radioGroup1);
        r1 =(RadioButton) findViewById(R.id.radio0);
        r2 =(RadioButton) findViewById(R.id.radio1);
        r3 =(RadioButton) findViewById(R.id.radio2);
        tax=(CheckBox) findViewById(R.id.checkBox1);
        result=(TextView) findViewById(R.id.textView5);
        calc=(Button) findViewById(R.id.button1);
        
        //adding button functionality(Calculate Monthly Payement Button)
        calc.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
		
				//monthly_payement stores result
				double monthly_payement;
				
				//Converting text from textfield showing interest to double and storing it in i
				double i=Double.valueOf(interest_disp.getText().toString());
				
				//Dividing it by 1200 to get monthly interest
				i=i/1200;
				
				//initializing loan term variable as 15*12 default value in case user does not select any button default is 15 years multiplied by 12 months in each year
				int term=15*12;
				
				//Checking if there is no amount entered in edittext view setting it to default value of 0 and preventing the application to crash
				if(et.getText().length()==0)
				{
					et.setText("0.0");
					et.setSelection(et.getText().toString().length());
				}
				
				//Converting value entered in edit text view to do double and storing it in p which now denotes amount borrowed
				double p= Double.valueOf(et.getText().toString());
				
				//Checking which Radio button is checked and setting appropriate loan term
				if(r1.isChecked())
				{
					term=15*12;
				}
				if(r2.isChecked())
				{
					term=20*12;
				}
				if(r3.isChecked())
				{
					term=30*12;
				}
				
				//if interest is 0.0 than calculating monthly payement
				if(i==0.0)
				{	
					monthly_payement=(double) (p/term);
				}
				
				//if interest is not 0.0 than calculating monthly payement
				else
				{
					monthly_payement=(double) (p*(i/(1-Math.pow(1+i, -term))));
				}
				
				// Checking if the check box for tax is checked and if checked calculating result with tax
				if(tax.isChecked())
				{
					monthly_payement+=0.001*p;
				}
				
				//Setting result textfield with result by rounding it to 2 decimal places
				monthly_payement= Math.round(monthly_payement*100)/100.0;
				result.setText(""+monthly_payement);
				
				
			}
		});
        
        //Adding additional button Clr to reset all the values so that the user can start fresh
        clr=(Button)findViewById(R.id.button2);
        clr.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
			et.setText("");
			interest_disp.setText("10.0");
			seek.setProgress(100);
			tax.setChecked(false);
			rgroup.clearCheck();
			r1.setChecked(true);
			result.setText("0");
			}
		});
        
    }
}