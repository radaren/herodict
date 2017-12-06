package pub.tanzby.herodict;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.IOException;

public class editFragm_1 extends Fragment {
    OnClickGravter mCallback;

    private boolean initialize = true;
    //定义一个接口
    public interface OnClickGravter{
        //并实现一个方法，用来传值并在（onAttach()中绑定activity）
        public void onGravterClick();
    }
    protected View frameView = null;
    private  EditText ed_name;
    ImageButton head;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (frameView==null)
        {
            frameView = inflater.inflate(R.layout.activity_edit_fragm_1, container, false);
            final ImageButton bnt_gravter = (ImageButton) frameView.findViewById(R.id.ed_gravater);
            bnt_gravter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onGravterClick();
                }
            });
            ed_name = (EditText) frameView.findViewById(R.id.ed_name);
            ed_name.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"font/content.ttf"));
            head = frameView.findViewById(R.id.ed_gravater);
            final ConstraintLayout root = (ConstraintLayout) ed_name.getRootView();
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    root.setFocusable(true);
                    root.setFocusableInTouchMode(true);
                    root.requestFocus();
                    ed_name.clearFocus();
                }
            });

            ed_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                private int moveDis = tool_for_project.dip2px(getContext(),180);
                private static final int timeIntervel = 500;
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    ObjectAnimator translationUp1 = ObjectAnimator.ofFloat(ed_name, "Y",
                            ed_name.getY(),ed_name.getY()+(hasFocus?-moveDis:moveDis));
                    ObjectAnimator translation2 = ObjectAnimator.ofFloat(bnt_gravter,"alpha",
                            hasFocus?0:1,!hasFocus?1:0);
                    translationUp1.setDuration(timeIntervel);
                    translation2.setDuration(timeIntervel);
                    translationUp1.setInterpolator(new DecelerateInterpolator());
                    translation2.setInterpolator(new DecelerateInterpolator());
                    translationUp1.start();
                    translation2.start();

                    if (!hasFocus)
                    {
                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                    }
                }
            });
        }

        return frameView;

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle.getString("adj").equals("true") && initialize)
        {
            ed_name.setText(bundle.getString("name"));
            Bitmap selectedImage = null;
            try {
                selectedImage = tool_for_project.readBitmapFromLocal(frameView.getContext()
                        ,bundle.getString("pic") + ".jpg");
            } catch (IOException e) {
                if (bundle.getString("gender").equals("男")) {
                    head.setImageResource(R.drawable.default_male);
                } else {
                    head.setImageResource(R.drawable.default_female);
                }
            }
            if(selectedImage == null)
            {
                if (bundle.getString("gender").equals("男")) {
                    head.setImageResource(R.drawable.default_male);
                } else {
                    head.setImageResource(R.drawable.default_female);
                }
            }else
                head.setImageBitmap(selectedImage);
            initialize = false;
        }
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
