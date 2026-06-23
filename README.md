# Bespoke Ops Prototype

A prototype for an internal real estate operations platform.

# What A Company Like This Needs
A prototype for an internal real estate automation app.

- One intake hub for agent requests, approvals, marketing work, and listing launch tasks.
- AI drafting tools with reusable prompts for listing copy, emails, summaries, and offer notes.
@@ -34,28 +32,6 @@ The prototype includes:
- Supporting palette: ivory, white, mist grey, soft sage, brass, and restrained status colours.
- Typography direction: the public site loads Poiret One and Lato. The prototype uses Android-native equivalents: a thin display face for branded headers and a clean sans-serif for operations text, avoiding live font-provider dependencies during demos.

## What It Shows

- Agent request intake
- Listing launch workflows
- AI draft review
- Offer summary review
- Marketing queue coordination
- Compliance and file control reminders
- CRM, Gmail, Sheets, Canva, and automation handoff concepts
- Internal visibility for staff and managers

## Open The Android App

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

## Demo Build

From the `android` folder, build the installable demo APK with:

```powershell
.\gradlew.bat :app:assembleDemo
```

The presentation APK is created at:

```text
android/app/build/outputs/apk/demo/BespokeOps-demo-release.apk
```

The `demo` build behaves like a release build with `debuggable=false`, uses fictional records, and is signed for easy installation on presentation devices.

## Notes

- Demo records are fictional.
- No private client data, API keys, tokens, or credentials are included.
- Android backup is disabled for internal-data posture.
- Production integrations would connect OpenAI or Claude, CRM, Gmail, Google Sheets, Google Drive, Canva, and automation platforms such as n8n, Make, Zapier, or Playwright.
