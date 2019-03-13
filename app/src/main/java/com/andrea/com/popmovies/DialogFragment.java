package com.andrea.com.popmovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    //This interface will pass data from sort order dialog
    public interface passData{
        void onSelectedSortOrder(int selectedData);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.sort_order));
        builder.setItems(R.array.settings_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passData passData = (DialogFragment.passData) getActivity();
                if(which == 0){
                    passData.onSelectedSortOrder(0);
                }
                if(which == 1){
                    passData.onSelectedSortOrder(1);
                }
                if(which == 2){
                    passData.onSelectedSortOrder(2);
                }
            }
        });
        return builder.create();
    }
}
