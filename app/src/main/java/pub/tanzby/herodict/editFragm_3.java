package pub.tanzby.herodict;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by tan on 2017/11/23.
 */

public class editFragm_3 extends Fragment {
    protected View frameView = null;
    private EditText editText;
    private static final int timeIntervel = 400;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (frameView == null)
        {

            frameView = inflater.inflate(R.layout.activity_edit_fragm_3, container, false);

            editText = (EditText) frameView.findViewById(R.id.ed_content);

            final ConstraintLayout mainLayout = (ConstraintLayout) editText.getRootView();

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                }
            });


            final ConstraintLayout root = (ConstraintLayout) editText.getRootView();
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    root.setFocusable(true);
                    root.setFocusableInTouchMode(true);
                    root.requestFocus();
                    editText.clearFocus();
                }
            });

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                private int moveDis = tool_for_project.dip2px(getContext(),180);
                private static final int timeIntervel = 500;
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ValueAnimator f = null;
                    if (hasFocus)
                    {
                        f = ValueAnimator.ofInt(
                        tool_for_project.dip2px(getContext(),400),
                        tool_for_project.dip2px(getContext(),200));
                    }
                    else
                    {
                        f = ValueAnimator.ofInt(
                                tool_for_project.dip2px(getContext(),200),
                                tool_for_project.dip2px(getContext(),400));
                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                    }

                    f.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        // 启动动画之后, 会不断回调此方法来获取最新的值
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            // 获取最新的高度值
                            ViewGroup.LayoutParams para = editText.getLayoutParams();
                            para.height = (Integer) animator.getAnimatedValue();
                            editText.setLayoutParams(para);
                        }
                    });

                    f.setDuration(timeIntervel);
                    f.start();


                }
            });




        }
        return  frameView;
    }
}
