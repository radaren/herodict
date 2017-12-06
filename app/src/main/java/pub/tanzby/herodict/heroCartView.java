package pub.tanzby.herodict;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class heroCartView extends AppCompatActivity {
    ImageButton imgBnt_add;
    RecyclerView mRecycleView;
    RCAdapter     mAdapter;
    ArrayList<Hero> heroList;
    Integer current_item_id;
    SeekBar seekbar;
    TextView cur_pos_tv;
    int is_moving_to = 0;
    boolean is_moving = false;


    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_cart_view);

        element_binding();
        Intent itent = getIntent();
        String heroName = itent.getStringExtra("heroName");

        // 1.7s vs 2.9s in emulator
        heroList = (ArrayList<Hero>) DataSupport.select("name", "pic","sex").limit(10).find(Hero.class);
        //heroList = (ArrayList<Hero>) DataSupport.findAll(Hero.class);
        seekbar.setMax(heroList.size()-1);
        even_binding();
        if(heroName != null)
        {
            for (int i = 0; i < heroList.size(); ++i)
            {
                if(heroList.get(i).getName().equals(heroName))
                {
                    mRecycleView.scrollToPosition(i);
                    break;
                }
            }
        }
    }
    public void element_binding()
    {
        seekbar = (SeekBar) findViewById(R.id.sk_for_card_view);
        cur_pos_tv  =(TextView) findViewById(R.id.tv_cur_pos);
        cur_pos_tv.setTypeface(Typeface.createFromAsset(getAssets(),"font/content.ttf"));
        /*set RecyclerView*/
        mRecycleView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecycleView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        imgBnt_add = (ImageButton) findViewById(R.id.imgbnt_add_new_hero);

        new LinearSnapHelper().attachToRecyclerView(mRecycleView);
    }


    public void even_binding() {
        mRecycleView.smoothScrollToPosition(2);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                /*update current item id*/
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecycleView.getLayoutManager();
                View firstVisibleItem = mRecycleView.getChildAt(0);

                if (dx < 0)
                {
                    current_item_id = layoutManager.findFirstVisibleItemPosition() + 1;
                }
                else
                {
                    current_item_id = layoutManager.findLastVisibleItemPosition() - 1;
                }

                cur_pos_tv.setText(current_item_id + "");


                if (!is_moving) seekbar.setProgress(current_item_id);
            }
        });
        mAdapter = new RCAdapter(this, heroList);
        mAdapter.setOnItemClickLitener(
                new RCAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(heroCartView.this, "短按", Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(heroCartView.this, heroDetailActivity.class);
                        newIntent.putExtra("id", heroList.get(position).getId());
                        startActivity(newIntent);
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        new AlertDialog.Builder(view.getContext(),R.style.AlertDialog)
                            .setTitle("警告")
                            .setMessage("是否要删除当前人物")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Hero hero = heroList.get(position);
                                    hero.setVotep("DEL");
                                    EventBus.getDefault().post(hero);
                                }
                            })
                            .setNegativeButton("取消",null).show();

                    }
                }
        );
        mRecycleView.setAdapter(mAdapter);

        EventBus.getDefault().register(heroCartView.this);

        imgBnt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(heroCartView.this,heroEditActivity.class);
                newIntent.putExtra("Opt","ADD");
                startActivity(newIntent);
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cur_pos_tv.setText(progress+"");
                mRecycleView.smoothScrollToPosition(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                is_moving = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                is_moving = false;
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent (Hero hero)
    {
        if(hero.getVotep().equals("DEL"))
        {
            MainActivity.heroArrayList.remove(hero);
            for (int position = 0; position < heroList.size(); ++position)
            {
                if(heroList.get(position).getName().equals(hero.getName()))
                {
                    mAdapter.removeData(position);
                    break;
                }
            }
        }
        if(hero.getVotep().equals("ADD"))
        {
            mAdapter.addData(hero);
            MainActivity.heroArrayList.add(hero);
            hero.save();
        }
        if(hero.getVotep().equals("UPD"))
        {
            for (int position = 0; position < heroList.size(); ++position)
            {
                if(heroList.get(position).getName().equals(hero.getName())
                        || heroList.get(position).getId().equals(hero.getId()))
                {
                    mAdapter.updateData(position,hero);

                    break;
                }
            }
        }
        seekbar.setMax(heroList.size()-1);
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

            if (velocityY >5)
            {
                finish();
            }

            return true;
        }
    }
}
