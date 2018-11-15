package com.asana.git.graphql;

import android.content.Context;

import com.asana.git.graphql.dagger.DaggerGraphQlAppComponent;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * GraphQlApp  Application Class
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */
public class GraphQlApp extends DaggerApplication {


    /**
     * Indicates the main App Component and passes the context
     */
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerGraphQlAppComponent.builder().application(this).build();
    }


    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {

        GraphQlApp graphQlApp = (GraphQlApp) context.getApplicationContext();
        return graphQlApp.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        refWatcher = LeakCanary.install(this);
    }
}
