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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class heroCartView extends AppCompatActivity {
    ImageButton imgBnt_add;
    RecyclerView mRecycleView;
    RCAdapter     mAdapter;
    Integer current_item_id;

    ArrayList<Hero> heroList;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_cart_view);

        element_binding();

        heroList = Hero.getDataFromXMLSource(this,R.xml.test_hero);

        even_binding();


    }
    public void element_binding()
    {
        /*set RecyclerView*/
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
                /*update current item id*/
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecycleView.getLayoutManager();
                View firstVisibleItem = mRecycleView.getChildAt(0);
                int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
                int itemWidth = firstVisibleItem.getWidth();
                int firstItemBottom = layoutManager.getDecoratedRight(firstVisibleItem);
                current_item_id = tool_for_project.px2dip(heroCartView.this, (firstItemPosition+1)*itemWidth-firstItemBottom);
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
