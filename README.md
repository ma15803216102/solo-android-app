<div align="center">
  <img src="assets/icon.svg" alt="Trae SOLO Logo" width="120" height="120">
  <h1>TRAE SOLO Android Client</h1>
  <p>🚀 随时随地，在手机上畅享 AI 无限职场 🚀</p>
</div>

---

## 📖 项目简介

为了解决在手机浏览器中操作 **TRAE SOLO (solo.trae.ai)** 网页体验不佳、界面受限等问题，本项目利用 **TRAE SOLO** 自身的能力，**全自动生成了一款轻量级的 Android 移动端原生应用 (App Wrapper)**。

通过这款专属的 App，你可以彻底摆脱手机浏览器的各种限制，随时随地打开手机，获得媲美原生应用般流畅、沉浸的 AI 对话与工具使用体验。

> 💡 **本项目全程由 TRAE SOLO 独立编写与部署，0 本地环境依赖，全云端自动化构建。**

---

## ✨ 核心特性

- **📱 原生级体验**：不再有烦人的浏览器地址栏、底部导航栏干扰，全屏沉浸式体验。
- **🛡️ 完美适配 Android 16**：通过深度定制底层原生 Java 代码，彻底解决了最新安卓系统（Android 15/16）Edge-to-Edge 全面屏特性的状态栏遮挡网页“顽疾”。
- **⚡ 丝滑启动**：配置了专属的启动屏 (Splash Screen) 与过渡动画，完美掩盖网页初始化加载的白屏时间。
- **☁️ 云端构建 (CI/CD)**：利用 GitHub Actions，无需配置复杂的 Android Studio 开发环境，每次代码更新均在云端自动编译出 `.apk` 安装包。
- **🎨 品牌定制**：内置了精心适配的多尺寸 TRAE 专属 Logo 图标与深色模式启动图。

---

## 🛠️ 技术栈

本项目是一个现代化的跨平台 Hybrid 应用：

- **核心框架**：[Capacitor 8](https://capacitorjs.com/) (将 Web 包装为原生 App 的最佳选择)
- **依赖管理**：Node.js 22 + npm
- **Android 原生层**：Java (处理 WindowInsets 安全区域)
- **编译工具**：Gradle 8 + Java 21
- **CI/CD 流水线**：GitHub Actions
- **前端资源加载**：Android WebView (直接加载 `https://solo.trae.ai`)

---

## 📦 如何安装与使用

### 下载安装包
1. 进入本仓库的 [Actions](https://github.com/ma15803216102/solo-android-app/actions) 页面。
2. 点击最新一次带有绿色 ✅ 的成功构建记录。
3. 滑动到底部的 **Artifacts** 区域，下载 `app-debug.apk`。
4. 将文件传输至 Android 手机，允许“安装未知来源应用”即可完成安装。

---

## ⚙️ 如何本地二次开发

由于这是一个标准的 Capacitor 项目，如果你有电脑和本地环境，可以随时对其进行深度定制：

```bash
# 1. 克隆项目到本地
git clone https://github.com/ma15803216102/solo-android-app.git

# 2. 进入目录并安装依赖
cd solo-android-app
npm install

# 3. 如果修改了 capacitor.config.json 或需要更新插件，请同步
npx cap sync android

# 4. 使用 Android Studio 打开并编译运行
npx cap open android
```

---

## 🤝 鸣谢

- 感谢 [TRAE 社区](https://forum.trae.cn/) 提供的 SOLO 挑战赛平台。
- 本项目是在没有电脑的极端条件下，完全通过移动端与 **TRAE SOLO AI** 对话协作诞生的产物。
