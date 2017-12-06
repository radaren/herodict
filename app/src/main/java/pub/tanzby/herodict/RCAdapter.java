package pub.tanzby.herodict;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tanzb on 2017/10/20 0020.
 */

public class RCAdapter extends RecyclerView.Adapter<RCAdapter.VH> {

    private Context mContext;
    private ArrayList<Hero> itemList;

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new VH(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_hero_cart_view, parent,false));
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.tv_name.setText(itemList.get(position).getName());
        Bitmap selectedImage = null;
        try {
            selectedImage = tool_for_project.readBitmapFromLocal(mContext,itemList.get(position).getPic() + ".jpg");
        } catch (IOException e) {
            if (itemList.get(position).getSex().equals("男")) {
                holder.im_img.setImageResource(R.drawable.default_male);
            } else {
                holder.im_img.setImageResource(R.drawable.default_female);
            }
        }
        if(selectedImage == null)
        {
            if (itemList.get(position).getSex().equals("男")) {
                holder.im_img.setImageResource(R.drawable.default_male);
            } else {
                holder.im_img.setImageResource(R.drawable.default_female);
            }
        }else
            holder.im_img.setImageBitmap(selectedImage);
        // TODO: operations for object in holder

         if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }});

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }});
        }
    }
    @Override
    public int getItemCount() {
        return itemList == null?0:itemList.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        // TODO: including an object

        TextView tv_name;
        ImageView im_img;

        VH(View itemView) {
            super(itemView);
            tv_name=(TextView) itemView.findViewById(R.id.tv_name);
            tv_name.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(),"font/mini_fan_zhuanshu.ttf"));
            im_img =(ImageView)itemView.findViewById(R.id.im_gravatar);
        }
    }

    RCAdapter(Context context, ArrayList<Hero> itemList_) {
        itemList = itemList_;
        mContext = context;
    }

    /*创建Item的点击接口*/
    interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    public void removeData(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }
    public void addData(Hero hero)
    {
        itemList.add(hero);
        notifyItemInserted(itemList.size());
    }
    public void updateData(int position, Hero hero)
    {
        itemList.set(position,hero);
        notifyItemChanged(position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}