# ALIEN MODS v1.0

Key nei, login nei, server nei. Install kore root permission dilei panel khole.

## APK bananor dhap (phone thekei hobe)
1. zip extract koro
2. github.com e notun repo banao (Private dileo cholbe)
3. zip er shob file upload koro - ".github" folder tao, nahole Actions cholbe na
4. Actions tab -> Build APK -> Run workflow
5. 3-4 minute por Artifacts -> ALIEN-MODS-apk download -> unzip -> app-debug.apk install

## Android Studio diye
Open -> Build > Build Bundle(s)/APK(s) > Build APK(s). File: app/build/outputs/apk/debug/

## Branding
- Nam: AndroidManifest.xml er android:label
- Package: app/build.gradle er namespace + applicationId (duitoi ek sathe), MainActivity.java er prothom line, folder path
- Color: panel.html er CSS e #a3ff3c ar #7c3aed bodlale theme bodlabe

## Note
- minSdk 21, targetSdk 34, debug APK - sorasori install hobe
- Shudhu rooted device. Magisk hole Free Fire ke DenyList e rakho
- Toggle gulo config + root command (kill, cache clear, prop set, FPS/GPU flag)
- applicationId alada, tai onno panel er sathe conflict korbe na
