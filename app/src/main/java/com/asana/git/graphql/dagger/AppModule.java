package com.asana.git.graphql.dagger;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Application level module witch provides dagger with the context
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */
@Module
public abstract class AppModule {

    /**
     * Provides Dagger with the context
     *
     * @param application application context
     * @return returns the context
     */
    @Binds
    abstract Context provideContext(Application application);
}
