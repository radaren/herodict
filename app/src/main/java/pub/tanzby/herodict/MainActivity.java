package pub.tanzby.herodict;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.abs;


public class MainActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ListView mListView;
    private TextView tv_title;
    private final String[] heroNames={"aaaaaa","bbbbbb","cccccc"};
    private ArrayAdapter mAdapter;
    private final float marginToTop = 50;
    private final int   timeIntervel=400;

    private GestureDetectorCompat mDetector;
    private boolean first_to_get=true;
    private float init_pos_y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        element_setting();
        even_binding();

    }

    private void  init()
    {
        tv_title = (TextView) findViewById(R.id.tv_main_title);
        mSearchView = (SearchView) findViewById(R.id.sv_searchHero);
        mListView = (ListView) findViewById(R.id.lv_searchHeroResult);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroNames);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    private void element_setting()
    {
        mListView.setVisibility(View.INVISIBLE);
        mListView.setAdapter(mAdapter);
        mListView.setTextFilterEnabled(true);
    }

    private void even_binding()
    {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);

                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });


        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(first_to_get)
                {
                    first_to_get = false;
                    init_pos_y = mSearchView.getY();
                }
                if (hasFocus)
                {
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            mListView.setVisibility(View.VISIBLE);
                            tv_title.setVisibility(View.INVISIBLE);
                            ObjectAnimator translationUp = ObjectAnimator.ofFloat(mSearchView, "Y",
                                    mSearchView.getY(),marginToTop);
                            translationUp.setDuration(timeIntervel);

                            ObjectAnimator translationUp2 = ObjectAnimator.ofFloat(mListView, "Y",
                                    mListView.getY(),marginToTop+mSearchView.getHeight());
                            translationUp2.setDuration(timeIntervel);
                            translationUp.setInterpolator(new DecelerateInterpolator());
                            translationUp2.setInterpolator(new DecelerateInterpolator());
                            translationUp2.start();
                            translationUp.start();

                        }
                    }, 200);

                }
                else
                {
                    ObjectAnimator translationUp = ObjectAnimator.ofFloat(mSearchView, "Y",
                            mSearchView.getY(),init_pos_y);
                    translationUp.setDuration(timeIntervel);

                    mListView.setVisibility(View.INVISIBLE);
                    ObjectAnimator translationUp2 = ObjectAnimator.ofFloat(mListView, "Y",
                            mListView.getY(),init_pos_y+mSearchView.getHeight());
                    translationUp2.setDuration(timeIntervel);

                    translationUp.start();
                    translationUp2.start();

                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            tv_title.setVisibility(View.VISIBLE);
                        }
                    },800);

                }
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
            Log.i(DEBUG_TAG, velocityX+" "+velocityY);

            if (velocityY <-5)
            {
                Intent newIntent = new Intent(MainActivity.this,heroCartView.class);
                startActivity(newIntent);
            }

            return true;
        }
    }


}
