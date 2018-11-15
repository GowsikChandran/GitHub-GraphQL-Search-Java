package com.asana.git.graphql.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.apollo.apollographql.api.SearchRepoQuery;
import com.apollographql.apollo.api.Response;

import java.util.List;

import javax.inject.Inject;

/**
 * SearchPresenter implements the SearchPresenterContract to pass the
 * query and after cursor to the repository
 * And also implements the GitGraphQlRepositoryCallback to handle the
 * success and failure response from the GitHubGraphQlRepository and
 * update the view through the view contract
 *
 * @author Gowsik K C
 * @version 1.0 ,10/28/2018
 * @since 1.0
 */
public class SearchPresenter implements SearchPresenterContract, GitHubGraphQlRepository.GitGraphQlRepositoryCallback {

    public static final String TAG = SearchPresenter.class.getSimpleName();

    private final SearchViewContract viewContract;
    private final GitHubGraphQlRepository repository;

    @Inject
    public SearchPresenter(@NonNull final SearchViewContract viewContract,
                           @NonNull final GitHubGraphQlRepository repository) {
        this.viewContract = viewContract;
        this.repository = repository;
    }


    @Override
    public void searchGitHubRepos(@Nullable String searchQuery, @Nullable String afterCursor) {

        if (searchQuery != null && searchQuery.length() > 0) {

            viewContract.setProgressBarVisibility(View.VISIBLE);
            repository.searchQuery(searchQuery, afterCursor, this);

        } else {
            viewContract.displayError("Please Enter a keyword");
        }
    }



    @Override
    public void handleGitHubResponse(Response<SearchRepoQuery.Data> response) {

        viewContract.setProgressBarVisibility(View.GONE);

        if (!response.hasErrors()) {

            List<SearchRepoQuery.Edge> repoList = response.data().search().edges();
            SearchRepoQuery.PageInfo pageInfo = response.data().search().pageInfo();

            viewContract.displaySearchResults(repoList, pageInfo);

        } else {
            viewContract.displayError();
        }



    }


    @Override
    public void handleGitHubError() {

        viewContract.setProgressBarVisibility(View.GONE);

        viewContract.displayError();
    }


}
