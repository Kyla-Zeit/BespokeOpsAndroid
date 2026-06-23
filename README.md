# Bespoke Ops Prototype

A prototype for an internal real estate automation app.

- One intake hub for agent requests, approvals, marketing work, and listing launch tasks.
- AI drafting tools with reusable prompts for listing copy, emails, summaries, and offer notes.
- Document workflow tracking for listing agreements, MLS sheets, FINTRAC, offer packages, and PDF generation.
- Automation monitoring for n8n, Make, Zapier, Playwright jobs, Gmail, Sheets, Drive, Canva, and CRM syncs.
- Clear status, ownership, and handoff notes so admin staff can trust the system.
- Later: real authentication, audit logs, webhook retry queues, a database, and permission roles.

## What Is Built

The prototype includes:

- Branded command dashboard using Bespoke's deep navy identity and refined real-estate tone.
- Fixed bottom navigation for Command, Intake, AI Studio, Listings, and Automation.
- Agent request intake form with webhook-ready fields and system routing.
- AI-style draft generator for listing emails, offer briefs, evaluations, and social captions.
- Listing launch room with document control, presentation packet action, and market modules.
- Automation health monitor for CRM, Canva, Gmail, Sheets, Drive, Docs, and Playwright-style tasks.
- Incident feed and executive brief sections that make the app presentable to business stakeholders.
- Safe-area handling for modern Android phones so the status bar and system navigation buttons do not cover the app controls.
- Interactive detail sheets for requests, AI drafts, offer summaries, listing stages, workflow modules, automation jobs, and incidents.
- Demo-safe sample data with no private client records, secrets, API keys, or credentials.
- Internal-data posture improvements including disabled Android backup and explicit non-debuggable release configuration.
- Installable demo build type using release behavior with demo signing for presentation devices.

## Brand Direction Used

- Primary colour: `#1C3E54`, taken from the public Bespoke logo SVG.
- Supporting palette: ivory, white, mist grey, soft sage, brass, and restrained status colours.
- Typography direction: the public site loads Poiret One and Lato. The prototype uses Android-native equivalents: a thin display face for branded headers and a clean sans-serif for operations text, avoiding live font-provider dependencies during demos.

## Next Real Integrations

- Replace the local draft generator with an OpenAI or Claude API call.
- Send request form submissions to an n8n or Make webhook.
- Store requests and checklist state in Supabase, Firebase, Airtable, or PostgreSQL.
- Add Google Workspace OAuth for Gmail, Sheets, and Drive.
- Add Canva template IDs and export links for marketing assets.
