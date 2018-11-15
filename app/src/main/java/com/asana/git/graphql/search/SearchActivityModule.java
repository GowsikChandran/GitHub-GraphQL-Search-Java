package com.asana.git.graphql.search;

import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloClient;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
/**
 * This is the Activity level Module for SearchActivity
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */
@Module
public abstract class SearchActivityModule {

    /**
     * This method provides the Apps Main Thread
     *
     * @return handler
     */
    @Provides
    static Handler provideMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    /**
     * This method constructs and provides the GitHubGraphQlRepository object
     *
     * @param apolloClient the apollo client to make requests
     * @param handler the handler to update the Ui
     */
    @Provides
    static GitHubGraphQlRepository provideRepository(ApolloClient apolloClient, Handler handler) {
        return new GitHubGraphQlRepository(apolloClient, handler);
    }


    /**
     * This method constructs and provides the SearchPresenter object
     *
     * @param view the apollo client to make requests
     * @param repository the handler to update the Ui
     */
    @Provides
    static SearchPresenter provideSearchPresenter(SearchViewContract view, GitHubGraphQlRepository repository) {
        return new SearchPresenter(view, repository);
    }

    /**
     * This method returns the SearchViewContract
     *
     * @param searchActivity the search activity implements the Search view contract
     */
    @Binds
    abstract SearchViewContract provideSearchView(SearchActivity searchActivity);


}
