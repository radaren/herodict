package pub.tanzby.herodict;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
    ConstraintLayout constraintLayout;
    TextView name, birthday, contents;
    ImageButton head;
    int idx = -1;
    Hero hero;

    private PopupWindow mPopupWindow;
    private View rootView;
    private ImageButton img_popup_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        init();
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        update_items();
        load_img(this);
        set_background();
    }
    private void init()
    {
        constraintLayout = (ConstraintLayout)findViewById(R.id.activity_hero_detail);
        name = (TextView) findViewById(R.id.tv_hero_name_detail);
        name.setTypeface(Typeface.createFromAsset(getAssets(),"font/mini_fan_zhuanshu.ttf"));
        birthday = (TextView) findViewById(R.id.tv_heroBirthday_detail);
        birthday.setTypeface(Typeface.createFromAsset(getAssets(),"font/content.ttf"));
        contents = (TextView) findViewById(R.id.tv_content);
        contents.setTypeface(Typeface.createFromAsset(getAssets(),"font/content.ttf"));
        head = (ImageButton) findViewById(R.id.bnt_img_gravter_detail);
        
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
                                hero.setVotep("DEL");
                                EventBus.getDefault().post(hero);
                                finish();
                            }
                        })
                        .setNegativeButton("取消",null).show();

            }
        });
        popupView.findViewById(R.id.tv_pop_menu_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                Intent newIntent = new Intent(getApplicationContext(),heroEditActivity.class);
                newIntent.putExtra("Opt","ADJ");
                newIntent.putExtra("hero",hero);
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
    private void update_items()
    {
        idx = getIntent().getIntExtra("id",1);
        hero = (Hero) DataSupport.findAll(Hero.class, idx).get(0);

        name.setText(hero.getName());
        birthday.setText(hero.getShengsi());
        contents.setText(hero.getContent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idx!=-1)
        {
            hero = (Hero) DataSupport.findAll(Hero.class, idx).get(0);
            name.setText(hero.getName());
            birthday.setText(hero.getShengsi());
            contents.setText(hero.getContent());
            load_img(this);
            set_background();
        }
    }

    private void set_background()
    {
        if(hero.getCata().equals("主效：蜀"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.shu));
        }
        else if(hero.getCata().equals("主效：魏"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.wei));
        }
        else if(hero.getCata().equals("主效：吴"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.wu));
        }
        else if(hero.getCata().equals("主效：刘表"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.liubiao));
        }
        else if(hero.getCata().equals("主效：刘璋"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.liuzhang));
        }
        else if(hero.getCata().equals("主效：起义军"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.qiyijun));
        }
        else if(hero.getCata().equals("主效：西晋"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.xijing));
        }
        else if(hero.getCata().equals("主效：刘表"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.liubiao));
        }
        else if(hero.getCata().equals("主效：东汉"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.donghan));
        }
        else if(hero.getCata().equals("主效：董卓"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.dongzhuo));
        }
        else if(hero.getCata().equals("主效：袁绍"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.yuanshao));
        }
        else if(hero.getCata().equals("主效：袁术"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.yuanshu));
        }
        else if(hero.getCata().equals("主效：在野"))
        {
            constraintLayout.setBackground(getDrawable(R.mipmap.zaiye));
        }
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
            Log.i(DEBUG_TAG, velocityX+" "+velocityY);

            if (velocityX >1000)
            {
                finish();
            }

            return true;
        }
    }
    private void load_img(Context context) {
        Bitmap selectedImage = null;
        try {
            selectedImage = tool_for_project.readBitmapFromLocal(context,hero.getPic() + ".jpg");
        } catch (IOException e) {
            if (hero.getSex().equals("男")) {
                head.setImageResource(R.drawable.default_male);
            } else {
                head.setImageResource(R.drawable.default_female);
            }
        }
        if(selectedImage == null)
        {
            if (hero.getSex().equals("男")) {
                head.setImageResource(R.drawable.default_male);
            } else {
                head.setImageResource(R.drawable.default_female);
            }
        }else
        head.setImageBitmap(selectedImage);
        head.setBackgroundColor(Color.WHITE);
    }
}
