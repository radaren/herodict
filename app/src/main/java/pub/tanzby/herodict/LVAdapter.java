package pub.tanzby.herodict;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.time.chrono.HijrahEra;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tan on 2017/11/26.
 */

public class LVAdapter extends BaseAdapter implements Filterable{

    private Context mContext;
    private ArrayList<Hero> itemList;   // 总的数据源
    private List<Hero> data;//显示的数据
    private final Object mLock = new Object();
    private MyFilter mFilter = null;

    public LVAdapter(Context context) {
        itemList = null;
        mContext = context;
        mFilter = new MyFilter();
    }
    public void setSourceList(ArrayList<Hero> newList)
    {
        itemList = newList;
    }
    public void removeData(int position) {
        itemList.remove(position);
        this.notifyDataSetChanged();
    }
    public void addData(Hero product) {
        itemList.add(product);
        this.notifyDataSetChanged();
    }
    public void clearData()
    {
        data.clear();
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return data==null?0:data.size();
    }
    @Override
    public Hero getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder = null;
        if (convertView == null) {
            //使用R.layout.items_view_for_lv构建一个列表项的布局
            convertView = inflater.inflate(R.layout.simple_search_list_view, null);
            holder = new ViewHolder();
            holder.heroName = (TextView) convertView.findViewById(R.id.tv_search_list_item);
            convertView.setTag(holder);
        }    else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mOnItemClickLitener != null)
        {
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(finalConvertView,position);
                }
            });
        }
        holder.heroName.setText(data.get(position).getName());
        return convertView;
    }

    interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private static class ViewHolder
    {
        TextView heroName;
    }

    @Override
    public android.widget.Filter getFilter() {
        return mFilter;
    }

    private class MyFilter extends Filter {

        /**
         * 过滤autoCompleteEditext中的字 改成包含 
         */
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (itemList == null) {
                synchronized (mLock) {
                    itemList = new ArrayList<Hero>(data);
                }
            }
            int count = itemList.size();
            ArrayList<Hero> values = new ArrayList<Hero>();

            String[] constraintList = ((String) constraint).split("\\s+");

            for (int i = 0; i < count; i++) {
                String valueText = itemList.get(i).getName();
                boolean okForAll =true;
                for (String c : constraintList)
                {
                    if (null != valueText && null != c
                            && !valueText.contains(c)) {
                        okForAll = false;
                        break;
                    }
                }
                if (okForAll) values.add(itemList.get(i));
            }
            results.values = values;
            results.count = values.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults results) {

            data = (ArrayList<Hero>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
