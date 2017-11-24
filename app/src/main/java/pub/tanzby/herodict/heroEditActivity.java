package pub.tanzby.herodict;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.Snapshot;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class heroEditActivity extends AppCompatActivity implements editFragm_1.OnClickGravter {

    private ImageButton ed_imgbnt;
    private EditText    ed_name;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_edit);
        init();
        even_binding();
    }

    private void init()
    {
        /*fragments*/
        mFragments.add(new editFragm_1());
        mFragments.add(new editFragm_2());
        mFragments.add(new editFragm_3());


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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(heroEditActivity.this.getCurrentFocus().getWindowToken(), 0);
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
                .setTitle("确认添加").setMessage("已经完成一个英雄的编辑，是否立即添加到词典中？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO: 获取每一个控件到值，并储存英雄到数据库中

                        Snackbar.make(v, "英雄"+"XX"+"已成功添加到数据库",Snackbar.LENGTH_LONG)
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
            ed_imgbnt.setImageBitmap(bm);
            tool_for_project.saveBitmapToLocal(heroEditActivity.this,bm,"1.jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
