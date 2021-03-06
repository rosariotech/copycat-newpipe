package org.schabi.newpipe;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;
import org.acra.sender.ReportSenderFactory;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.report.AcraReportSenderFactory;
import org.schabi.newpipe.report.ErrorActivity;
import org.schabi.newpipe.report.UserAction;
import org.schabi.newpipe.settings.SettingsActivity;
import org.schabi.newpipe.util.ExtractorHelper;
import org.schabi.newpipe.util.StateSaver;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;

import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class App extends Application{
    protected static final String TAG = App.class.toString();
    @SuppressWarnings("unchecked")
    private static final Class<? extends ReportSenderFactory>[] reportSenderFactoryClasses = new Class[]{AcraReportSenderFactory.class};
    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        initACRA();
    }
    @Override
    public void onCreate(){
        super.onCreate();
        SettingsActivity.initSettings(this);
        NewPipe.init(Downloader.getInstance());
        NewPipeDatabase.init(this);
        StateSaver.init(this);
        initNotificationChannel();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        configureRxJavaErrorHandler();
    }
    private void configureRxJavaErrorHandler(){
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            Log.e(TAG, "RxJavaPlugins.ErrorHandler called with -> : throwable = [" + throwable.getClass().getName() + "]");
            if (throwable instanceof CompositeException){
                for (Throwable element : ((CompositeException) throwable).getExceptions()){
                    if (checkThrowable(element)) return;
                }
            }
            if (checkThrowable(throwable)) return;

            Thread.currentThread().getUncaughtExceptionHandler()
                .uncaughtException(Thread.currentThread(),throwable);
        
        private boolean checkThrowable(@NonNull Throwable throwable){
            return ExtractorHelper.hasAssignableCauseThrowable(throwable,
                IOException.class, SocketException.class, InterruptedException.class);
        }
    }
});


private void initACRA(){
    try {
        final ACRAConfiguration acraConfig = new ConfigurationBuilder(this)
            .setReportSenderFactoryClasses(reportSenderFactoryClasses)
            .setBuildConfigClass(BuildConfig.class)
            .build();
        ACRA.init(this, acraConfig);
    } catch (ACRAConfiguration ace){
        ace.printStackTrace();
        ErrorActivity.reportError(this, ace, null, null, ErrorActivity.ErrorInfo.make(UserAction.SOMETHING_ELSE, "none", "Could not initialize ACRA crash report", R.string.app_ui_crash));
    }
}

public void initNotificationChannel(){
    if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O){
        return;
    }
    final String id=getString(R.string.notification_channel_id);
    final CharSequence name= getString(R.string.notification_channel_name);
    final String description = getString(R.string.notification_channel_description);
    final int importance = NotificationManager.IMPORTANCE_LOW;

    NotificationChannel mChannel = new NotificationChannel(id, name, importance);
    mChannel.setDescription(description);

    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.createNotificationChannel(mChannel);
}
}