package pub.tanzby.herodict;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.FileNotFoundException;

public class editFragm_1 extends Fragment {
    OnClickGravter mCallback;
    //定义一个接口
    public interface OnClickGravter{
        //并实现一个方法，用来传值并在（onAttach()中绑定activity）
        public void onGravterClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_edit_fragm_1, container, false);
        ImageButton bnt_gravter = (ImageButton) view.findViewById(R.id.ed_gravater);
        bnt_gravter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onGravterClick();
            }
        });
        return view;

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnClickGravter) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


}
