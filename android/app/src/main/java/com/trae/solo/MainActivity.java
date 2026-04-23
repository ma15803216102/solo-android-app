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
                            "}" +
                            "if(!window._traeFabInjected){" +
                            "window._traeFabInjected=true;" +
                            "const c=document.createElement('div');" +
                            "c.id='t-fab';" +
                            "const s=document.createElement('style');" +
                            "s.innerHTML=`#t-fab{position:fixed;right:20px;bottom:80px;z-index:999999;display:flex;flex-direction:column-reverse;align-items:center;gap:12px}.t-btn{border-radius:50%;display:flex;justify-content:center;align-items:center;cursor:pointer;box-shadow:0 4px 12px rgba(0,0,0,0.3);transition:all .3s cubic-bezier(.25,.8,.25,1);border:none;color:#fff;outline:none;-webkit-tap-highlight-color:transparent}.t-main{width:56px;height:56px;background:rgba(92,97,255,.85);backdrop-filter:blur(4px)}.t-sub{width:48px;height:48px;opacity:0;transform:translateY(20px) scale(.8);pointer-events:none}#t-fab.exp .t-sub{opacity:1;transform:translateY(0) scale(1);pointer-events:auto}.t-back{background:#00C853}.t-ref{background:#2979FF}.t-main svg{width:28px;height:28px;transition:transform .3s}#t-fab.exp .t-main svg{transform:rotate(45deg)}`;" +
                            "document.head.appendChild(s);" +
                            "c.innerHTML=`<div class='t-btn t-main' id='t-main'><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 34 24' fill='none'><g fill='currentColor'><path d='M.002 0H0v19.549h4.454V4.454h24.864v15.092H4.454V24h29.318V0z'></path><path d='m13.43 8.776-3.149 3.15 3.15 3.149 3.149-3.15zM23.204 8.775l-3.15 3.149 3.15 3.149 3.15-3.15z'></path></g></svg></div><div class='t-btn t-sub t-back' id='t-back'><svg viewBox='0 0 24 24' width='24' height='24' stroke='currentColor' stroke-width='2' fill='none' stroke-linecap='round' stroke-linejoin='round'><line x1='19' y1='12' x2='5' y2='12'></line><polyline points='12 19 5 12 12 5'></polyline></svg></div><div class='t-btn t-sub t-ref' id='t-ref'><svg viewBox='0 0 24 24' width='24' height='24' stroke='currentColor' stroke-width='2' fill='none' stroke-linecap='round' stroke-linejoin='round'><polyline points='23 4 23 10 17 10'></polyline><polyline points='1 20 1 14 7 14'></polyline><path d='M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15'></path></svg></div>`;" +
                            "document.body.appendChild(c);" +
                            "document.getElementById('t-main').onclick=()=>c.classList.toggle('exp');" +
                            "document.getElementById('t-back').onclick=()=>{window.history.back();c.classList.remove('exp')};" +
                            "document.getElementById('t-ref').onclick=()=>{window.location.reload();c.classList.remove('exp')};" +
                            "document.addEventListener('click',e=>{if(!c.contains(e.target))c.classList.remove('exp')});" +
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
