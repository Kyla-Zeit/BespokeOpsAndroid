# Bespoke Ops Prototype

A working Android prototype for an internal real estate operations platform.

Present it as:

> A working internal operations prototype showing how a brokerage could centralize staff workflows, AI-assisted drafting, listing launches, offer reviews, compliance reminders, and automation visibility.

## Download The Demo APK
 
 Install the demo APK directly on an Android phone or tablet:

[Download BespokeOps-demo-release.apk](https://github.com/Kyla-Zeit/BespokeOpsAndroid/raw/main/BespokeOps-demo-release.apk)

Open the downloaded APK from **Files** or **Downloads**. If prompted, allow installs from that source, then install and open **Bespoke Ops**.

## What It Shows

- Agent request intake
- Listing launch workflows
- AI draft review
- Offer summary review
- Marketing queue coordination
- Compliance and file control reminders
- CRM, Gmail, Sheets, Canva, and automation handoff concepts
- Internal visibility for staff and managers

## Design Direction

- Brand-inspired palette: ivory, white, mist grey, soft sage, brass, and restrained status colours.
- Typography direction: the public Bespoke site loads Poiret One and Lato. The prototype uses Android-native equivalents to avoid live font-provider dependencies during demos.

## Open In Android Studio

Open Android Studio.

Choose **File > Open**.

Select the `android` folder in this repo.

Let Gradle sync.

Run the app configuration on an Android emulator or Android device.

You can also build it from the terminal:

```powershell
cd android
.\gradlew.bat :app:assembleDebug
```

## Rebuild The Demo APK

If you want to rebuild the installable demo APK yourself, run this from the `android` folder:

```powershell
.\gradlew.bat :app:assembleDemo
```

The rebuilt presentation APK is created at:

```text
android/app/build/outputs/apk/demo/BespokeOps-demo-release.apk
```

The `demo` build behaves like a release build with `debuggable=false`, uses fictional records, and is signed for easy installation on presentation devices.

## Notes

- Demo records are fictional.
- No private client data, API keys, tokens, or credentials are included.
- Android backup is disabled for internal-data posture.
- Production integrations would connect OpenAI or Claude, CRM, Gmail, Google Sheets, Google Drive, Canva, and automation platforms such as n8n, Make, Zapier, or Playwright.
