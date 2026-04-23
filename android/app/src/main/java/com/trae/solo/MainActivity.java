package com.trae.solo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable jsInjector = new Runnable() {
        @Override
        public void run() {
            if (bridge != null) {
                WebView webView = bridge.getWebView();
                if (webView != null) {
                    String js = "if(!window._traeNotifierInjected){" +
                            "window._traeNotifierInjected=true;" +
                            "if(window.Capacitor && window.Capacitor.Plugins.LocalNotifications){" +
                            "  window.Capacitor.Plugins.LocalNotifications.requestPermissions();" +
                            "}" +
                            "let isGen=false;" +
                            "let timer;" +
                            "new MutationObserver(()=>{ " +
                            "  clearTimeout(timer);" +
                            "  timer = setTimeout(()=>{ " +
                            "    let t=document.body.textContent||'';" +
                            "    let gen=t.includes('停止生成')||t.includes('Stop generating');" +
                            "    let err=t.includes('发生错误')||t.includes('重新生成')||t.includes('生成失败');" +
                            "    if(gen && !isGen) isGen=true;" +
                            "    else if(!gen && isGen){ " +
                            "      isGen=false;" +
                            "      if(document.visibilityState==='hidden'){" +
                            "        let msg=err?'❌ 生成过程中发生错误，请返回查看':'✅ SOLO 已经回复完毕啦';" +
                            "        if(window.Capacitor && window.Capacitor.Plugins.LocalNotifications){" +
                            "          window.Capacitor.Plugins.LocalNotifications.schedule({" +
                            "            notifications:[{title:'TRAE SOLO',body:msg,id:Date.now(),schedule:{at:new Date(Date.now()+100)}}]" +
                            "          });" +
                            "        }" +
                            "      }" +
                            "    }" +
                            "  }, 500);" +
                            "}).observe(document.body,{childList:true,subtree:true,characterData:true});" +
                            "}";
                    webView.evaluateJavascript(js, null);
                }
            }
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        
        View webView = this.bridge.getWebView();
        if (webView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(webView, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                if (mlp != null) {
                    mlp.topMargin = insets.top;
                    mlp.bottomMargin = insets.bottom;
                    mlp.leftMargin = insets.left;
                    mlp.rightMargin = insets.right;
                    v.setLayoutParams(mlp);
                }
                return windowInsets;
            });
        }
        
        // Start injecting JS observer periodically (survives page reloads)
        handler.postDelayed(jsInjector, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Prevent WebView from suspending JS execution and network requests
        // when the app goes to the background.
        if (bridge != null && bridge.getWebView() != null) {
            bridge.getWebView().resumeTimers();
            bridge.getWebView().onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(jsInjector);
    }
}
