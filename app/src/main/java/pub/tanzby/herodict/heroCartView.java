package pub.tanzby.herodict;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
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

import java.util.ArrayList;

public class heroCartView extends AppCompatActivity {
    ImageButton imgBnt_add;
    RecyclerView mRecycleView;
    RCAdapter     mAdapter;
    Integer current_item_id;
    
    SeekBar seekbar;
    TextView cur_pos_tv;
    int is_moving_to = 0;
    boolean is_moving = false;

    ArrayList<Hero> heroList;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_cart_view);

        init();

        heroList = Hero.getDataFromXMLSource(this,R.xml.test_hero);

        seekbar.setMax(heroList.size()-1);

        even_binding();


    }
    public void init()
    {
        /*set RecyclerView*/
        seekbar = (SeekBar) findViewById(R.id.sk_for_card_view);
        cur_pos_tv  =(TextView) findViewById(R.id.tv_cur_pos);
        
        mRecycleView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecycleView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        imgBnt_add = (ImageButton) findViewById(R.id.imgbnt_add_new_hero);

        new LinearSnapHelper().attachToRecyclerView(mRecycleView);
    }





    public void even_binding()
    {
        mRecycleView.smoothScrollToPosition(2);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = 
                    (LinearLayoutManager) mRecycleView.getLayoutManager();
                current_item_id = layoutManager.findFirstVisibleItemPosition()+1;
                cur_pos_tv.setText(current_item_id+"");
                if(!is_moving) seekbar.setProgress(current_item_id);
            }
        });
        mAdapter = new RCAdapter(this, heroList);
        mAdapter.setOnItemClickLitener(
                new RCAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(heroCartView.this,"短按",Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(heroCartView.this,heroDetailActivity.class);
                        startActivity(newIntent);
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        Toast.makeText(heroCartView.this,"长按",Toast.LENGTH_SHORT).show();

                        // TODO  删操作
                    }
                }
        );
        mRecycleView.setAdapter(mAdapter);

        imgBnt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(heroCartView.this,heroEditActivity.class);
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
