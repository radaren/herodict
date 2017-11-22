package pub.tanzby.herodict;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        // TODO: operations for object in holder
        holder.tv_name.setText( itemList.get(position).getName());

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


        VH(View itemView) {
            super(itemView);

            // TODO: instantiating an object

            tv_name= itemView.findViewById(R.id.tv_name);

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

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}