package com.example.harsh.androlearner;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
      Toast.makeText(parent.getContext(),
		"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString()+pos,
		Toast.LENGTH_SHORT).show();

      if(pos==0){

      }
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
  }

}