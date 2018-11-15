package com.asana.git.graphql.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.apollo.apollographql.api.SearchRepoQuery;
import com.asana.git.graphql.R;
import com.asana.git.graphql.databinding.ActivitySearchBinding;
import com.asana.git.graphql.util.InfiniteScrollListener;
import com.asana.git.graphql.util.RecViewItemDecoration;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Search activity implements the SearchViewContract interface
 * to display the results and error messages from the Search presenter
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */

public class SearchActivity extends DaggerAppCompatActivity implements SearchViewContract {

    private static final String TAG = SearchActivity.class.getSimpleName();

    //Dagger will initialise the presenter
    @Inject
    SearchPresenter searchPresenter;

    ActivitySearchBinding binding;
    SearchRepoAdapter mAdapter;
    String afterCursor, query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);


        //set the layout manager of the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.searchRv.setLayoutManager(linearLayoutManager);

        //set the Item decoration for equal spacing around each recycler view item
        RecViewItemDecoration recViewItemDecoration =
                new RecViewItemDecoration(getResources().getDimensionPixelSize(R.dimen.search_rv_item_space));
        binding.searchRv.addItemDecoration(recViewItemDecoration);

        //set the adapter of the recycler view
        mAdapter = new SearchRepoAdapter(this);
        binding.searchRv.setAdapter(mAdapter);


        //set onScrollListener
        binding.searchRv.addOnScrollListener(infiniteScrollListener);

        //set onEditActionListener
        binding.searchEdt.setOnEditorActionListener(editorActionListener);

    }

    /**
     * OnEditorActionListener is triggered when the soft keyboard search
     * button is pressed
     */
    TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                //reset page cursor for new searches
                afterCursor = null;

                //clears the any previous list in adapter
                mAdapter.clear();

                //sets is loading variable to true so that
                // on load more will not be called
                infiniteScrollListener.setIsLoading(true);

                query = binding.searchEdt.getText().toString();

                getRepos(query, afterCursor);

                hideKeyboard(view);

                return true;
            }
            return false;
        }
    };

    /**
     * InfiniteScrollListener onLoad more method is triggered when end of the list
     * is reached
     */
    InfiniteScrollListener infiniteScrollListener = new InfiniteScrollListener() {
        @Override
        public void onLoadMore() {
         /*   Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {*/
            //indicates that the next set of items are loaded
            mAdapter.showLoading();

            getRepos(query, afterCursor);
        /*        }
            };
            handler.post(runnable);*/
        }
    };

    /**
     * getRepos method call the search Github Repos method
     * in the presenter
     *
     * @param query       the search keyword string
     * @param afterCursor the end cursor of the current page
     */
    void getRepos(String query, String afterCursor) {

        searchPresenter.searchGitHubRepos(query, afterCursor);
    }


    @Override
    public void displaySearchResults(@NonNull final List<SearchRepoQuery.Edge> repositoryList, @NonNull final SearchRepoQuery.PageInfo pageInfo) {


        //shows the main progress bar only on first load
        if (afterCursor != null) {
            mAdapter.hideLoading();
        }
        //adds the new set of results to the adapter list
        mAdapter.addRepos(repositoryList);

        //if the Repo result does not have a next page
        //there is no need to call on load more when the
        // end is reached
        if (pageInfo.hasNextPage()) {
            infiniteScrollListener.setIsLoading(false);

        } else {
            infiniteScrollListener.setIsLoading(true);
            displayError("No Items found");
        }
        //sets the latest end cursor
        setAfterCursor(pageInfo.endCursor());


    }

    void setAfterCursor(String afterCursor) {

        this.afterCursor = afterCursor;
    }

    @Override
    public void displayError() {

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_text), Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayError(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProgressBarVisibility(final int visibility) {

        if (afterCursor == null) {
            binding.imageListPb.setVisibility(visibility);
        }

    }


    /**
     * Hides the soft keyboard
     *
     * @param view - the edit text view form the OnEditorActionListener
     */
    public void hideKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
