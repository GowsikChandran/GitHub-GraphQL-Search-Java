package com.asana.git.graphql.dagger;

import com.asana.git.graphql.search.SearchActivity;
import com.asana.git.graphql.search.SearchActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Provides Dagger with the activities that will inject
 * their respective modules
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = SearchActivityModule.class)
    abstract SearchActivity bindSearchActivity();


}
