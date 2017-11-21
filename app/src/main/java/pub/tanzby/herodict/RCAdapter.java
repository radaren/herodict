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
                .inflate(R.layout.items_view_for_rc, parent,false));
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {

        // TODO: operations for object in holder
        holder.tv_name.setText( itemList.get(position).name);
        holder.tv_hometown.setText(itemList.get(position).hometown+"人");
        holder.tv_birthday.setText(itemList.get(position).birthDay+"生");
        holder.tv_belong.setText(itemList.get(position).belong);
        holder.tv_brief.setText( itemList.get(position).brieftext);
        holder.tv_gender.setText(itemList.get(position).gender);

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
        TextView tv_hometown;
        TextView tv_birthday;
        TextView tv_belong;
        TextView tv_brief;
        TextView tv_gender;


        VH(View itemView) {
            super(itemView);

            // TODO: instantiating an object

            tv_name=(TextView) itemView.findViewById(R.id.tv_name);
            tv_hometown=(TextView) itemView.findViewById(R.id.tv_hometown);
            tv_birthday=(TextView) itemView.findViewById(R.id.tv_birthday);
            tv_belong=(TextView) itemView.findViewById(R.id.tv_belong);
            tv_brief=(TextView) itemView.findViewById(R.id.tv_brief);
            tv_gender=(TextView) itemView.findViewById(R.id.tv_gender);

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