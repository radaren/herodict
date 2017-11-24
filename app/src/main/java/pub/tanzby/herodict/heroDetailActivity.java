package pub.tanzby.herodict;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class heroDetailActivity extends AppCompatActivity {


    private GestureDetectorCompat mDetector;
    private PopupWindow mPopupWindow;
    private View rootView;
    private ImageButton img_popup_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        img_popup_menu = (ImageButton) findViewById(R.id.imgbnt_popup_menu);
        rootView = findViewById(R.id.activity_hero_detail);
        ((TextView)findViewById(R.id.tv_content)).setMovementMethod(ScrollingMovementMethod.getInstance());

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        final View popupView = getLayoutInflater().inflate(R.layout.popupwindow_style, null);

        mPopupWindow = new PopupWindow(popupView,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        popupView.findViewById(R.id.tv_pop_menu_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO ...
                mPopupWindow.dismiss();
                Toast.makeText(getApplicationContext(),"取消",Toast.LENGTH_LONG).show();
            }
        });

        popupView.findViewById(R.id.tv_pop_menu_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                new AlertDialog.Builder(v.getContext(),R.style.AlertDialog)
                        .setTitle("警告")
                        .setMessage("是否要删除当前词条")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getApplicationContext(),"删除",Toast.LENGTH_LONG).show();
                                // TODO finish() 以及一些必要的操作
                            }
                        })
                        .setNegativeButton("取消",null).show();

            }
        });
        popupView.findViewById(R.id.tv_pop_menu_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPopupWindow.dismiss();
                Intent newIntent = new Intent(getApplicationContext(),heroEditActivity.class);
                startActivity(newIntent);

            }
        });
        img_popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.i(DEBUG_TAG, "X: "+velocityX+"  Y: "+velocityY);

            if (velocityX >1000)
            {
                finish();
            }
            return true;
        }


    }
}
