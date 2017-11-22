package pub.tanzby.herodict;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
public class heroDetailActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    TextView name, birthday, contents;
    private Intent itent;
    ImageButton head;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);

        constraintLayout = (ConstraintLayout)findViewById(R.id.activity_hero_detail);
        name = (TextView) findViewById(R.id.tv_hero_name_detail);
        birthday = (TextView) findViewById(R.id.tv_heroBirthday_detail);
        contents = (TextView) findViewById(R.id.textView3);
        head = (ImageButton) findViewById(R.id.bnt_img_gravter_detail);

        // fill infomation
        itent = getIntent();
        Hero hero = (Hero) itent.getParcelableExtra("Hero");
        name.setText(hero.getName());
        birthday.setText(hero.getShengsi());
        contents.setText(hero.getContent());

        url = String.format("http://www.e3ol.com/biography/pic/id/240/%d.jpg",hero.getId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLimage(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();

        if(hero.getCata().equals("主效：蜀"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.shu));
        }
        else if(hero.getCata().equals("主效：魏"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.wei));
        }
        else if(hero.getCata().equals("主效：吴"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.wu));
        }
        else if(hero.getCata().equals("主效：刘表"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.liubiao));
        }
        else if(hero.getCata().equals("主效：刘璋"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.liuzhang));
        }
        else if(hero.getCata().equals("主效：起义军"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.qiyijun));
        }
        else if(hero.getCata().equals("主效：西晋"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.xijing));
        }
        else if(hero.getCata().equals("主效：刘表"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.liubiao));
        }
        else if(hero.getCata().equals("主效：东汉"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.donghan));
        }
        else if(hero.getCata().equals("主效：董卓"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.dongzhuo));
        }
        else if(hero.getCata().equals("主效：袁绍"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.yuanshao));
        }
        else if(hero.getCata().equals("主效：袁术"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.yuanshu));
        }
        else if(hero.getCata().equals("主效：在野"))
        {
            constraintLayout.setBackground(getDrawable(R.drawable.zaiye));
        }

    }

    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("revceived");
                    Bitmap bmp=(Bitmap)msg.obj;
                    head.setImageBitmap(bmp);
                    break;
            }
        };
    };


    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
