package org.schabi.newpipe;

import android.content.Context;
import android.support.multidex.Multidex;

import com.facebook.stetho.Stetho;

public class DebugApp extends App{
    private static final String TAG= DebugApp.class.toString();

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        Multidex.install(this);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        initStetho();
    }

    private void initStetho(){
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()));
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }
}