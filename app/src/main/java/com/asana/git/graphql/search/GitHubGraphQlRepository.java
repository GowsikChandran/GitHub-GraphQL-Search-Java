package com.asana.git.graphql.search;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.apollo.apollographql.api.SearchRepoQuery;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.fetcher.ApolloResponseFetchers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * GitHubGraphQlRepository class is where the actual call to the
 * Git Hub GraphQL is made
 *
 * @author Gowsik K C
 * @version 1.0 ,10/30/2018
 * @since 1.0
 */
public class GitHubGraphQlRepository {

    public static final String TAG = GitHubGraphQlRepository.class.getSimpleName();


    private final ApolloClient apolloClient;
    private final Handler handler;

    @Inject
    public GitHubGraphQlRepository(ApolloClient apolloClient, Handler handler) {
        this.apolloClient = apolloClient;
        this.handler = handler;
    }

    public void searchQuery(@NonNull final String query, @Nullable String afterCursor,
                            @NonNull final GitGraphQlRepositoryCallback callback) {

        //number of repositories per request
        int pageLimit = 20;

        //builds the search query 
        SearchRepoQuery searchRepoQuery = SearchRepoQuery.builder()
                .query(query)
                .limit(pageLimit)
                .afterCursor(afterCursor)
                .build();

        ApolloCall<SearchRepoQuery.Data> searchApolloCall =
                apolloClient.query(searchRepoQuery).responseFetcher(ApolloResponseFetchers.NETWORK_ONLY);


        searchApolloCall.enqueue(new ApolloCallback<>(new ApolloCall.Callback<SearchRepoQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<SearchRepoQuery.Data> response) {

                callback.handleGitHubResponse(response);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

                callback.handleGitHubError();
            }
        }, handler));


    }

    /**
     * Callback Interface for the presenter the handle the success and failure
     *
     * @author Gowsik K C
     * @version 1.0 ,10/28/2018
     * @since 1.0
     */
    public interface GitGraphQlRepositoryCallback {

        /**
         * Handles the success response from the Apollo call
         *
         * @param response the response from the apollo call
         */
        void handleGitHubResponse(Response<SearchRepoQuery.Data> response);


        /**
         * Handles the Failure response from the Apollo call
         */
        void handleGitHubError();

    }


}
