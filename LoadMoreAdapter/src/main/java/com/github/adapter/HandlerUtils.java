package com.github.adapter;

import android.os.Handler;
import android.os.Looper;

class HandlerUtils {
    /**********************************************************/
    private static Handler handler;
    private HandlerUtils() {
    }
    public static Handler get(){
        if(handler==null){
            synchronized (HandlerUtils.class){
                if(handler==null){
                    handler=new Handler(Looper.getMainLooper());
                }
            }
        }
        return handler;
    }
    /**********************************************************/

}
