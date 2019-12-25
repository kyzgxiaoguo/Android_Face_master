package com.hg.orcdiscern.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hg.orcdiscern.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Zhangzhenguo
 * @create 2019/8/23
 * @Email 18311371235@163.com
 * @Describe
 */
public class PopupWindowAdapter extends RecyclerView.Adapter<PopupWindowAdapter.ViewHolder> {


    private List<String> mValues;
    private OnItemClickListener onItemClickListener;

    public PopupWindowAdapter(List<String> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popupwindow_item_style, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tvPopupItem.setText(mValues.get(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(mValues.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String itemValue);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPopupItem;

        public ViewHolder(View view) {
            super(view);
            tvPopupItem=view.findViewById(R.id.tv_Popup_Item);
        }
    }
}
