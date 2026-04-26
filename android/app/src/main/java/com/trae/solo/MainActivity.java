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
                    String js = "if(!window._traeFabInjected){" +
                             "window._traeFabInjected=true;" +
                             "const c=document.createElement('div');" +
                             "c.id='t-fab';" +
                             "const s=document.createElement('style');" +
                             "s.innerHTML=`#t-fab{position:fixed;right:0px;bottom:80px;z-index:999999;display:flex;flex-direction:column-reverse;align-items:center;gap:12px;transition:left 0.3s ease-out,top 0.3s ease-out,transform 0.3s,opacity 0.3s;}#t-fab.dragging{transition:none;}#t-fab.half.snap-left{transform:translateX(-28px);opacity:0.5}#t-fab.half.snap-right{transform:translateX(28px);opacity:0.5}.t-btn{border-radius:50%;display:flex;justify-content:center;align-items:center;cursor:pointer;box-shadow:0 4px 12px rgba(0,0,0,0.3);transition:all .3s cubic-bezier(.25,.8,.25,1);border:none;color:#fff;outline:none;-webkit-tap-highlight-color:transparent}.t-main{width:56px;height:56px;background:rgba(92,97,255,.85);backdrop-filter:blur(4px)}.t-sub{width:48px;height:48px;opacity:0;transform:translateY(20px) scale(.8);pointer-events:none}#t-fab.exp .t-sub{opacity:1;transform:translateY(0) scale(1);pointer-events:auto}.t-back{background:#00C853}.t-ref{background:#2979FF}.t-main svg{width:28px;height:28px;transition:transform .3s}#t-fab.exp .t-main svg{transform:rotate(45deg)}`;" +
                             "document.head.appendChild(s);" +
                             "c.innerHTML=`<div class='t-btn t-main' id='t-main'><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 34 24' fill='none'><g fill='currentColor'><path d='M.002 0H0v19.549h4.454V4.454h24.864v15.092H4.454V24h29.318V0z'></path><path d='m13.43 8.776-3.149 3.15 3.15 3.149 3.149-3.15zM23.204 8.775l-3.15 3.149 3.15 3.149 3.15-3.15z'></path></g></svg></div><div class='t-btn t-sub t-back' id='t-back'><svg viewBox='0 0 24 24' width='24' height='24' stroke='currentColor' stroke-width='2' fill='none' stroke-linecap='round' stroke-linejoin='round'><line x1='19' y1='12' x2='5' y2='12'></line><polyline points='12 19 5 12 12 5'></polyline></svg></div><div class='t-btn t-sub t-ref' id='t-ref'><svg viewBox='0 0 24 24' width='24' height='24' stroke='currentColor' stroke-width='2' fill='none' stroke-linecap='round' stroke-linejoin='round'><polyline points='23 4 23 10 17 10'></polyline><polyline points='1 20 1 14 7 14'></polyline><path d='M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15'></path></svg></div><div class='t-btn t-sub t-auto' id='t-auto-toggle' style='background:#5C61FF;'><svg id='t-icon-play' viewBox='0 0 24 24' width='24' height='24' fill='currentColor'><path d='M8 5v14l11-7z'/></svg><svg id='t-icon-stop' viewBox='0 0 24 24' width='24' height='24' fill='#ff4444' style='display:none;'><path d='M6 6h12v12H6z'/></svg></div>`;" +
                             "document.body.appendChild(c);" +
                             "let m=document.getElementById('t-main');" +
                             "let sx,sy,ix,iy,moved=false,ht;" +
                             "const K='traeFabPosV1';" +
                             "const load=()=>{try{return JSON.parse(localStorage.getItem(K)||'null')}catch(e){return null}};" +
                             "const save=()=>{try{localStorage.setItem(K,JSON.stringify({side:c.classList.contains('snap-left')?'L':'R',top:parseFloat(c.style.top)||0}))}catch(e){}};" +
                             "const resT=()=>{" +
                             "  clearTimeout(ht);" +
                             "  if(!c.classList.contains('exp')) ht=setTimeout(()=>c.classList.add('half'),2000);" +
                             "};" +
                             "const snap=()=>{" +
                             "  let r=c.getBoundingClientRect();let cx=r.left+r.width/2;" +
                             "  let isL=cx<window.innerWidth/2;" +
                             "  c.style.left=isL?'0px':(window.innerWidth-56)+'px';" +
                             "  c.classList.remove('snap-left','snap-right');" +
                             "  c.classList.add(isL?'snap-left':'snap-right');" +
                             "  resT();" +
                             "  save();" +
                             "};" +
                             "m.addEventListener('touchstart',e=>{" +
                             "  c.classList.remove('half');" +
                             "  let t=e.touches[0];sx=t.clientX;sy=t.clientY;" +
                             "  let r=c.getBoundingClientRect();" +
                             "  ix=parseFloat(c.style.left); if(isNaN(ix)) ix=window.innerWidth-56;" +
                             "  iy=parseFloat(c.style.top); if(isNaN(iy)) iy=r.top;" +
                             "  moved=false;c.classList.add('dragging');" +
                             "  c.style.right='auto';c.style.bottom='auto';" +
                             "  c.style.left=ix+'px';c.style.top=iy+'px';" +
                             "  clearTimeout(ht);" +
                             "},{passive:false});" +
                             "m.addEventListener('touchmove',e=>{" +
                             "  let t=e.touches[0];let dx=t.clientX-sx;let dy=t.clientY-sy;" +
                             "  if(Math.abs(dx)>5||Math.abs(dy)>5){" +
                             "    moved=true;e.preventDefault();" +
                             "    let nl=ix+dx,nt=iy+dy;" +
                             "    let ml=window.innerWidth-56,mt=window.innerHeight-56;" +
                             "    c.style.left=Math.max(0,Math.min(nl,ml))+'px';" +
                             "    c.style.top=Math.max(0,Math.min(nt,mt))+'px';" +
                             "  }" +
                             "},{passive:false});" +
                             "m.addEventListener('touchend',e=>{" +
                             "  c.classList.remove('dragging');" +
                             "  if(!moved){" +
                             "    c.classList.toggle('exp');" +
                             "    if(c.classList.contains('exp')) clearTimeout(ht);" +
                             "    else resT();" +
                             "  }else{ snap(); c.classList.remove('exp'); }" +
                             "});" +
                             "document.getElementById('t-back').onclick=()=>{window.history.back();c.classList.remove('exp');resT();};\n" +
                             "document.getElementById('t-ref').onclick=()=>{window.location.reload();c.classList.remove('exp');resT();};\n" +
                             "window._traeAutoContinueEnabled = false;\n" +
                             "document.getElementById('t-auto-toggle').onclick=(e)=>{\n" +
                             "  e.stopPropagation();\n" +
                             "  window._traeAutoContinueEnabled = !window._traeAutoContinueEnabled;\n" +
                             "  const p=document.getElementById('t-icon-play');\n" +
                             "  const s=document.createElement('t-icon-stop');\n" +
                             "  if(window._traeAutoContinueEnabled){\n" +
                             "    document.getElementById('t-icon-play').style.display='none';\n" +
                             "    document.getElementById('t-icon-stop').style.display='block';\n" +
                             "    document.getElementById('t-auto-toggle').style.background='#ffebee';\n" +
                             "    console.log('[SOLO监控] ▶️ 自动继续监控已开启');\n" +
                             "    if(typeof window._traeCheckAndAutoContinue === 'function') window._traeCheckAndAutoContinue();\n" +
                             "  }else{\n" +
                             "    document.getElementById('t-icon-play').style.display='block';\n" +
                             "    document.getElementById('t-icon-stop').style.display='none';\n" +
                             "    document.getElementById('t-auto-toggle').style.background='#5C61FF';\n" +
                             "    console.log('[SOLO监控] ⏹️ 自动继续监控已停止');\n" +
                             "  }\n" +
                             "  c.classList.remove('exp');resT();\n" +
                             "};\n" +
                             "document.addEventListener('click',e=>{if(!c.contains(e.target)){c.classList.remove('exp');resT();}});" +
                             "window.addEventListener('resize',()=>{let t=parseFloat(c.style.top);if(isNaN(t))t=c.getBoundingClientRect().top;c.style.top=Math.max(0,Math.min(t,window.innerHeight-56))+'px';snap();});" +
                             "c.style.right='auto';c.style.bottom='auto';" +
                             "const st=load();" +
                             "if(st&&typeof st.top==='number'){" +
                             "  c.style.top=Math.max(0,Math.min(st.top,window.innerHeight-56))+'px';" +
                             "  c.style.left=(st.side==='L')?'0px':(window.innerWidth-56)+'px';" +
                             "  c.classList.remove('snap-left','snap-right');" +
                             "  c.classList.add((st.side==='L')?'snap-left':'snap-right');" +
                             "  resT();" +
                             "}else{" +
                             "  c.style.top=Math.max(0,Math.min(window.innerHeight-56-80,window.innerHeight-56))+'px';" +
                             "  c.style.left=(window.innerWidth-56)+'px';" +
                             "  c.classList.remove('snap-left','snap-right');" +
                             "  c.classList.add('snap-right');" +
                             "  resT();" +
                             "}" +
                             "}\n" +
                             "if(!window._traeAutoContinueInjected) {\n" +
                            "  window._traeAutoContinueInjected=true;\n" +
                            "  console.log('[SOLO监控] 🚀 监控脚本已成功注入，正在后台静默监听DOM变化与定时巡检...');\n" +
                            "  window._traeCheckAndAutoContinue=()=>{\n" +
                            "    if(!window._traeAutoContinueEnabled) return;\n" +
                            "    const agentMessages=document.querySelectorAll('.turn__agent-message, [class*=\"agent-message\"]');\n" +
                            "    if(agentMessages.length===0) return;\n" +
                            "    const lastMsg=agentMessages[agentMessages.length-1];\n" +
                            "    if(lastMsg.dataset.autoContinued==='true') return;\n" +
                            "    const text=lastMsg.textContent||'';\n" +
                            "    if(text.includes('检测到模型循环，请求已被中断') || text.includes('abnormally stopped') || text.includes('进入循环') || text.includes('停止了当前对话')){\n" +
                            "      lastMsg.dataset.autoContinued='true';" +
                            "      console.log('[SOLO监控] ⚠️ 捕捉到模型循环中断信号！准备执行自动恢复...');\n" +
                            "      setTimeout(()=>{\n" +
                            "        console.log('[SOLO监控] 🔍 正在寻找富文本输入框...');\n" +
                            "        const inputElement=document.querySelector('.chat-input-v2-input-box-editable[contenteditable=\"true\"]');\n" +
                            "        if(inputElement){\n" +
                            "          console.log('[SOLO监控] ✅ 找到输入框，正在模拟获取焦点和人类输入...');\n" +
                            "          inputElement.focus();\n" +
                            "          document.execCommand('insertText', false, '继续');\n" +
                            "          console.log('[SOLO监控] ⌨️ 已成功通过底层指令输入“继续”');\n" +
                            "          setTimeout(()=>{\n" +
                            "            console.log('[SOLO监控] 🔍 正在寻找发送按钮...');\n" +
                            "            const sendBtn=document.querySelector('.chat-input-v2-send-button');\n" +
                            "            if(sendBtn){ \n" +
                            "              console.log('[SOLO监控] ✅ 找到发送按钮，正在移除禁用状态并触发点击...');\n" +
                            "              sendBtn.removeAttribute('disabled'); \n" +
                            "              sendBtn.click(); \n" +
                            "              console.log('[SOLO监控] 🎉 自动发送完毕，对话已成功恢复！');\n" +
                            "            } else {\n" +
                            "              console.error('[SOLO监控] ❌ 未找到发送按钮，流程异常终止！');\n" +
                            "            }\n" +
                            "          }, 300);\n" +
                            "        } else {\n" +
                            "          console.error('[SOLO监控] ❌ 未找到富文本输入框，流程异常终止！');\n" +
                            "        }\n" +
                            "      }, 1000);\n" +
                            "    }\n" +
                            "  };\n" +
                            "  const observer=new MutationObserver(window._traeCheckAndAutoContinue);\n" +
                            "  observer.observe(document.body,{childList:true,subtree:true,characterData:true});\n" +
                            "  setInterval(window._traeCheckAndAutoContinue, 3000);\n" +
                            "}\n" +
                             "if(!window._vConsoleInjected) {\n" +
                             "  window._vConsoleInjected=true;\n" +
                             "  const script=document.createElement('script');\n" +
                             "  script.src='https://unpkg.com/vconsole@latest/dist/vconsole.min.js';\n" +
                             "  script.onload=function(){\n" +
                             "    new window.VConsole();\n" +
                             "    console.log('[SOLO] vConsole initialized');\n" +
                             "  };\n" +
                             "  document.head.appendChild(script);\n" +
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
