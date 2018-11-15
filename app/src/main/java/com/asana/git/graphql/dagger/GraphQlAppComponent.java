package com.asana.git.graphql.dagger;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * This is the Application level Component
 *
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */
@PerActivity
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApolloModule.class,
        ActivityBuilder.class}
)
public interface GraphQlAppComponent extends AndroidInjector<DaggerApplication> {

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        GraphQlAppComponent build();
    }


}

