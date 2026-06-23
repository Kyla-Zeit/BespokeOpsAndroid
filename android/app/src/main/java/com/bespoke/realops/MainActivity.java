package com.bespoke.realops;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {
    private static final int NAVY = Color.rgb(28, 62, 84);
    private static final int NAVY_DARK = Color.rgb(13, 33, 47);
    private static final int NAVY_SOFT = Color.rgb(51, 91, 118);
    private static final int IVORY = Color.rgb(248, 245, 239);
    private static final int SURFACE = Color.WHITE;
    private static final int PAPER = Color.rgb(252, 250, 246);
    private static final int MIST = Color.rgb(237, 240, 241);
    private static final int SAGE = Color.rgb(220, 227, 218);
    private static final int BORDER = Color.rgb(216, 222, 225);
    private static final int HAIRLINE = Color.rgb(229, 232, 233);
    private static final int INK = Color.rgb(23, 30, 36);
    private static final int MUTED = Color.rgb(98, 108, 116);
    private static final int BRASS = Color.rgb(181, 144, 82);
    private static final int SUCCESS = Color.rgb(48, 126, 89);
    private static final int WARNING = Color.rgb(179, 128, 54);
    private static final int DANGER = Color.rgb(166, 71, 61);
    private static final int BLUE = Color.rgb(65, 152, 211);

    private static final String FONT_BODY = "sans-serif";
    private static final String FONT_MEDIUM = "sans-serif-medium";
    private static final String FONT_DISPLAY = "sans-serif-light";

    private final ArrayList<WorkflowRequest> requests = new ArrayList<>();
    private final ArrayList<AutomationJob> jobs = new ArrayList<>();
    private final ArrayList<Incident> incidents = new ArrayList<>();
    private final Random random = new Random();

    private final String[] navLabels = {"Command", "Intake", "AI Studio", "Listings", "Automation"};
    private final String[] navIcons = {"home", "plus", "spark", "folder", "pulse"};
    private final String[] checklistNames = {
            "Listing agreement",
            "MLS data sheet",
            "FINTRAC ID check",
            "Seller disclosure",
            "Photography approved",
            "Feature sheet drafted",
            "Offer package",
            "Commission worksheet"
    };
    private final boolean[] checklistState = {true, true, false, false, true, false, false, true};

    private LinearLayout content;
    private LinearLayout bottomNav;
    private LinearLayout appBar;
    private TextView appBarTitle;
    private TextView appBarSubtitle;
    private int currentScreen = 0;
    private String lastDraft = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seedData();

        Window window = getWindow();
        window.setStatusBarColor(NAVY_DARK);
        window.setNavigationBarColor(SURFACE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        buildShell();
    }

    private void seedData() {
        if (!requests.isEmpty()) {
            return;
        }

        requests.add(new WorkflowRequest("Marketing", "Avery Chen", "24 Lakeshore Road", "High", "Canva, Instagram, Email", "Prepare launch carousel, story frames, and email teaser.", "Today 3:00 PM", "In review"));
        requests.add(new WorkflowRequest("Offer", "Maya Singh", "88 Bloor Street W", "Urgent", "Docs, Gmail", "Summarize terms and flag conditions before client call.", "Today 4:00 PM", "Queued"));
        requests.add(new WorkflowRequest("Listing", "Noah Patel", "17 Cedar Lane", "Normal", "CRM, Sheets", "Create seller timeline and launch checklist.", "Tomorrow 10:00 AM", "Working"));
        requests.add(new WorkflowRequest("Evaluation", "Sofia Romano", "8300 Woodbine Ave", "Normal", "Sheets, CRM", "Build CMA packet from comparable sales notes.", "Tomorrow 1:30 PM", "Queued"));

        jobs.add(new AutomationJob("CRM Pipeline Sync", "Website forms to agent pipeline", 96, "Healthy", "CRM", SUCCESS));
        jobs.add(new AutomationJob("Canva Production Queue", "Marketing request to template draft", 84, "Watching", "Canva", WARNING));
        jobs.add(new AutomationJob("Gmail Deal Summaries", "Inbox threads to client-ready briefs", 91, "Healthy", "Gmail", SUCCESS));
        jobs.add(new AutomationJob("Google Sheets Ledger", "Listings, tasks, and approval states", 88, "Healthy", "Sheets", SUCCESS));
        jobs.add(new AutomationJob("Playwright Portal Tasks", "MLS/admin form checks", 73, "Needs review", "Browser", DANGER));

        incidents.add(new Incident("Canva export delayed", "2 template renders waiting for approval", "12 min ago", WARNING));
        incidents.add(new Incident("Offer brief verified", "AI summary checked by Maya Singh", "32 min ago", SUCCESS));
        incidents.add(new Incident("Portal login challenge", "Playwright job paused for manual review", "1 hr ago", DANGER));
    }

    private void buildShell() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(IVORY);

        appBar = buildAppBar();
        root.addView(appBar, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(16), dp(18), dp(28));
        scroll.addView(content, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        root.addView(scroll, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
        ));

        bottomNav = new LinearLayout(this);
        bottomNav.setOrientation(LinearLayout.HORIZONTAL);
        bottomNav.setGravity(Gravity.CENTER);
        bottomNav.setPadding(dp(10), dp(7), dp(10), dp(8));
        bottomNav.setBackgroundColor(SURFACE);
        bottomNav.setElevation(dp(12));
        root.addView(bottomNav, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setContentView(root);
        installSystemInsetPadding(root);
        renderNav();
        renderScreen();
    }

    private LinearLayout buildAppBar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(16), dp(9), dp(16), dp(10));
        bar.setBackgroundColor(NAVY);

        BrandMarkView mark = new BrandMarkView(this);
        LinearLayout.LayoutParams markParams = new LinearLayout.LayoutParams(dp(38), dp(38));
        markParams.rightMargin = dp(12);
        bar.addView(mark, markParams);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        bar.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        appBarTitle = text("Bespoke Ops", 17, Color.WHITE, Typeface.BOLD, FONT_MEDIUM);
        appBarSubtitle = text("Real estate operations suite", 11, Color.rgb(216, 228, 232), Typeface.NORMAL, FONT_BODY);
        copy.addView(appBarTitle);
        copy.addView(appBarSubtitle);

        LinearLayout right = new LinearLayout(this);
        right.setOrientation(LinearLayout.VERTICAL);
        right.setGravity(Gravity.RIGHT);
        right.addView(pill("Internal Ops", Color.argb(42, 255, 255, 255), Color.WHITE));
        TextView office = text("Markham HQ", 10, Color.rgb(216, 228, 232), Typeface.NORMAL, FONT_BODY);
        office.setGravity(Gravity.RIGHT);
        right.addView(office, topMargin(1));
        bar.addView(right);
        return bar;
    }

    private void installSystemInsetPadding(View root) {
        root.setOnApplyWindowInsetsListener((view, insets) -> {
            int topInset;
            int bottomInset;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                topInset = insets.getInsets(WindowInsets.Type.statusBars()).top;
                bottomInset = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
            } else {
                topInset = insets.getSystemWindowInsetTop();
                bottomInset = insets.getSystemWindowInsetBottom();
            }

            appBar.setPadding(dp(16), dp(9) + topInset, dp(16), dp(10));
            bottomNav.setPadding(dp(10), dp(7), dp(10), dp(8) + Math.max(bottomInset, dp(6)));
            return insets;
        });
        root.requestApplyInsets();
    }

    private void renderNav() {
        bottomNav.removeAllViews();
        for (int i = 0; i < navLabels.length; i++) {
            final int index = i;
            boolean selected = currentScreen == index;
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.setPadding(dp(3), dp(4), dp(3), dp(3));
            item.setBackground(rounded(selected ? Color.rgb(238, 243, 245) : SURFACE, dp(8)));
            item.setOnClickListener(v -> {
                currentScreen = index;
                renderNav();
                renderScreen();
            });

            LineIconView icon = new LineIconView(this, navIcons[i], selected ? NAVY : Color.rgb(127, 136, 142));
            item.addView(icon, new LinearLayout.LayoutParams(dp(23), dp(23)));

            TextView label = text(navLabels[i], 9, selected ? NAVY : Color.rgb(132, 140, 146), Typeface.BOLD, FONT_BODY);
            label.setGravity(Gravity.CENTER);
            item.addView(label);

            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(0, dp(50), 1f);
            if (i < navLabels.length - 1) {
                itemParams.rightMargin = dp(3);
            }
            bottomNav.addView(item, itemParams);
        }
    }

    private void renderScreen() {
        content.removeAllViews();
        appBarTitle.setText("Bespoke Ops");
        appBarSubtitle.setText(navLabels[currentScreen] + " workspace");

        if (currentScreen == 0) {
            renderCommand();
        } else if (currentScreen == 1) {
            renderIntake();
        } else if (currentScreen == 2) {
            renderAiStudio();
        } else if (currentScreen == 3) {
            renderListings();
        } else {
            renderAutomation();
        }
    }

    private String screenSubtitle() {
        if (currentScreen == 1) {
            return "Route agent work into one reliable queue";
        }
        if (currentScreen == 2) {
            return "Draft listing, offer, and client communications";
        }
        if (currentScreen == 3) {
            return "Launch listings with document and marketing control";
        }
        if (currentScreen == 4) {
            return "Monitor integrations, retries, and handoffs";
        }
        return "A customized approach to real estate operations";
    }

    private void renderCommand() {
        content.addView(heroPanel());
        content.addView(demoModeNotice(), fullWidthMargins(0, dp(10), 0, dp(12)));

        LinearLayout metrics = new LinearLayout(this);
        metrics.setOrientation(LinearLayout.HORIZONTAL);
        content.addView(metrics, fullWidthMargins(0, dp(14), 0, dp(14)));
        metrics.addView(metricTile("Open Work", String.valueOf(requests.size()), "agent tasks", NAVY));
        metrics.addView(metricTile("Launch Ready", completedDocs() + "/" + checklistNames.length, "file items", SUCCESS));
        metrics.addView(metricTile("Automation", averageHealth() + "%", "reliability", WARNING));

        content.addView(commandBoard(), bottomMargin(14));

        content.addView(sectionLabel("Priority Desk", "Items requiring action before the next client touchpoint"));
        for (int i = 0; i < Math.min(3, requests.size()); i++) {
            content.addView(requestCard(requests.get(i), true), bottomMargin(10));
        }

        content.addView(sectionLabel("Executive Brief", "Why this becomes valuable inside the brokerage"));
        LinearLayout brief = panel();
        brief.addView(briefRow("One command center", "Requests, listings, documents, AI drafts, and automation health stay visible in one app.", "home", NAVY));
        brief.addView(briefRow("Human-approved AI", "Drafts accelerate admin work while approvals preserve brand voice and compliance.", "spark", BRASS));
        brief.addView(briefRow("Brokerage-grade audit trail", "Every task keeps owner, due time, status, source system, and next action.", "pulse", SUCCESS));
        content.addView(brief);
    }

    private View heroPanel() {
        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(16), dp(15), dp(16), dp(15));
        hero.setBackground(gradient(NAVY_DARK, NAVY));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        hero.addView(top);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        top.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView eyebrow = text("BROKERAGE OPERATIONS", 10, Color.rgb(199, 214, 221), Typeface.BOLD, FONT_BODY);
        copy.addView(eyebrow);
        copy.addView(text("Brokerage Command Centre", 23, Color.WHITE, Typeface.BOLD, FONT_DISPLAY), topMargin(1));
        copy.addView(text("Centralizes staff requests, listing launches, offer reviews, file control, and automation visibility.", 13, Color.rgb(220, 230, 235), Typeface.NORMAL, FONT_BODY), topMargin(3));

        TextView live = pill("Live", Color.argb(42, 255, 255, 255), Color.WHITE);
        top.addView(live);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        hero.addView(actions, topMargin(14));
        actions.addView(compactAction("Intake", "intake", "plus"));
        actions.addView(compactAction("Draft", "ai", "spark"));
        actions.addView(compactAction("Monitor", "automation", "pulse"));

        return hero;
    }

    private View demoModeNotice() {
        LinearLayout notice = new LinearLayout(this);
        notice.setOrientation(LinearLayout.HORIZONTAL);
        notice.setGravity(Gravity.CENTER_VERTICAL);
        notice.setPadding(dp(12), dp(10), dp(12), dp(10));
        notice.setBackground(outlined(Color.rgb(243, 246, 247), dp(7), HAIRLINE));

        notice.addView(iconWrap(new LineIconView(this, "check", NAVY), Color.rgb(232, 239, 242)));

        TextView copy = text("Demo mode uses fictional brokerage records to show how Bespoke Ops would centralize requests, approvals, file control, and automation visibility.", 12, INK, Typeface.NORMAL, FONT_BODY);
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        copyParams.leftMargin = dp(10);
        notice.addView(copy, copyParams);
        return notice;
    }

    private View compactAction(String title, String target, String iconName) {
        LinearLayout action = new LinearLayout(this);
        action.setOrientation(LinearLayout.HORIZONTAL);
        action.setGravity(Gravity.CENTER);
        action.setPadding(dp(9), dp(8), dp(9), dp(8));
        action.setBackground(rounded(Color.argb(33, 255, 255, 255), dp(8)));
        action.setOnClickListener(v -> {
            if ("intake".equals(target)) {
                currentScreen = 1;
            } else if ("ai".equals(target)) {
                currentScreen = 2;
            } else if ("listing".equals(target)) {
                currentScreen = 3;
            } else {
                currentScreen = 4;
            }
            renderNav();
            renderScreen();
        });

        action.addView(new LineIconView(this, iconName, Color.WHITE), new LinearLayout.LayoutParams(dp(18), dp(18)));
        TextView titleView = text(title, 11, Color.WHITE, Typeface.BOLD, FONT_BODY);
        titleView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        labelParams.leftMargin = dp(7);
        action.addView(titleView, labelParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.rightMargin = dp(8);
        action.setLayoutParams(params);
        return action;
    }

    private View commandBoard() {
        LinearLayout board = panel();
        board.addView(fieldHeader("Today's Operating Plan", "Fictional demo records showing intended brokerage workflows"));
        board.addView(boardRow("Listing Launches", "3 active", "24 Lakeshore needs disclosure and seller approval.", "folder", NAVY));
        board.addView(boardRow("AI Drafts Pending Review", "2 drafts", "Listing email and offer recap ready for compliance review.", "spark", BRASS));
        board.addView(boardRow("Offer Summaries", "1 urgent", "88 Bloor terms summary due before client call.", "doc", DANGER));
        board.addView(boardRow("Marketing Queue", "5 assets", "Canva exports, social captions, and agent email package.", "plus", BLUE));
        board.addView(boardRow("Compliance / File Control", completedDocs() + "/" + checklistNames.length, "FINTRAC and seller disclosure still require attention.", "check", WARNING));
        board.addView(boardRow("Automation Health", averageHealth() + "%", "One browser task paused for manual review.", "pulse", SUCCESS));
        return board;
    }

    private View boardRow(String title, String status, String detail, String iconName, int accent) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(8), 0, dp(8));
        row.setOnClickListener(v -> showDetailSheet(
                title,
                "Operations detail",
                status,
                accent,
                new String[]{
                        "Current status|" + status,
                        "Owner|Operations desk",
                        "Due / urgency|Today",
                        "Connected systems|CRM, Gmail, Sheets, Drive, Canva, and workflow automations as needed.",
                        "Business context|" + detail,
                        "Recommended next action|Assign an owner, confirm deadline, and keep the workflow visible in Bespoke Ops.",
                        "Production integration note|This record would sync with live brokerage systems and retain approval history.",
                        "Audit / review note|Staff actions would be timestamped for follow-up and accountability."
                },
                "Open Workflow"
        ));

        row.addView(iconWrap(new LineIconView(this, iconName, accent), Color.rgb(242, 245, 246)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        copyParams.leftMargin = dp(11);
        row.addView(copy, copyParams);
        copy.addView(text(title, 14, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(detail, 12, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(2));

        row.addView(pill(status, Color.rgb(236, 241, 243), NAVY));
        return row;
    }

    private void renderIntake() {
        content.addView(sectionLabel("Request Intake", "Capture enough detail for admin, AI, and automations to act"));

        LinearLayout form = panel();
        form.addView(fieldHeader("New agent request", "Webhook-ready data package"));

        Spinner agent = spinner("Avery Chen", "Maya Singh", "Noah Patel", "Sofia Romano", "Jordan Lee");
        Spinner type = spinner("Marketing", "Listing Launch", "Offer Summary", "Home Evaluation", "Neighbourhood Alert", "CRM Update");
        Spinner priority = spinner("Normal", "High", "Urgent");
        EditText address = input("Property address or client name");
        EditText notes = multilineInput("Brief, deadline, audience, links, or special instructions");

        form.addView(labeled("Agent", agent));
        form.addView(labeled("Request type", type));
        form.addView(labeled("Priority", priority));
        form.addView(labeled("Subject", address));
        form.addView(channelPicker());
        form.addView(labeled("Notes", notes));

        Button submit = primaryButton("Queue request", NAVY);
        submit.setOnClickListener(v -> {
            String subject = address.getText().toString().trim();
            if (subject.isEmpty()) {
                address.setError("Add a property, client, or task subject");
                return;
            }

            String channels = collectChannels(form);
            if (channels.isEmpty()) {
                channels = "Admin review";
            }

            requests.add(0, new WorkflowRequest(
                    type.getSelectedItem().toString(),
                    agent.getSelectedItem().toString(),
                    subject,
                    priority.getSelectedItem().toString(),
                    channels,
                    notes.getText().toString().trim().isEmpty() ? "No extra notes provided." : notes.getText().toString().trim(),
                    "Queued now",
                    "Queued"
            ));
            Toast.makeText(this, "Demo action: this would create a routed request and sync with the operations queue.", Toast.LENGTH_SHORT).show();
            renderScreen();
        });
        form.addView(submit, fullWidthMargins(0, dp(12), 0, 0));
        content.addView(form, bottomMargin(14));

        content.addView(sectionLabel("Open Queue", requests.size() + " active work items"));
        for (WorkflowRequest request : requests) {
            content.addView(requestCard(request, false), bottomMargin(10));
        }
    }

    private void renderAiStudio() {
        content.addView(sectionLabel("AI Studio", "Brand-safe drafts for real estate communications"));
        content.addView(aiReviewQueue(), bottomMargin(14));

        LinearLayout studio = panel();
        studio.addView(fieldHeader("Listing email generator", "Prepared for OpenAI or Claude integration"));

        EditText property = input("Property address");
        property.setText("128 King Street West");
        EditText price = input("Price or offer context");
        price.setText("$1,249,000");
        EditText highlights = multilineInput("Property highlights, client context, or offer notes");
        highlights.setText("south-facing loft, parking, walkable restaurants, renovated kitchen");
        Spinner tone = spinner("Bespoke warm luxury", "Investor concise", "Family friendly", "Brokerage leadership", "Bold social caption");

        studio.addView(labeled("Property", property));
        studio.addView(labeled("Context", price));
        studio.addView(labeled("Tone", tone));
        studio.addView(labeled("Highlights", highlights));

        HorizontalScrollView templatesScroll = new HorizontalScrollView(this);
        templatesScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout templates = new LinearLayout(this);
        templates.setOrientation(LinearLayout.HORIZONTAL);
        templatesScroll.addView(templates);
        templates.addView(templateChip("Listing launch", highlights, "photography-ready interiors, premium neighbourhood story, transit access"));
        templates.addView(templateChip("Offer brief", highlights, "deposit, conditions, closing date, inclusions, risk flags"));
        templates.addView(templateChip("Home eval", highlights, "recent upgrades, buyer profile, comparable sales, pricing strategy"));
        studio.addView(templatesScroll, fullWidthMargins(0, dp(8), 0, dp(12)));

        Button generate = primaryButton("Generate reviewed draft", BRASS);
        studio.addView(generate);
        content.addView(studio, bottomMargin(14));

        LinearLayout output = panel();
        output.addView(fieldHeader("Draft preview", "Human approval required before sending"));
        TextView draft = text(lastDraft.isEmpty() ? "Generate a draft to preview the client-ready copy here." : lastDraft, 15, INK, Typeface.NORMAL, FONT_BODY);
        draft.setLineSpacing(dp(3), 1.0f);
        output.addView(draft, bottomMargin(14));

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        Button copy = secondaryButton("Copy");
        Button send = primaryButton("Send to review", NAVY);
        copy.setOnClickListener(v -> copyDraft());
        send.setOnClickListener(v -> Toast.makeText(this, "Demo action: this would send the item to the review queue.", Toast.LENGTH_SHORT).show());
        actions.addView(copy, new LinearLayout.LayoutParams(0, dp(48), 1f));
        LinearLayout.LayoutParams sendParams = new LinearLayout.LayoutParams(0, dp(48), 1f);
        sendParams.leftMargin = dp(10);
        actions.addView(send, sendParams);
        output.addView(actions);
        content.addView(output);

        generate.setOnClickListener(v -> {
            lastDraft = buildDraft(
                    property.getText().toString().trim(),
                    price.getText().toString().trim(),
                    highlights.getText().toString().trim(),
                    tone.getSelectedItem().toString()
            );
            renderScreen();
        });
    }

    private void renderListings() {
        content.addView(sectionLabel("Listing Launch Room", "Everything needed to go from signed listing to market-ready"));

        LinearLayout packagePanel = panel();
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        packagePanel.addView(top);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        top.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        copy.addView(text("24 Lakeshore Road", 22, INK, Typeface.BOLD, FONT_DISPLAY));
        copy.addView(text("Seller launch package | Avery Chen | Friday target", 13, MUTED, Typeface.NORMAL, FONT_BODY));
        top.addView(pill(completedDocs() + "/" + checklistNames.length + " ready", NAVY, Color.WHITE));

        packagePanel.addView(progressStrip(completedDocs(), checklistNames.length), fullWidthMargins(0, dp(14), 0, dp(14)));
        packagePanel.addView(stageRow("1", "Prep", "Docs, disclosure, photography, seller approvals", SUCCESS));
        packagePanel.addView(stageRow("2", "Launch", "MLS copy, Canva kit, social posts, agent email", BRASS));
        packagePanel.addView(stageRow("3", "Follow-up", "Showing feedback, offer room, weekly seller report", NAVY));
        content.addView(packagePanel, bottomMargin(14));

        content.addView(sectionLabel("Offer Summary", "Deal terms prepared for agent review"));
        LinearLayout offer = panel();
        offer.addView(moduleRow("88 Bloor Street W", "Urgent offer brief: closing date, deposit, conditions, inclusions, and risk flags.", "doc", DANGER));
        offer.addView(moduleRow("Client-ready recap", "AI has drafted the summary; Maya Singh approval is required before it is sent.", "spark", BRASS));
        offer.addView(moduleRow("Compliance note", "Confirm FINTRAC check and signed working-with-realtor disclosure before archive.", "check", WARNING));
        content.addView(offer, bottomMargin(14));

        content.addView(sectionLabel("File Control", "Compliance and launch checklist"));
        LinearLayout checklist = panel();
        for (int i = 0; i < checklistNames.length; i++) {
            final int index = i;
            CheckBox box = new CheckBox(this);
            box.setText(checklistNames[i]);
            box.setTextColor(INK);
            box.setTextSize(15);
            box.setTypeface(Typeface.create(FONT_BODY, Typeface.BOLD));
            box.setChecked(checklistState[i]);
            box.setPadding(0, dp(6), 0, dp(6));
            box.setOnCheckedChangeListener((buttonView, isChecked) -> checklistState[index] = isChecked);
            checklist.addView(box);
        }

        Button export = primaryButton("Build presentation packet", NAVY);
        export.setOnClickListener(v -> Toast.makeText(this, "Demo action: this would generate the PDF/Canva packet and store approval history.", Toast.LENGTH_SHORT).show());
        checklist.addView(export, topMargin(12));
        content.addView(checklist, bottomMargin(14));

        content.addView(sectionLabel("Market Modules", "Client-facing services from the Bespoke site"));
        LinearLayout modules = panel();
        modules.addView(moduleRow("Neighbourhood Alerts", "Auto-create saved searches for Toronto, Markham, Ajax, Pickering, and Scarborough.", "pulse", BLUE));
        modules.addView(moduleRow("Free Home Evaluation", "Collect seller intake, improvements, timeline, and CMA packet inputs.", "home", SUCCESS));
        modules.addView(moduleRow("Pre-construction VIP", "Track prospect interest, launch dates, deposits, and allocation status.", "folder", BRASS));
        content.addView(modules);
    }

    private void renderAutomation() {
        content.addView(sectionLabel("Automation Health", "Operational visibility for n8n, Make, Zapier, Playwright, Google, Canva, and CRM"));

        LinearLayout summary = panel();
        summary.addView(fieldHeader("Systems summary", averageHealth() + "% average reliability"));
        summary.addView(moduleRow("Retry policy", "Retry twice, notify the owner, then hold for manual approval.", "pulse", NAVY));
        summary.addView(moduleRow("Compliance posture", "AI drafts and document actions stay human-reviewed before client delivery.", "check", SUCCESS));
        Button refresh = primaryButton("Run health check", BRASS);
        refresh.setOnClickListener(v -> {
            refreshJobs();
            Toast.makeText(this, "Demo action: this would sync health data from automation logs.", Toast.LENGTH_SHORT).show();
            renderScreen();
        });
        summary.addView(refresh, topMargin(12));
        content.addView(summary, bottomMargin(14));

        for (AutomationJob job : jobs) {
            content.addView(jobCard(job), bottomMargin(10));
        }

        content.addView(sectionLabel("Incident Feed", "Recent automation outcomes"));
        for (Incident incident : incidents) {
            content.addView(incidentRow(incident), bottomMargin(8));
        }
    }

    private View metricTile(String label, String value, String helper, int accent) {
        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setGravity(Gravity.CENTER);
        tile.setPadding(dp(8), dp(10), dp(8), dp(10));
        tile.setBackground(outlined(PAPER, dp(7), HAIRLINE));

        tile.addView(text(value, 21, accent, Typeface.BOLD, FONT_MEDIUM));
        TextView labelView = text(label, 11, INK, Typeface.BOLD, FONT_BODY);
        labelView.setGravity(Gravity.CENTER);
        tile.addView(labelView);
        TextView helperView = text(helper, 10, MUTED, Typeface.NORMAL, FONT_BODY);
        helperView.setGravity(Gravity.CENTER);
        tile.addView(helperView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(86), 1f);
        params.rightMargin = dp(8);
        tile.setLayoutParams(params);
        return tile;
    }

    private View requestCard(WorkflowRequest request, boolean compact) {
        LinearLayout card = panel();
        card.setPadding(dp(12), dp(11), dp(12), dp(11));
        card.setOnClickListener(v -> showRequestDetail(request));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(top);

        LineIconView icon = new LineIconView(this, iconForType(request.type), NAVY);
        LinearLayout iconWrap = iconWrap(icon, Color.rgb(236, 241, 243));
        top.addView(iconWrap);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        copyParams.leftMargin = dp(10);
        top.addView(copy, copyParams);
        copy.addView(text(request.type + " | " + request.address, 14, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(request.agent + " | " + request.due, 11, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(1));

        top.addView(pill(request.priority, priorityColor(request.priority), Color.WHITE));

        if (!compact) {
            card.addView(text(request.notes, 13, INK, Typeface.NORMAL, FONT_BODY), topMargin(9));
        }

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        bottom.setGravity(Gravity.CENTER_VERTICAL);
        bottom.addView(pill(request.status, statusColor(request.status), Color.WHITE));
        TextView channels = text(request.channels, 11, MUTED, Typeface.BOLD, FONT_BODY);
        channels.setGravity(Gravity.RIGHT);
        bottom.addView(channels, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        card.addView(bottom, topMargin(10));
        return card;
    }

    private View aiReviewQueue() {
        LinearLayout queue = panel();
        queue.addView(fieldHeader("Review Queue", "Drafts are accelerated by AI and controlled by human approval"));
        queue.addView(draftReviewRow("Listing email", "24 Lakeshore Road", "Ready to Send", BRASS, "Warm luxury listing launch copy prepared for Avery Chen."));
        queue.addView(draftReviewRow("Offer summary", "88 Bloor Street W", "Pending Review", DANGER, "Term recap needs condition and closing-date validation."));
        queue.addView(draftReviewRow("Seller update", "17 Cedar Lane", "In Progress", NAVY, "Weekly showing feedback summary queued for Friday."));
        return queue;
    }

    private View draftReviewRow(String title, String subject, String status, int accent, String detail) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(8), 0, dp(8));
        row.setOnClickListener(v -> showDetailSheet(
                title,
                subject,
                status,
                accent,
                new String[]{
                        "Draft status|" + status,
                        "Owner|Operations review desk",
                        "Due / urgency|Before next client touchpoint",
                        "Subject|" + subject,
                        "Connected systems|Gmail, CRM, Drive, and AI approval history.",
                        "Review note|" + detail,
                        "Recommended next action|Confirm tone, compliance wording, and any missing deal facts before approval.",
                        "Production integration note|OpenAI or Claude output would be stored with review history and approval metadata.",
                        "Audit / review note|Approver, timestamp, prompt version, and final copy would be retained."
                },
                "Review Draft"
        ));

        row.addView(iconWrap(new LineIconView(this, "spark", accent), Color.rgb(242, 245, 246)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        copyParams.leftMargin = dp(11);
        row.addView(copy, copyParams);
        copy.addView(text(title + " | " + subject, 14, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(detail, 12, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(2));
        row.addView(pill(status, accent, Color.WHITE));
        return row;
    }

    private View briefRow(String title, String body, String iconName, int accent) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, dp(10), 0, dp(10));
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(iconWrap(new LineIconView(this, iconName, accent), Color.rgb(242, 244, 245)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.leftMargin = dp(12);
        row.addView(copy, params);
        copy.addView(text(title, 15, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(body, 13, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(3));
        return row;
    }

    private View stageRow(String number, String title, String detail, int accent) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, dp(10), 0, dp(10));
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setOnClickListener(v -> showDetailSheet(
                title,
                "Listing launch stage " + number,
                "In Progress",
                accent,
                new String[]{
                        "Stage|" + title,
                        "Owner|Listing coordinator",
                        "Due / urgency|Before public launch",
                        "Connected systems|Drive, Sheets, CRM, Canva, MLS/admin portal, and agent inbox.",
                        "Operational scope|" + detail,
                        "Recommended next action|Clear outstanding dependencies before moving the launch forward.",
                        "Production integration note|This stage would hold task history, approvals, and related document links.",
                        "Audit / review note|Completed checklist items would retain owner and timestamp."
                },
                "View Stage"
        ));

        TextView badge = text(number, 15, Color.WHITE, Typeface.BOLD, FONT_BODY);
        badge.setGravity(Gravity.CENTER);
        badge.setBackground(rounded(accent, dp(8)));
        row.addView(badge, new LinearLayout.LayoutParams(dp(38), dp(38)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.leftMargin = dp(12);
        row.addView(copy, params);
        copy.addView(text(title, 15, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(detail, 13, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(3));
        return row;
    }

    private View moduleRow(String title, String detail, String iconName, int accent) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(10), 0, dp(10));
        row.setOnClickListener(v -> showDetailSheet(
                title,
                "Workflow module",
                "Active",
                accent,
                new String[]{
                        "Module|" + title,
                        "Owner|Operations desk",
                        "Due / urgency|Based on client or agent deadline",
                        "Connected systems|CRM, Gmail, Sheets, Drive, Canva, and automation workflows as needed.",
                        "Business use|" + detail,
                        "Recommended next action|Confirm data source, owner, and approval path before delivery.",
                        "Production integration note|A secure production integration would replace demo data and persist audit history.",
                        "Audit / review note|Changes would be logged against the request or listing record."
                },
                "Open Module"
        ));
        row.addView(iconWrap(new LineIconView(this, iconName, accent), Color.rgb(242, 244, 245)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.leftMargin = dp(12);
        row.addView(copy, params);
        copy.addView(text(title, 15, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(detail, 13, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(3));
        return row;
    }

    private View jobCard(AutomationJob job) {
        LinearLayout card = panel();
        card.setPadding(dp(14), dp(12), dp(14), dp(12));
        card.setOnClickListener(v -> showDetailSheet(
                job.name,
                job.description,
                job.status,
                job.color,
                new String[]{
                        "System|" + job.system,
                        "Owner|Automation owner",
                        "Due / urgency|Monitor continuously during business hours",
                        "Reliability|" + job.health + "%",
                        "Current status|" + job.status,
                        "Connected systems|n8n, Make, Zapier, Playwright, CRM, Gmail, Sheets, and Canva where applicable.",
                        "Recommended next action|Review the latest run, confirm whether retry or manual approval is needed.",
                        "Alert policy|Retry twice, notify owner, then hold for manual review.",
                        "Production integration note|This card would read from automation job logs and webhook delivery history.",
                        "Audit / review note|Failures, retries, owners, and resolutions would be retained."
                },
                "Inspect Automation"
        ));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(top);

        top.addView(iconWrap(new LineIconView(this, "pulse", job.color), Color.rgb(242, 244, 245)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        copyParams.leftMargin = dp(12);
        top.addView(copy, copyParams);
        copy.addView(text(job.name, 16, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(job.description, 12, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(2));

        top.addView(pill(job.system, Color.rgb(235, 240, 242), NAVY));

        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgress(job.health);
        card.addView(bar, fullWidthMargins(0, dp(14), 0, dp(7)));

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        bottom.addView(text(job.health + "% reliability", 13, MUTED, Typeface.BOLD, FONT_BODY), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        bottom.addView(pill(job.status, job.color, Color.WHITE));
        card.addView(bottom);
        return card;
    }

    private View incidentRow(Incident incident) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(14), dp(12), dp(14), dp(12));
        row.setBackground(outlined(SURFACE, dp(8), BORDER));
        row.setOnClickListener(v -> showDetailSheet(
                incident.title,
                incident.time,
                "Needs Review",
                incident.color,
                new String[]{
                        "Incident|" + incident.title,
                        "Owner|Operations desk",
                        "Due / urgency|Same business day",
                        "Detail|" + incident.detail,
                        "Logged|" + incident.time,
                        "Connected systems|Depends on affected workflow: CRM, Gmail, Sheets, Canva, Drive, or Playwright.",
                        "Recommended action|Review the affected workflow, confirm owner, and document the resolution.",
                        "Production integration note|Production builds should retain retries, owner notes, and final resolution.",
                        "Audit / review note|Resolution notes would be stored against the incident."
                },
                "Review Incident"
        ));

        View stripe = new View(this);
        stripe.setBackground(rounded(incident.color, dp(3)));
        row.addView(stripe, new LinearLayout.LayoutParams(dp(5), dp(48)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        copyParams.leftMargin = dp(12);
        row.addView(copy, copyParams);
        copy.addView(text(incident.title, 15, INK, Typeface.BOLD, FONT_BODY));
        copy.addView(text(incident.detail, 13, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(3));
        row.addView(text(incident.time, 12, MUTED, Typeface.BOLD, FONT_BODY));
        return row;
    }

    private void showRequestDetail(WorkflowRequest request) {
        showDetailSheet(
                request.type + " Request",
                request.address + " | " + request.agent,
                request.status,
                priorityColor(request.priority),
                new String[]{
                        "Status|" + request.status,
                        "Owner|" + request.agent,
                        "Priority|" + request.priority,
                        "Due|" + request.due,
                        "Connected systems|" + request.channels,
                        "Operations note|" + request.notes,
                        "Recommended next action|Confirm owner, due time, approval path, and delivery channel.",
                        "Production integration note|This record would be stored with source, approval history, and webhook delivery logs.",
                        "Audit / review note|Status changes would be logged for the operations desk."
                },
                "Open Request"
        );
    }

    private void showDetailSheet(String title, String subtitle, String status, int accent, String[] rows, String actionLabel) {
        Dialog dialog = new Dialog(this);
        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setPadding(dp(18), dp(18), dp(18), dp(16));
        shell.setBackground(outlined(PAPER, dp(10), HAIRLINE));

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        shell.addView(header);

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        header.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        copy.addView(text(title, 19, INK, Typeface.BOLD, FONT_MEDIUM));
        copy.addView(text(subtitle, 12, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(2));
        header.addView(pill(status, accent, Color.WHITE));

        View divider = new View(this);
        divider.setBackgroundColor(HAIRLINE);
        shell.addView(divider, fullWidthMargins(0, dp(14), 0, dp(10)));
        divider.getLayoutParams().height = Math.max(1, dp(1));

        for (String row : rows) {
            shell.addView(detailRow(row));
        }

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        Button close = secondaryButton("Close");
        Button primary = primaryButton(actionLabel, NAVY);
        close.setOnClickListener(v -> dialog.dismiss());
        primary.setOnClickListener(v -> {
            Toast.makeText(this, "Demo action: approval history would be stored in production.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        actions.addView(close, new LinearLayout.LayoutParams(0, dp(46), 1f));
        LinearLayout.LayoutParams primaryParams = new LinearLayout.LayoutParams(0, dp(46), 1f);
        primaryParams.leftMargin = dp(10);
        actions.addView(primary, primaryParams);
        shell.addView(actions, fullWidthMargins(0, dp(14), 0, 0));

        dialog.setContentView(shell);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private View detailRow(String packed) {
        String[] parts = packed.split("\\|", 2);
        String label = parts.length > 0 ? parts[0] : "Detail";
        String value = parts.length > 1 ? parts[1] : "";

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(0, dp(7), 0, dp(7));
        row.addView(text(label, 11, MUTED, Typeface.BOLD, FONT_BODY));
        row.addView(text(value, 13, INK, Typeface.NORMAL, FONT_BODY), topMargin(2));
        return row;
    }

    private View channelPicker() {
        LinearLayout wrap = new LinearLayout(this);
        wrap.setOrientation(LinearLayout.VERTICAL);
        wrap.setPadding(0, dp(7), 0, dp(7));
        wrap.addView(text("Systems", 12, MUTED, Typeface.BOLD, FONT_BODY));

        String[][] rows = {
                {"Canva", "Instagram"},
                {"Gmail", "CRM"},
                {"Sheets", "Drive"},
                {"Docs", "Playwright"}
        };

        for (String[] rowValues : rows) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for (String value : rowValues) {
                CheckBox box = new CheckBox(this);
                box.setTag("channel");
                box.setText(value);
                box.setTextColor(INK);
                box.setTextSize(14);
                box.setTypeface(Typeface.create(FONT_BODY, Typeface.NORMAL));
                row.addView(box, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            }
            wrap.addView(row);
        }
        return wrap;
    }

    private View templateChip(String label, EditText target, String value) {
        TextView chip = text(label, 12, NAVY, Typeface.BOLD, FONT_BODY);
        chip.setGravity(Gravity.CENTER);
        chip.setPadding(dp(12), 0, dp(12), 0);
        chip.setMinHeight(dp(36));
        chip.setBackground(outlined(Color.rgb(240, 244, 246), dp(18), Color.rgb(213, 221, 225)));
        chip.setOnClickListener(v -> target.setText(value));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(36));
        params.rightMargin = dp(8);
        chip.setLayoutParams(params);
        return chip;
    }

    private View progressStrip(int complete, int total) {
        LinearLayout strip = new LinearLayout(this);
        strip.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < total; i++) {
            View block = new View(this);
            block.setBackground(rounded(i < complete ? NAVY : Color.rgb(224, 229, 232), dp(3)));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(8), 1f);
            params.rightMargin = dp(4);
            strip.addView(block, params);
        }
        return strip;
    }

    private LinearLayout iconWrap(LineIconView icon, int background) {
        FrameLayout frame = new FrameLayout(this);
        frame.setBackground(rounded(background, dp(7)));
        frame.addView(icon, new FrameLayout.LayoutParams(dp(21), dp(21), Gravity.CENTER));
        LinearLayout wrap = new LinearLayout(this);
        wrap.addView(frame, new LinearLayout.LayoutParams(dp(38), dp(38)));
        return wrap;
    }

    private LinearLayout panel() {
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(13), dp(13), dp(13), dp(13));
        panel.setBackground(outlined(PAPER, dp(7), HAIRLINE));
        panel.setElevation(dp(1));
        return panel;
    }

    private View sectionLabel(String title, String subtitle) {
        LinearLayout label = new LinearLayout(this);
        label.setOrientation(LinearLayout.VERTICAL);
        label.setPadding(0, dp(6), 0, dp(9));
        label.addView(text(title, 18, INK, Typeface.BOLD, FONT_MEDIUM));
        label.addView(text(subtitle, 12, MUTED, Typeface.NORMAL, FONT_BODY), topMargin(1));
        return label;
    }

    private View fieldHeader(String title, String subtitle) {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.addView(text(title, 16, INK, Typeface.BOLD, FONT_BODY));
        header.addView(text(subtitle, 12, MUTED, Typeface.NORMAL, FONT_BODY), fullWidthMargins(0, dp(2), 0, dp(8)));
        return header;
    }

    private View labeled(String label, View field) {
        LinearLayout wrap = new LinearLayout(this);
        wrap.setOrientation(LinearLayout.VERTICAL);
        wrap.setPadding(0, dp(7), 0, dp(7));
        wrap.addView(text(label, 12, MUTED, Typeface.BOLD, FONT_BODY));
        wrap.addView(field, topMargin(4));
        return wrap;
    }

    private EditText input(String hint) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setTextColor(INK);
        input.setHintTextColor(Color.rgb(139, 150, 158));
        input.setTextSize(15);
        input.setTypeface(Typeface.create(FONT_BODY, Typeface.NORMAL));
        input.setSingleLine(true);
        input.setMinHeight(dp(50));
        input.setPadding(dp(12), 0, dp(12), 0);
        input.setBackground(outlined(Color.WHITE, dp(7), BORDER));
        return input;
    }

    private EditText multilineInput(String hint) {
        EditText input = input(hint);
        input.setSingleLine(false);
        input.setMinLines(3);
        input.setGravity(Gravity.TOP);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setPadding(dp(12), dp(12), dp(12), dp(12));
        return input;
    }

    private Spinner spinner(String... values) {
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setMinimumHeight(dp(50));
        spinner.setPadding(dp(8), 0, dp(8), 0);
        spinner.setBackground(outlined(Color.WHITE, dp(7), BORDER));
        return spinner;
    }

    private Button primaryButton(String label, int color) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setTextColor(Color.WHITE);
        button.setTextSize(15);
        button.setTypeface(Typeface.create(FONT_MEDIUM, Typeface.BOLD));
        button.setMinHeight(dp(48));
        button.setPadding(dp(12), 0, dp(12), 0);
        button.setBackground(rounded(color, dp(7)));
        return button;
    }

    private Button secondaryButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setTextColor(NAVY);
        button.setTextSize(15);
        button.setTypeface(Typeface.create(FONT_MEDIUM, Typeface.BOLD));
        button.setMinHeight(dp(48));
        button.setPadding(dp(12), 0, dp(12), 0);
        button.setBackground(outlined(SURFACE, dp(7), Color.rgb(191, 204, 212)));
        return button;
    }

    private TextView pill(String label, int background, int color) {
        TextView pill = text(label, 10, color, Typeface.BOLD, FONT_BODY);
        pill.setGravity(Gravity.CENTER);
        pill.setMinHeight(dp(26));
        pill.setPadding(dp(9), 0, dp(9), 0);
        pill.setBackground(rounded(background, dp(13)));
        return pill;
    }

    private TextView text(String value, int sp, int color, int style, String family) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setTypeface(Typeface.create(family, style));
        view.setIncludeFontPadding(true);
        view.setLineSpacing(0, 1.0f);
        return view;
    }

    private GradientDrawable gradient(int start, int end) {
        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{start, end}
        );
        drawable.setCornerRadius(dp(8));
        return drawable;
    }

    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private GradientDrawable outlined(int color, int radius, int stroke) {
        GradientDrawable drawable = rounded(color, radius);
        drawable.setStroke(dp(1), stroke);
        return drawable;
    }

    private LinearLayout.LayoutParams fullWidthMargins(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, right, bottom);
        return params;
    }

    private LinearLayout.LayoutParams topMargin(int top) {
        return fullWidthMargins(0, top, 0, 0);
    }

    private LinearLayout.LayoutParams bottomMargin(int bottom) {
        return fullWidthMargins(0, 0, 0, bottom);
    }

    private int completedDocs() {
        int complete = 0;
        for (boolean state : checklistState) {
            if (state) {
                complete++;
            }
        }
        return complete;
    }

    private int averageHealth() {
        int total = 0;
        for (AutomationJob job : jobs) {
            total += job.health;
        }
        return jobs.isEmpty() ? 0 : total / jobs.size();
    }

    private String collectChannels(View view) {
        ArrayList<String> values = new ArrayList<>();
        collectChannelsRecursive(view, values);
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(value);
        }
        return builder.toString();
    }

    private void collectChannelsRecursive(View view, ArrayList<String> values) {
        if (view instanceof CheckBox && "channel".equals(view.getTag()) && ((CheckBox) view).isChecked()) {
            values.add(((CheckBox) view).getText().toString());
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                collectChannelsRecursive(group.getChildAt(i), values);
            }
        }
    }

    private String buildDraft(String property, String context, String highlights, String tone) {
        String cleanProperty = property.isEmpty() ? "the featured property" : property;
        String cleanContext = context.isEmpty() ? "available now" : context;
        String cleanHighlights = highlights.isEmpty() ? "a polished presentation, practical layout, and strong neighbourhood appeal" : highlights;
        String date = new SimpleDateFormat("MMM d", Locale.US).format(new Date());

        if ("Investor concise".equals(tone)) {
            return "Subject: Investment note for " + cleanProperty + "\n\n"
                    + "Hi,\n\n"
                    + cleanProperty + " is positioned at " + cleanContext + " and stands out for " + cleanHighlights + ". "
                    + "The main items to review are rental appeal, carrying costs, comparable activity, and exit flexibility.\n\n"
                    + "I can send the details package and comparable summary for your review.\n\n"
                    + "Prepared by Bespoke Ops | " + date;
        }

        if ("Family friendly".equals(tone)) {
            return "Subject: A thoughtful next chapter at " + cleanProperty + "\n\n"
                    + "Hi,\n\n"
                    + "I wanted to share " + cleanProperty + ", currently noted as " + cleanContext + ". "
                    + "It has the everyday qualities buyers tend to notice quickly: " + cleanHighlights + ".\n\n"
                    + "I can arrange a private showing or send the full listing package when convenient.\n\n"
                    + "Prepared by Bespoke Ops | " + date;
        }

        if ("Brokerage leadership".equals(tone)) {
            return "Team brief: " + cleanProperty + "\n\n"
                    + "Objective: keep the client experience consistent from intake to delivery.\n\n"
                    + "Context: " + cleanContext + "\n"
                    + "Key details: " + cleanHighlights + "\n"
                    + "Recommended next action: confirm owner, approval path, and client-facing deadline before automation delivery.\n\n"
                    + "Prepared by Bespoke Ops | " + date;
        }

        if ("Bold social caption".equals(tone)) {
            return cleanProperty + " is ready for its moment. " + cleanContext + ". "
                    + "Highlights include " + cleanHighlights + ". "
                    + "Message the Bespoke team for the private details package.";
        }

        return "Subject: Private look at " + cleanProperty + "\n\n"
                + "Hi,\n\n"
                + "I thought you might appreciate an early look at " + cleanProperty + ", with current context noted as " + cleanContext + ". "
                + "The property brings together " + cleanHighlights + ", with the calm, customized guidance the Bespoke team is known for.\n\n"
                + "Would you like the full feature sheet or a private showing time?\n\n"
                + "Prepared by Bespoke Ops | " + date;
    }

    private void copyDraft() {
        if (lastDraft.isEmpty()) {
            Toast.makeText(this, "Generate a draft first", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Bespoke Ops Draft", lastDraft));
        Toast.makeText(this, "Draft copied", Toast.LENGTH_SHORT).show();
    }

    private void refreshJobs() {
        for (AutomationJob job : jobs) {
            int change = random.nextInt(21) - 7;
            job.health = Math.max(58, Math.min(99, job.health + change));
            if (job.health >= 88) {
                job.status = "Healthy";
                job.color = SUCCESS;
            } else if (job.health >= 76) {
                job.status = "Watching";
                job.color = WARNING;
            } else {
                job.status = "Needs review";
                job.color = DANGER;
            }
        }
    }

    private int priorityColor(String priority) {
        if ("Urgent".equals(priority)) {
            return DANGER;
        }
        if ("High".equals(priority)) {
            return WARNING;
        }
        return NAVY;
    }

    private int statusColor(String status) {
        if ("Working".equals(status)) {
            return BRASS;
        }
        if ("In review".equals(status)) {
            return NAVY_SOFT;
        }
        return Color.rgb(104, 112, 118);
    }

    private String iconForType(String type) {
        if (type.contains("Marketing")) {
            return "spark";
        }
        if (type.contains("Offer")) {
            return "doc";
        }
        if (type.contains("Listing") || type.contains("Evaluation")) {
            return "home";
        }
        return "folder";
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static class BrandMarkView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();

        BrandMarkView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = getWidth();
            float h = getHeight();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(new RectF(0, 0, w, h), h * 0.18f, h * 0.18f, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(h * 0.055f);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setColor(NAVY);
            path.reset();
            path.moveTo(w * 0.18f, h * 0.58f);
            path.lineTo(w * 0.18f, h * 0.42f);
            path.lineTo(w * 0.34f, h * 0.28f);
            path.lineTo(w * 0.50f, h * 0.42f);
            path.lineTo(w * 0.65f, h * 0.30f);
            path.lineTo(w * 0.82f, h * 0.47f);
            path.lineTo(w * 0.82f, h * 0.62f);
            path.lineTo(w * 0.18f, h * 0.62f);
            canvas.drawPath(path, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setTypeface(Typeface.create(FONT_DISPLAY, Typeface.BOLD));
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(h * 0.19f);
            canvas.drawText("BO", w * 0.50f, h * 0.86f, paint);
        }
    }

    private static class LineIconView extends ImageView {
        private final String type;
        private final int color;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();

        LineIconView(Context context, String type, int color) {
            super(context);
            this.type = type;
            this.color = color;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = getWidth();
            float h = getHeight();
            paint.setColor(color);
            paint.setStrokeWidth(Math.max(3f, w * 0.08f));
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            path.reset();

            if ("home".equals(type)) {
                path.moveTo(w * 0.15f, h * 0.52f);
                path.lineTo(w * 0.50f, h * 0.18f);
                path.lineTo(w * 0.85f, h * 0.52f);
                path.moveTo(w * 0.25f, h * 0.48f);
                path.lineTo(w * 0.25f, h * 0.82f);
                path.lineTo(w * 0.75f, h * 0.82f);
                path.lineTo(w * 0.75f, h * 0.48f);
                canvas.drawPath(path, paint);
                return;
            }

            if ("plus".equals(type)) {
                canvas.drawLine(w * 0.50f, h * 0.20f, w * 0.50f, h * 0.80f, paint);
                canvas.drawLine(w * 0.20f, h * 0.50f, w * 0.80f, h * 0.50f, paint);
                return;
            }

            if ("spark".equals(type)) {
                path.moveTo(w * 0.50f, h * 0.10f);
                path.lineTo(w * 0.58f, h * 0.40f);
                path.lineTo(w * 0.88f, h * 0.50f);
                path.lineTo(w * 0.58f, h * 0.60f);
                path.lineTo(w * 0.50f, h * 0.90f);
                path.lineTo(w * 0.42f, h * 0.60f);
                path.lineTo(w * 0.12f, h * 0.50f);
                path.lineTo(w * 0.42f, h * 0.40f);
                path.close();
                canvas.drawPath(path, paint);
                return;
            }

            if ("folder".equals(type)) {
                RectF body = new RectF(w * 0.14f, h * 0.30f, w * 0.86f, h * 0.80f);
                canvas.drawRoundRect(body, w * 0.08f, w * 0.08f, paint);
                canvas.drawLine(w * 0.18f, h * 0.30f, w * 0.38f, h * 0.30f, paint);
                canvas.drawLine(w * 0.38f, h * 0.30f, w * 0.46f, h * 0.42f, paint);
                return;
            }

            if ("pulse".equals(type)) {
                path.moveTo(w * 0.12f, h * 0.58f);
                path.lineTo(w * 0.30f, h * 0.58f);
                path.lineTo(w * 0.40f, h * 0.30f);
                path.lineTo(w * 0.55f, h * 0.76f);
                path.lineTo(w * 0.66f, h * 0.48f);
                path.lineTo(w * 0.88f, h * 0.48f);
                canvas.drawPath(path, paint);
                return;
            }

            if ("doc".equals(type)) {
                RectF page = new RectF(w * 0.22f, h * 0.12f, w * 0.78f, h * 0.88f);
                canvas.drawRoundRect(page, w * 0.05f, w * 0.05f, paint);
                canvas.drawLine(w * 0.34f, h * 0.42f, w * 0.66f, h * 0.42f, paint);
                canvas.drawLine(w * 0.34f, h * 0.56f, w * 0.66f, h * 0.56f, paint);
                canvas.drawLine(w * 0.34f, h * 0.70f, w * 0.56f, h * 0.70f, paint);
                return;
            }

            if ("check".equals(type)) {
                path.moveTo(w * 0.18f, h * 0.52f);
                path.lineTo(w * 0.42f, h * 0.75f);
                path.lineTo(w * 0.84f, h * 0.25f);
                canvas.drawPath(path, paint);
                return;
            }

            canvas.drawCircle(w * 0.50f, h * 0.50f, w * 0.30f, paint);
        }
    }

    private static class WorkflowRequest {
        final String type;
        final String agent;
        final String address;
        final String priority;
        final String channels;
        final String notes;
        final String due;
        final String status;

        WorkflowRequest(String type, String agent, String address, String priority, String channels, String notes, String due, String status) {
            this.type = type;
            this.agent = agent;
            this.address = address;
            this.priority = priority;
            this.channels = channels;
            this.notes = notes;
            this.due = due;
            this.status = status;
        }
    }

    private static class AutomationJob {
        final String name;
        final String description;
        int health;
        String status;
        final String system;
        int color;

        AutomationJob(String name, String description, int health, String status, String system, int color) {
            this.name = name;
            this.description = description;
            this.health = health;
            this.status = status;
            this.system = system;
            this.color = color;
        }
    }

    private static class Incident {
        final String title;
        final String detail;
        final String time;
        final int color;

        Incident(String title, String detail, String time, int color) {
            this.title = title;
            this.detail = detail;
            this.time = time;
            this.color = color;
        }
    }
}
