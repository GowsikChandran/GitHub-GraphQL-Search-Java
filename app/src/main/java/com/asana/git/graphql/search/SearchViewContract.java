package com.asana.git.graphql.search;

import android.support.annotation.NonNull;

import com.apollo.apollographql.api.SearchRepoQuery;


import java.util.List;

/**
 * SearchViewContract interface for activity
 *
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */
public interface SearchViewContract {

    /**
     * Display the results
     * @param repositoryList a list of git hub repositories
     * @param pageInfo contains the after cursor and is last page variable
     */
    void displaySearchResults(@NonNull List<SearchRepoQuery.Edge> repositoryList,
                              @NonNull SearchRepoQuery.PageInfo pageInfo);

    /**
     * Displays a standard error message
     */
    void displayError();

    /**
     * Displays a custom error message
     * @param errorMessage custom string error message
     */
    void displayError(String errorMessage);

    /**
     * Sets the visibility if the main progress bar
     * @param visibility the visibility values are View.VISIBLE or View.GONE
     */
    void setProgressBarVisibility(int visibility);


}
