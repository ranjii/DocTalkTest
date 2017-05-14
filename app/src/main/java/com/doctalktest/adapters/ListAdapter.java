package com.doctalktest.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doctalktest.models.IssuesModel;
import com.doctalktest.R;
import com.doctalktest.interfaces.RecyclersOnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 29/4/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {


    private List<IssuesModel> mIssueList = new ArrayList<IssuesModel>();

    private final Activity mActivity;
    RecyclersOnItemClickListener mRecyclersOnItemClickListener;

    public ListAdapter(Activity mActivity, RecyclersOnItemClickListener listener, List<IssuesModel> mIssueList) {
        this.mActivity = mActivity;
        this.mIssueList = mIssueList;
        this.mRecyclersOnItemClickListener = listener;

    }

    @Override
    public ListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.CustomViewHolder holder, int position) {
        holder.name.setText(mIssueList.get(position).user.login);
        holder.commit.setText( mIssueList.get(position).title);
        Picasso.with(holder.image.getContext().getApplicationContext()).load( mIssueList.get(position).user.avatar_url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).fit().into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mIssueList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView name, commit;
        ImageView image;
        CardView cardView;
        LinearLayout cardLayout;
        public CustomViewHolder(View view) {
            super(view);

            cardLayout = (LinearLayout)view.findViewById(R.id.cardLayout);
            name = (TextView) view.findViewById(R.id.tvName);
            commit = (TextView) view.findViewById(R.id.tvCommit);
            image = (ImageView) view.findViewById(R.id.commitImage);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            mRecyclersOnItemClickListener.onItemClick(getPosition(), v);

        }

    }
}
