package com.librarytest;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Random;

public class CalendarModule extends ReactContextBaseJavaModule {
    CalendarModule(ReactApplicationContext context){
        super(context);
    }

    @ReactMethod
    public void createCalendarEvent(String name, String location){
        Log.d("CalendarModule", "Create event called with name: " + name
                + " and location: " + location);
        Toast.makeText(this.getReactApplicationContext(), name, Toast.LENGTH_SHORT).show();
    }

    @ReactMethod
    public void getRandomNumber(Callback callback){
        Random random = new Random();
        Integer randomNumber = random.nextInt(10);
        callback.invoke(randomNumber);
    }

    @NonNull
    @Override
    public String getName() {
        return "CalendarModule";
    }
}
