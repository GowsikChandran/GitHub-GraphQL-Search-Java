package com.asana.git.graphql.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apollo.apollographql.api.SearchRepoQuery;
import com.asana.git.graphql.databinding.RecViewLoadingItemBinding;
import com.asana.git.graphql.databinding.RecViewSearchItemBinding;
import com.asana.git.graphql.util.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The SearchRepoAdapter is an adapter class for the search repository recycler view
 * in the Search Activity
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */

public class SearchRepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants for repository item and loading item
    private final int VIEW_TYPE_REPO_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context mContext;
    private List<SearchRepoQuery.Edge> repositoryList = new ArrayList<>();


    public SearchRepoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        //return appropriate view holder
        if (viewType == VIEW_TYPE_REPO_ITEM) {

            RecViewSearchItemBinding binding = RecViewSearchItemBinding.inflate(
                    layoutInflater, viewGroup, false);

            return new SearchViewHolder(binding);

        } else {

            RecViewLoadingItemBinding binding = RecViewLoadingItemBinding.inflate(
                    layoutInflater, viewGroup, false);

            return new LoadingViewHolder(binding);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof SearchViewHolder) {

            final SearchViewHolder searchViewHolder = (SearchViewHolder) viewHolder;
            SearchRepoQuery.Edge edge = repositoryList.get(position);

            //Glide loads the image and manges the Cache
            GlideApp.with(mContext)
                    .load(edge.node().asRepository().owner().avatarUrl())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(searchViewHolder.binding.avatarImageView);

            searchViewHolder.binding.repoNameTv.setText(edge.node().asRepository().name());
            searchViewHolder.binding.repoDescriptionTv.setText(edge.node().asRepository().description());
            searchViewHolder.binding.forksTv.setText(String.valueOf(edge.node().asRepository().forkCount()));
        } else {

            final LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
            loadingViewHolder.binding.rvRepoListPb.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    /**
     * Decides the view type of an item
     *
     * @param position int value position of item
     * @return int view type
     */
    @Override
    public int getItemViewType(int position) {
        return repositoryList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_REPO_ITEM;
    }

    /**
     * adds the given repository list to the existing List
     *
     * @param repositoryList list of git hub repository items
     *
     */
    public void addRepos(List<SearchRepoQuery.Edge> repositoryList) {
        this.repositoryList.addAll(getItemCount(), repositoryList);
        notifyDataSetChanged();
    }

    /**
     * Adds a null value to the end of the list
     * when the item is null the view type changer to
     * loading View to indicate the user that next set of
     * items is being loaded
     */
    public void showLoading() {
        repositoryList.add(null);
        notifyItemInserted(repositoryList.size() - 1);
    }

    /**
     * Removes the last item - a null value in the end of the list
     * when the next set of data has been loaded
     */
    public void hideLoading() {
        repositoryList.remove(repositoryList.size() - 1);
        notifyItemRemoved(repositoryList.size());
    }

    /**
     * clears the the repository list
     */
    public void clear() {
        int size = getItemCount();
        repositoryList.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * ViewHolder class to show a Repository item
     * in the Recycler view
     *
     * @author Gowsik K C
     * @version 1.0 ,10/27/2018
     * @since 1.0
     */

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        RecViewSearchItemBinding binding;

        public SearchViewHolder(@NonNull RecViewSearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * ViewHolder class to show a progress bar
     * in the Recycler view
     *
     * @author Gowsik K C
     * @version 1.0 ,10/27/2018
     * @since 1.0
     */
    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        RecViewLoadingItemBinding binding;

        public LoadingViewHolder(@NonNull RecViewLoadingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
