package org.schabi.newpipe;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.schabi.newpipe.database.AppDatabase;
import org.schabi.newpipe.database.history.dao.HistoryDAO;
import org.schabi.newpipe.database.history.dao.SearchHistoryDAO;
import org.schabi.newpipe.database.history.dao.WatchHistoryDAO;
import org.schabi.newpipe.database.history.model.HistoryEntry;
import org.schabi.newpipe.database.history.model.SearchHistoryEntry;
import org.schabi.newpipe.database.history.model.WatchHistoryEntry;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.VideoStream;
import org.schabi.newpipe.fragments.BackPressable;
import org.schabi.newpipe.fragments.detail .VideoDetailFragment;
import org.schabi.newpipe.fragments.list.search.SearchFragment;
import org.schabi.newpipe.history.HistoryListener;
import org.schabi.newpipe.util.Constants;
import org.schabi.newpipe.util.NavigationHelper;
import org.schabi.newpipe.util.StateSaver;
import org.schabi.newpipe.util.ThemeHelper;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements HistoryListener{
    private static final String TAG="MainActivity";
    public static final boolean DEBUG=false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (DEBUG) Log.d(TAG, "onCreate() called with. savedInstanceState = [" + savedInstanceState + "]");
        ThemeHelper.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupprtFragmentManager() != null && getSupprtFragmentManager().getBackStackEntryCount() == 0){
            initFragments();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isChangingConfigurations()){
            StateSaver.clearStateFiles();
        }
        disposeHistory();
    }
    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean(Constants.KEY_THEME_CHANGE, false)){
            if (DEBUG) Log.d(TAG, "O tema foi trocado, recriando atividade...");
            sharedPreferences.edit().putBoolean(Constants.KEY_THEME_CHANGE, false).apply();
            new Handler(Looper.getMainLooper()).post(new Runnable(){
                @Override
                public void run(){
                    MainActivity.this.recreate();
                }
            });
        }

        if(sharedPreferences.getBoolean(Constants.KEY_MAIN_PAGE_CHANGE, false)){
            if (DEBUG) Log.d(TAG, "página principal mudou, recriando fragmento principal...");
            sharedPreferences.edit().putBoolean(Constants.KEY_MAIN_PAGE_CHANGE, false).apply();
            NavigationHelper.openMainActivity(this);
        }
    }

    
}