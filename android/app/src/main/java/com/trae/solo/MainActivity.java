package com.trae.solo;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final int AUDIO_PERMISSION_REQUEST_CODE = 41001;

    private String getAppVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    private void ensureAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
        }
    }

    private Runnable jsInjector = new Runnable() {
        @Override
        public void run() {
            if (bridge != null) {
                WebView webView = bridge.getWebView();
                if (webView != null) {
                    String js = "window.__SOLO_APP_VERSION__=" + "'" + getAppVersionName() + "'" + ";" +
                             "if(!window._traeFabInjected){" +
                             "window._traeFabInjected=true;" +
                             "const c=document.createElement('div');" +
                             "c.id='t-fab';" +
                             "const s=document.createElement('style');" +
                             "s.innerHTML=`#t-fab{position:fixed;right:0px;bottom:80px;z-index:999999;display:flex;flex-direction:column-reverse;align-items:center;gap:12px;transition:left 0.3s ease-out,top 0.3s ease-out,transform 0.3s,opacity 0.3s;}#t-fab.dragging{transition:none;}#t-fab.half.snap-left{transform:translateX(-28px);opacity:0.5}#t-fab.half.snap-right{transform:translateX(28px);opacity:0.5}.t-btn{border-radius:50%;display:flex;justify-content:center;align-items:center;cursor:pointer;box-shadow:0 4px 12px rgba(0,0,0,0.3);transition:all .3s cubic-bezier(.25,.8,.25,1);border:none;color:#fff;outline:none;-webkit-tap-highlight-color:transparent}.t-main{width:56px;height:56px;background:rgba(92,97,255,.85);backdrop-filter:blur(4px)}.t-sub{width:48px;height:48px;opacity:0;transform:translateY(20px) scale(.8);pointer-events:none}#t-fab.exp .t-sub{opacity:1;transform:translateY(0) scale(1);pointer-events:auto}.t-back{background:#00C853}.t-ref{background:#2979FF}.t-upd{background:#FF9800}.t-vc{background:#607D8B}.t-main svg{width:28px;height:28px;transition:transform .3s}#t-fab.exp .t-main svg{transform:rotate(45deg)}#t-update-modal{position:fixed;inset:0;z-index:999998;display:none;align-items:center;justify-content:center;background:rgba(0,0,0,.45);font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica,Arial,sans-serif}#t-update-modal .t-update-card{width:min(92vw,360px);background:#fff;border-radius:14px;padding:16px 16px 12px 16px;box-shadow:0 12px 30px rgba(0,0,0,.25)}#t-update-modal .t-update-title{font-size:16px;font-weight:700;color:#111}#t-update-modal .t-update-sub{margin-top:6px;font-size:12px;color:#666;line-height:1.5}#t-update-modal .t-update-status{margin-top:10px;font-size:13px;color:#222;white-space:pre-wrap;word-break:break-word}#t-update-modal .t-update-actions{margin-top:14px;display:flex;gap:10px;justify-content:flex-end}#t-update-modal .t-u-btn{border:none;border-radius:10px;padding:10px 12px;font-size:13px;cursor:pointer}#t-update-modal .t-u-btn-primary{background:#5C61FF;color:#fff}#t-update-modal .t-u-btn-ghost{background:#f2f3f5;color:#111}`;" +
                             "s.innerHTML += `#t-fab:not(.exp) .t-sub{display:none !important}`;" +
                             "document.head.appendChild(s);" +
                             "c.innerHTML=`<div class='t-btn t-main' id='t-main'><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 34 24' fill='none'><g fill='currentColor'><path d='M.002 0H0v19.549h4.454V4.454h24.864v15.092H4.454V24h29.318V0z'></path><path d='m13.43 8.776-3.149 3.15 3.15 3.149 3.149-3.15zM23.204 8.775l-3.15 3.149 3.15 3.149 3.15-3.15z'></path></g></svg></div><div class='t-btn t-sub t-back' id='t-back'><svg viewBox='0 0 24 24' width='24' height='24' stroke='currentColor' stroke-width='2' fill='none' stroke-linecap='round' stroke-linejoin='round'><line x1='19' y1='12' x2='5' y2='12'></line><polyline points='12 19 5 12 12 5'></polyline></svg></div><div class='t-btn t-sub t-ref' id='t-ref'><svg viewBox='0 0 24 24' width='24' height='24' stroke='currentColor' stroke-width='2' fill='none' stroke-linecap='round' stroke-linejoin='round'><polyline points='23 4 23 10 17 10'></polyline><polyline points='1 20 1 14 7 14'></polyline><path d='M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15'></path></svg></div><div class='t-btn t-sub t-upd' id='t-upd'><svg viewBox='0 0 24 24' width='24' height='24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M12 3v10'></path><path d='M8 9l4 4 4-4'></path><path d='M4 17h16'></path></svg></div><div class='t-btn t-sub t-vc' id='t-vc-toggle'><svg id='t-icon-vc-on' viewBox='0 0 24 24' width='24' height='24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><polyline points='4 17 10 11 4 5'></polyline><line x1='12' y1='19' x2='20' y2='19'></line></svg><svg id='t-icon-vc-off' viewBox='0 0 24 24' width='24' height='24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='display:none;'><polyline points='4 17 10 11 4 5'></polyline><line x1='12' y1='19' x2='20' y2='19'></line><line x1='4' y1='4' x2='20' y2='20'></line></svg></div><div class='t-btn t-sub t-auto' id='t-auto-toggle' style='background:#5C61FF;'><svg id='t-icon-play' viewBox='0 0 24 24' width='24' height='24' fill='currentColor'><path d='M8 5v14l11-7z'/></svg><svg id='t-icon-stop' viewBox='0 0 24 24' width='24' height='24' fill='#ff4444' style='display:none;'><path d='M6 6h12v12H6z'/></svg></div>`;" +
                             "document.body.appendChild(c);" +
                             "const um=document.createElement('div');" +
                             "um.id='t-update-modal';" +
                             "um.innerHTML=`<div class='t-update-card'><div class='t-update-title'>应用更新</div><div class='t-update-sub'>当前版本：<span id='t-update-current'></span></div><div class='t-update-status' id='t-update-status'>点击“检查更新”以获取最新版本信息。</div><div class='t-update-actions'><button class='t-u-btn t-u-btn-ghost' id='t-update-close'>关闭</button><button class='t-u-btn t-u-btn-ghost' id='t-update-page'>更新页</button><button class='t-u-btn t-u-btn-ghost' id='t-update-check'>检查更新</button><button class='t-u-btn t-u-btn-primary' id='t-update-download' style='display:none;'>下载更新</button></div></div>`;" +
                             "document.body.appendChild(um);" +
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
                             "const showUpdateModal=()=>{document.getElementById('t-update-current').textContent=window.__SOLO_APP_VERSION__||'';um.style.display='flex';};\n" +
                             "const hideUpdateModal=()=>{um.style.display='none';};\n" +
                             "document.getElementById('t-upd').onclick=(e)=>{e.stopPropagation();c.classList.remove('exp');resT();showUpdateModal();};\n" +
                             "document.getElementById('t-update-close').onclick=()=>{hideUpdateModal();};\n" +
                             "um.addEventListener('click',(e)=>{if(e.target===um) hideUpdateModal();});\n" +
                             "const setUpdateStatus=(t)=>{document.getElementById('t-update-status').textContent=t;};\n" +
                             "window._traeLatestDownloadUrl=null;\n" +
                             "window._traeCheckUpdate=async(manual)=>{try{const isCN=(location.host||'').includes('trae.cn');const ghProxy='https://ghproxy.com/';const api='https://api.github.com/repos/ma15803216102/solo-android-app/releases/latest';const accel=(u)=>{if(!u) return u; if(isCN) return ghProxy+u; return u;};const withTimeout=async(url,ms)=>{const c=typeof AbortController!=='undefined'?new AbortController():null;const t=setTimeout(()=>{try{if(c) c.abort();}catch(e){}},ms);try{return await fetch(url,{cache:'no-store',signal:c?c.signal:undefined});}finally{clearTimeout(t);}};if(manual) setUpdateStatus('正在检查更新...');document.getElementById('t-update-download').style.display='none';window._traeLatestDownloadUrl=null;const urls=isCN?[api,ghProxy+api]:[api];let r=null;for(const u of urls){try{const rr=await withTimeout(u,8000);if(rr&&rr.ok){r=rr;break;}}catch(e){}}if(!r){setUpdateStatus('检查更新失败：网络或接口异常\\n可点击“更新页”使用加速链接查看并手动下载。');return;}const j=await r.json();const tag=j.tag_name||'';const assets=Array.isArray(j.assets)?j.assets:[];const key=isCN?'TRAE-SOLO-CN-':'TRAE-SOLO-Global-';const apk=assets.find(a=>a&&typeof a.name==='string'&&a.name.includes(key)&&a.name.endsWith('.apk'));const url=apk?apk.browser_download_url:null;window._traeLatestDownloadUrl=accel(url);const cur=window.__SOLO_APP_VERSION__||'';const curN=parseInt((cur.split('.').pop()||'0'),10);const tagN=parseInt((tag.replace(/^v/,'').split('.').pop()||'0'),10);if(tagN>curN&&window._traeLatestDownloadUrl){setUpdateStatus('发现新版本：'+tag+'\\n点击“下载更新”获取安装包。');document.getElementById('t-update-download').style.display='inline-block';}else{setUpdateStatus('当前已是最新版本。\\n最新版本：'+(tag||'未知'));document.getElementById('t-update-download').style.display='none';}}catch(e){setUpdateStatus('检查更新失败：'+(e&&e.message?e.message:'未知错误')+'\\n可点击“更新页”使用加速链接查看并手动下载。');}};\n" +
                             "document.getElementById('t-update-page').onclick=()=>{const isCN=(location.host||'').includes('trae.cn');const ghProxy='https://ghproxy.com/';const page='https://github.com/ma15803216102/solo-android-app/releases';window.open(isCN?ghProxy+page:page,'_blank');};\n" +
                             "document.getElementById('t-update-check').onclick=()=>{window._traeCheckUpdate(true);};\n" +
                             "document.getElementById('t-update-download').onclick=()=>{if(window._traeLatestDownloadUrl) window.open(window._traeLatestDownloadUrl,'_blank');};\n" +
                             "setTimeout(()=>{window._traeCheckUpdate(false);},1500);\n" +
                             "const VC_KEY='traeVConsoleEnabledV1';\n" +
                             "if(typeof window._traeVConsoleEnabled!=='boolean'){try{window._traeVConsoleEnabled=(localStorage.getItem(VC_KEY)||'0')==='1';}catch(e){window._traeVConsoleEnabled=false;}}\n" +
                             "window._traeApplyVConsole=()=>{try{if(window._traeVConsoleEnabled){if(window.VConsole){if(!window._traeVConsoleInstance){window._traeVConsoleInstance=new window.VConsole();}}else if(!window._vConsoleInjected){window._vConsoleInjected=true;const sc=document.createElement('script');sc.src='https://unpkg.com/vconsole@latest/dist/vconsole.min.js';sc.onload=function(){try{if(window._traeVConsoleEnabled&&!window._traeVConsoleInstance&&window.VConsole){window._traeVConsoleInstance=new window.VConsole();}}catch(e){}};document.head.appendChild(sc);}}else{try{if(window._traeVConsoleInstance&&typeof window._traeVConsoleInstance.destroy==='function'){window._traeVConsoleInstance.destroy();}}catch(e){}window._traeVConsoleInstance=null;try{document.querySelectorAll('#__vconsole,.vc-switch,.vc-mask,.vc-panel,.vc-tabbar').forEach(n=>n.remove());}catch(e){}}}catch(e){}};\n" +
                             "const syncVConsoleBtn=()=>{const on=!!window._traeVConsoleEnabled;document.getElementById('t-icon-vc-on').style.display=on?'block':'none';document.getElementById('t-icon-vc-off').style.display=on?'none':'block';document.getElementById('t-vc-toggle').style.opacity=on?'1':'0.6';};\n" +
                             "syncVConsoleBtn();\n" +
                             "document.getElementById('t-vc-toggle').onclick=(e)=>{e.stopPropagation();window._traeVConsoleEnabled=!window._traeVConsoleEnabled;try{localStorage.setItem(VC_KEY,window._traeVConsoleEnabled?'1':'0');}catch(e){}syncVConsoleBtn();if(typeof window._traeApplyVConsole==='function') window._traeApplyVConsole();c.classList.remove('exp');resT();};\n" +
                             "if(typeof window._traeApplyVConsole==='function') window._traeApplyVConsole();\n" +
                             "window._traeAutoContinueEnabled = false;\n" +
                             "document.getElementById('t-auto-toggle').onclick=(e)=>{\n" +
                             "  e.stopPropagation();\n" +
                             "  window._traeAutoContinueEnabled = !window._traeAutoContinueEnabled;\n" +
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
                            "    let targetNode = null;\n" +
                            "    const statusBars = document.querySelectorAll('.latest-assistant-bar');\n" +
                            "    if(statusBars.length > 0) {\n" +
                            "      const lastStatusBar = statusBars[statusBars.length - 1];\n" +
                            "      const sText = lastStatusBar.textContent||'';\n" +
                            "      if(sText.includes('检测到模型循环') || sText.includes('abnormally stopped') || sText.includes('进入循环') || sText.includes('停止了当前对话') || sText.includes('异常打断')) {\n" +
                            "        targetNode = lastStatusBar;\n" +
                            "      }\n" +
                            "    }\n" +
                            "    if(!targetNode) {\n" +
                            "      const agentMessages=document.querySelectorAll('.turn__agent-message, [class*=\"agent-message\"]');\n" +
                            "      if(agentMessages.length > 0) {\n" +
                            "        const lastMsg=agentMessages[agentMessages.length-1];\n" +
                            "        const text=lastMsg.textContent||'';\n" +
                            "        if(text.includes('检测到模型循环') || text.includes('abnormally stopped') || text.includes('进入循环') || text.includes('停止了当前对话') || text.includes('异常打断')){\n" +
                            "          targetNode = lastMsg;\n" +
                            "        }\n" +
                            "      }\n" +
                            "    }\n" +
                            "    if(!targetNode || targetNode.dataset.autoContinued==='true') return;\n" +
                            "    targetNode.dataset.autoContinued='true';" +
                            "    window._traeLastAutoContinueTime = window._traeLastAutoContinueTime || 0;\n" +
                            "    const now = Date.now();\n" +
                            "    if(now - window._traeLastAutoContinueTime < 60000) {\n" +
                            "      console.log('[SOLO监控] ⏳ 触发1分钟冷却锁，已忽略本次打断信号...');\n" +
                            "      return;\n" +
                            "    }\n" +
                            "    window._traeLastAutoContinueTime = now;\n" +
                            "    console.log('[SOLO监控] ⚠️ 捕捉到最新节点模型异常打断信号！准备执行继续操作...');\n" +
                            "    setTimeout(()=>{\n" +
                            "      console.log('[SOLO监控] 🔍 正在寻找富文本输入框...');\n" +
                            "      const inputElement=document.querySelector('.chat-input-v2-input-box-editable[contenteditable=\"true\"]');\n" +
                            "      if(inputElement){\n" +
                            "        console.log('[SOLO监控] ✅ 找到输入框，正在模拟获取焦点和人类输入...');\n" +
                            "        inputElement.focus();\n" +
                            "        document.execCommand('insertText', false, '继续');\n" +
                            "        console.log('[SOLO监控] ⌨️ 已成功通过底层指令输入“继续”');\n" +
                            "        setTimeout(()=>{\n" +
                            "          console.log('[SOLO监控] 🔍 正在寻找发送按钮...');\n" +
                            "          const sendBtn=document.querySelector('.chat-input-v2-send-button');\n" +
                            "          if(sendBtn){ \n" +
                            "            console.log('[SOLO监控] ✅ 找到发送按钮，正在移除禁用状态并触发点击...');\n" +
                            "            sendBtn.removeAttribute('disabled'); \n" +
                            "            sendBtn.click(); \n" +
                            "            console.log('[SOLO监控] 🎉 继续操作执行完毕！');\n" +
                            "            } else {\n" +
                            "              console.error('[SOLO监控] ❌ 未找到发送按钮，流程异常终止！');\n" +
                            "            }\n" +
                            "          }, 300);\n" +
                            "        } else {\n" +
                            "          console.error('[SOLO监控] ❌ 未找到富文本输入框，流程异常终止！');\n" +
                            "        }\n" +
                            "      }, 1000);\n" +
                            "  };\n" +
                            "  const observer=new MutationObserver(window._traeCheckAndAutoContinue);\n" +
                            "  observer.observe(document.body,{childList:true,subtree:true,characterData:true});\n" +
                            "  setInterval(window._traeCheckAndAutoContinue, 3000);\n" +
                            "}\n" +
                             "if(typeof window._traeApplyVConsole==='function') window._traeApplyVConsole();";
                    webView.evaluateJavascript(js, null);
                }
            }
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        ensureAudioPermission();
        
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
