package pub.tanzby.herodict;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;
import org.litepal.util.Const;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import eu.amirs.JSON;

import static java.lang.Math.abs;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ListView mListView;
    private TextView tv_title;
//    public static List<String> heroNames;
//    private ArrayAdapter mAdapter;
    public  static  ArrayList<Hero> heroArrayList;
    private LVAdapter mAdapter;

    private final float marginToTop = 50;
    private final int   timeIntervel=400;

    private GestureDetectorCompat mDetector;
    private boolean first_to_get=true;
    private float init_pos_y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveToDataBases(this, "products.json");

        init();

        element_setting();
        even_binding();

    }

    private void  init()
    {
        //heroArrayList = Hero.getDataFromXMLSource(this,R.xml.test_hero);
        mAdapter = new LVAdapter(this);
        mAdapter.setSourceList(heroArrayList);

        //heroNames = new ArrayList<String>();
        //Hero.getDataFromJsonSource(this,"products.json");
        tv_title = (TextView) findViewById(R.id.tv_main_title);
        tv_title.setTypeface(Typeface.createFromAsset(getAssets(),"font/content.ttf"));
        mSearchView = (SearchView) findViewById(R.id.sv_searchHero);
        mListView = (ListView) findViewById(R.id.lv_searchHeroResult);
        //mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroNames);
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
        mAdapter.setOnItemClickLitener(new LVAdapter.OnItemClickLitener() {
             @Override
             public void onItemClick(View view, int position) {
                 Intent newIntent = new Intent(MainActivity.this,heroDetailActivity.class);

                 newIntent.putExtra("id", mAdapter.getItem(position).getId());
                 startActivity(newIntent);
                 Toast.makeText(view.getContext(),mAdapter.getItem(position).getName(),Toast.LENGTH_SHORT).show();
             }
         });

        mSearchView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 mSearchView.setIconified(false);
             }
         });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent newIntent = new Intent(MainActivity.this,heroCartView.class);

                newIntent.putExtra("heroName", query);
                startActivity(newIntent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText)){
                    mAdapter.getFilter().filter(newText);
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
                            ObjectAnimator translationUp1 = ObjectAnimator.ofFloat(mSearchView, "Y",
                                    mSearchView.getY(),marginToTop);
                            ObjectAnimator translationUp2 = ObjectAnimator.ofFloat(mListView, "Y",
                                    mListView.getY(),marginToTop+mSearchView.getHeight());
                            ObjectAnimator translationUp3 = ObjectAnimator.ofFloat(tv_title,"alpha",1,0);

                            translationUp3.setDuration(timeIntervel);
                            translationUp1.setDuration(timeIntervel);
                            translationUp2.setDuration(timeIntervel);
                            translationUp1.setInterpolator(new DecelerateInterpolator());
                            translationUp2.setInterpolator(new DecelerateInterpolator());
                            translationUp3.setInterpolator(new DecelerateInterpolator());
                            translationUp3.setDuration(timeIntervel);
                            translationUp2.start();
                            translationUp1.start();
                            translationUp3.start();

                            mListView.setVisibility(View.VISIBLE);
                        }
                    }, 200);

                }
                else
                {
                    ObjectAnimator translationUp = ObjectAnimator.ofFloat(mSearchView, "Y",
                            mSearchView.getY(),init_pos_y);
                    ObjectAnimator translationUp2 = ObjectAnimator.ofFloat(mListView, "Y",
                            mListView.getY(),init_pos_y+mSearchView.getHeight());
                    ObjectAnimator translationUp3 = ObjectAnimator.ofFloat(tv_title,"alpha",0,1);

                    translationUp3.setDuration(timeIntervel);
                    translationUp.setDuration(timeIntervel);
                    translationUp2.setDuration(timeIntervel);

                    translationUp.start();
                    translationUp2.start();
                    translationUp3.start();

                    mListView.setVisibility(View.INVISIBLE);

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

    public void saveToDataBases(Context context, String fileName)
    {
        heroArrayList = (ArrayList<Hero>)DataSupport.findAll(Hero.class);// select("name").find(Hero.class);
        if(heroArrayList.size() > 10) {
//            for(Hero name:andDevs)
//            {
//                heroArrayList.add(name);
//                //heroNames.add(name.getName());
//            }
            return; // already exist database
        }
        JSON heroes = new JSON(tool_for_project.readFileFromAsset(context,"products.json")).key("products");
        for(int idx = 0; idx < heroes.count() ; ++idx)
        {
            Hero hero = new Hero();
            JSON heroInfo = heroes.index(idx);
            //heroNames.add(heroInfo.key("name").stringValue());
            hero.setPic(heroInfo.key("id").stringValue());
            hero.setContent(heroInfo.key("content").stringValue());
            hero.setCata(heroInfo.key("cata").stringValue());
            hero.setJiguan(heroInfo.key("jiguan").stringValue());
            hero.setName(heroInfo.key("name").stringValue());
            hero.setSex(heroInfo.key("sex").stringValue());
            hero.setShengsi(heroInfo.key("shengsi").stringValue());;
            hero.save();
            heroArrayList.add(hero);
        }
    }

}
