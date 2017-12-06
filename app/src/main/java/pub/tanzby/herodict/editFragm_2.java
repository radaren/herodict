package pub.tanzby.herodict;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by tan on 2017/11/23.
 */

public class editFragm_2 extends Fragment {
    protected View frameView = null;
    private TextView ed_gender;
    private TextView ed_zhuxiao;
    private TextView ed_lifetime_start,ed_lifetime_end;
    private Context  context;
    private  String[] zhuxiaoList = {"蜀","吴","汉","魏","刘表","刘璋","起义军","西晋",
    "东汉","董卓","袁绍","袁术","在野"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (frameView==null)
        {
            frameView = inflater.inflate(R.layout.activity_edit_fragm_2, container, false);
            ed_gender = frameView.findViewById(R.id.ed_gender);
            ed_gender.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"font/content.ttf"));
            ed_zhuxiao= frameView.findViewById(R.id.ed_zhuxiao);
            ed_zhuxiao.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"font/content.ttf"));
            ed_lifetime_end=frameView.findViewById(R.id.ed_lifetime_end);
            ed_lifetime_end.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"font/content.ttf"));
            ed_lifetime_start=frameView.findViewById(R.id.ed_lifetime_start);
            ed_lifetime_start.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"font/content.ttf"));

            context = frameView.getRootView().getContext();

            ed_zhuxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context,R.style.AlertDialog)
                    .setTitle("选择效力阵营")
                    .setItems(zhuxiaoList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ed_zhuxiao.setText(zhuxiaoList[which]);
                        }
                    }).show();
                }
            });

            ed_gender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context,R.style.AlertDialog)
                    .setTitle("选择性别")
                    .setItems(new String[]{"男","女"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ed_gender.setText(which==1?"女":"男");
                        }
                    }).show();
                }
            });
        }
        return  frameView;
    }
    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle.getString("adj").equals("true"))
        {
            ed_gender.setText(bundle.getString("gender"));
            ed_zhuxiao.setText(bundle.getString("zhuxiao"));
            ed_lifetime_start.setText(bundle.getString("sheng"));
            ed_lifetime_end.setText(bundle.getString("si"));
        }
    }
}
