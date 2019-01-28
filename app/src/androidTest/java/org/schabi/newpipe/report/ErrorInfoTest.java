package org.schabi.newpipe.report;

import android.os.Parcel;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.schabi.newpipe.R;
import org.schabi.newpipe.report.ErrorActivity.ErrorInfo;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ErrorInfoTest{
    @Test
    public void errorInfo_testParcelable(){
        ErrorInfo info = ErrorInfo.make(UserAction.USER_REPORT, "youtube", "request", R.string.general_error);
        
    }
}