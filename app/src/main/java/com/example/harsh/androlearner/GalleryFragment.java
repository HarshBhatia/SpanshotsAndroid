package com.example.harsh.androlearner;

/**
 * Created by harsh on 20/09/16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GalleryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("AmazeLogs",this.getClass().getSimpleName()+" view created");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.gallery_view, container, false);
    }
}