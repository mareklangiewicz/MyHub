package pl.mareklangiewicz.mygithub.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import pl.mareklangiewicz.mygithub.R;
import pl.mareklangiewicz.mygithub.data.Repo;


public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    private @Nullable List<Repo> mRepos;
    private Callback mCallback;

    @Inject
    public ReposAdapter() { }

    public ReposAdapter(@Nullable List<Repo> repos) {
        this.mRepos = repos;
    }

    public void setRepos(@Nullable List<Repo> repos) {
        this.mRepos = repos;
        notifyDataSetChanged();
    }

    public @Nullable List<Repo> getRepos() { return mRepos; }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mg_item_repo, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mContentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null) {
                    mCallback.onItemClick(holder.repo);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //noinspection ConstantConditions
        Repo repo = mRepos.get(position);
        Context context = holder.mNameTextView.getContext();
        holder.repo = repo;
        holder.mNameTextView.setText(repo.name);
        holder.mDescriptionTextView.setText(repo.description);
        holder.mWatchersTextView.setText(context.getResources().getString(R.string.mg_watchers, repo.watchers));
        holder.mStarsTextView.setText(context.getResources().getString(R.string.mg_stars, repo.stars));
        holder.mForksTextView.setText(context.getResources().getString(R.string.mg_forks, repo.forks));
    }

    @Override
    public int getItemCount() {
        return mRepos == null ? 0 : mRepos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mContentTextView;
        public TextView mNameTextView;
        public TextView mDescriptionTextView;
        public TextView mWatchersTextView;
        public TextView mStarsTextView;
        public TextView mForksTextView;
        public Repo repo;

        public ViewHolder(View itemView) {
            super(itemView);
            mContentTextView = itemView.findViewById(R.id.content);
            mNameTextView = (TextView) itemView.findViewById(R.id.name);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.description);
            mWatchersTextView = (TextView) itemView.findViewById(R.id.watchers);
            mStarsTextView = (TextView) itemView.findViewById(R.id.stars);
            mForksTextView = (TextView) itemView.findViewById(R.id.forks);
        }
    }

    public interface Callback {
        void onItemClick(Repo repo);
    }
}
