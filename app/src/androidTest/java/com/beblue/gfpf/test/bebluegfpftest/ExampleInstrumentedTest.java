package com.beblue.gfpf.test.bebluegfpftest;

import android.content.Context;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        //Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //assertEquals("com.beblue.gfpf.test.bebluegfpftest", appContext.getPackageName());

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Base package name expected without any suffix
        String basePackageName = "com.beblue.gfpf.test.bebluegfpftest";

        // Actual package name of the app under test
        String actualPackageName = appContext.getPackageName();

        // Log both package names
        Log.d(TAG, "Expected package name: " + basePackageName);
        Log.d(TAG, "Current package name:  " + actualPackageName);

        // Check if the actual package name starts with the base package name
        assertTrue(actualPackageName.startsWith(basePackageName));
    }
}
