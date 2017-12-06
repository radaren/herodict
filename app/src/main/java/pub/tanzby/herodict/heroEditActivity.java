package pub.tanzby.herodict;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class heroEditActivity extends AppCompatActivity implements editFragm_1.OnClickGravter {

    private ImageButton ed_imgbnt;
    private EditText ed_name;
    private String imgText = null;

    private ImageButton img_bnt_comfirm;
    private ProgressBar prog_top;
    private GestureDetectorCompat mDetector;
    private ImageButton img_bnt_close;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final Integer wait_for_exit_time = 10000;
    private final int GALLERY_CODE = 435,SHOT_CODE =124; // 这里的IMAGE_CODE是自己任意定义的
    private boolean isAdj = false;
    Intent it;
    Hero nowHero = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_edit);
        it = getIntent();
        isAdj = it.getStringExtra("Opt").equals("ADJ");
        if(isAdj) {
            nowHero = it.getParcelableExtra("hero");
        }
        init();
        even_binding();
    }
    private void init()
    {
        editFragm_1 f1 = new editFragm_1();
        Bundle b = new Bundle();
        if(isAdj)
        {
            b.putString("adj","true");
            b.putString("name", nowHero.getName());
            b.putString("pic",nowHero.getPic());
            b.putString("gender",nowHero.getSex());
            Bitmap selectedImage = null;
            try {
                selectedImage = tool_for_project.readBitmapFromLocal(this,nowHero.getPic() + ".jpg");
            } catch (IOException e) {

                if (nowHero.getSex().equals("男")) {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.default_male);
                    selectedImage      = ((BitmapDrawable) myDrawable).getBitmap();
                }
                else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.default_female);
                    selectedImage      = ((BitmapDrawable) myDrawable).getBitmap();
                }
            }
            if(selectedImage == null)
            {
                if (nowHero.getSex().equals("男")) {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.default_male);
                    selectedImage      = ((BitmapDrawable) myDrawable).getBitmap();
                }
                else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.default_female);
                    selectedImage      = ((BitmapDrawable) myDrawable).getBitmap();
                }
            }
            imgText = DateFormat.getDateTimeInstance().format(new Date());
            tool_for_project.saveBitmapToLocal(heroEditActivity.this,selectedImage, imgText+".jpg");
        }else
        {
            b.putString("adj","false");
        }
        f1.setArguments(b);
        /*fragments*/
        mFragments.add(f1);

        editFragm_2 f2 = new editFragm_2();
        Bundle b1 = new Bundle();
        if(isAdj)
        {
            b1.putString("adj","true");
            b1.putString("gender", nowHero.getSex());
            String ss = nowHero.getShengsi();
            b1.putString("sheng",ss.substring(ss.indexOf('(')+1, ss.indexOf('-')));
            b1.putString("si",ss.substring(ss.indexOf('-')+1, ss.indexOf(')')));
            b1.putString("zhuxiao", nowHero.getCata().substring(3));
        }else
        {
            b1.putString("adj","false");
        }
        f2.setArguments(b1);
        mFragments.add(f2);

        editFragm_3 f3 = new editFragm_3();
        Bundle b2= new Bundle();
        if(isAdj)
        {
            b2.putString("adj","true");
            b2.putString("ct", nowHero.getContent());
        }else
        {
            b2.putString("adj","false");
        }
        f3.setArguments(b2);
        mFragments.add(f3);

        ed_name = (EditText) findViewById(R.id.ed_name);
        //ed_name.setTypeface(Typeface.createFromAsset(getAssets(),"font/content.ttf"));
        prog_top  = (ProgressBar) findViewById(R.id.progressBar_top);
        img_bnt_close=(ImageButton) findViewById(R.id.imgbnt_close);
        img_bnt_comfirm = (ImageButton) findViewById(R.id.imgbnt_comfirm) ;
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        mViewPager =  (ViewPager) findViewById(R.id.vp_hero_edit);

        tool_for_project.Permission.verifyStoragePermissions(heroEditActivity.this);
        tool_for_project.Permission.verifyCameraPermissions(heroEditActivity.this);
    }

    private void even_binding()
    {
        img_bnt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new AlertDialog.Builder(heroEditActivity.this,R.style.AlertDialog)
                .setTitle("编辑警告")
                .setMessage("你正在放弃编辑的英雄")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("否",null).show();
            }
        });


        mFragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0)
            {
                return mFragments.get(arg0);
            }
        };

        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                ViewGroup.LayoutParams para = prog_top.getLayoutParams();
                para.width = tool_for_project.dip2px(heroEditActivity.this,24.0f)*(position+1);
                prog_top.setLayoutParams(para);
                img_bnt_comfirm.setVisibility( position == 2? View.VISIBLE:View.INVISIBLE);
                tool_for_project.SoftKeyBoardOpenOrHodden(getBaseContext(),prog_top.getRootView(),false);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2){ }
            @Override
            public void onPageScrollStateChanged(int arg0) {  }
        });

        img_bnt_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(heroEditActivity.this,R.style.AlertDialog)
                .setTitle(isAdj?"确认修改":"确认添加")
                .setMessage("已经完成一个英雄的编辑"+(isAdj?"是否立即更新到词典中?":"是否立即添加到词典中?"))
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText ed_name = (EditText)findViewById(R.id.ed_name);
                        EditText ct = (EditText)findViewById(R.id.ed_content);
                        TextView gender = (TextView)findViewById(R.id.ed_gender);
                        EditText sheng = (EditText)findViewById(R.id.ed_lifetime_start);
                        EditText si = (EditText)findViewById(R.id.ed_lifetime_end);
                        Hero hero = new Hero();
                        if (isAdj)hero=nowHero;

                        hero.setPic(imgText);
                        hero.setContent(ct.getEditableText().toString());
                        hero.setCata("主效："+((TextView)findViewById(R.id.ed_zhuxiao)).getText().toString());
                        hero.setName(ed_name.getEditableText().toString());
                        hero.setSex(gender.getText().toString());

                        hero.setShengsi("生卒("+sheng.getText()+"-"+si.getText()+")");

                        if(isAdj)
                        {
                            hero.setVotep("UPD");
                            hero.update(hero.getId());
                            EventBus.getDefault().post(hero);

                        }
                        else {
                            hero.setVotep("ADD");
                            EventBus.getDefault().post(hero);
                        }


                        Snackbar.make(v, "英雄"+ed_name.getEditableText().toString()+"已成功添加到数据库",Snackbar.LENGTH_LONG)
                        .setAction("Got it", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {finish();}
                        })
                        .setDuration(wait_for_exit_time)
                        .show();
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                finish();
                            }
                        },wait_for_exit_time);

                    }
                })
                .setNegativeButton("我再修改一下",null)
                .show();
            }
        });


    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onGravterClick() {
        new AlertDialog.Builder(heroEditActivity.this)
            .setItems(new String[]{"图库", "拍摄"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                        startActivityForResult(intent, GALLERY_CODE);
                        break;
                    case 1:
                        Intent intent_ = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent_, SHOT_CODE);
                        break;
                }
                }
            }).show();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.i(DEBUG_TAG, velocityX+" "+velocityY);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        ed_imgbnt = (ImageButton) findViewById(R.id.ed_gravater);
        int px = tool_for_project.dip2px(heroEditActivity.this,200);
        try {
            Bitmap bm = null;

            if (resultCode==Activity.RESULT_OK)
            {
                if (requestCode==GALLERY_CODE)
                {
                    bm = MediaStore.Images.Media.getBitmap( getContentResolver(), data.getData());
                }
                else if(requestCode==SHOT_CODE)
                {
                    bm = (Bitmap) data.getExtras().get("data");
                }
            }
            bm = ThumbnailUtils.extractThumbnail(bm,px , px);
            if(bm != null)
            {
                ed_imgbnt.setImageBitmap(bm);
                imgText = DateFormat.getDateTimeInstance().format(new Date());
                tool_for_project.saveBitmapToLocal(heroEditActivity.this,bm, imgText+".jpg");
            }else imgText="1";
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
