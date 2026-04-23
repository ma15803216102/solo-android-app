package com.trae.solo;

import android.util.Log;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TraeLogger")
public class TraeLoggerPlugin extends Plugin {
    @PluginMethod
    public void log(PluginCall call) {
        String message = call.getString("message", "");
        Log.i("TRAE_SOLO", message);
        call.resolve();
    }
}

