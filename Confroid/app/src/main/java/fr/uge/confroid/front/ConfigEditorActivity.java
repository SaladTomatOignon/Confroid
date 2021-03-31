package fr.uge.confroid.front;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.fragments.ArrayEditorFragment;
import fr.uge.confroid.front.fragments.BoolEditorFragment;
import fr.uge.confroid.front.fragments.MapEditorFragment;
import fr.uge.confroid.front.fragments.TextEditorFragment;
import fr.uge.confroid.front.models.EditorOpener;
import fr.uge.confroid.providers.ConfigProvider;
import fr.uge.confroid.providers.ProviderType;
import fr.uge.confroid.front.models.EditorContext;
import fr.uge.confroid.front.models.EditorArgs;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroidlib.BundleUtils;
import fr.uge.confroidlib.ConfroidIntents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

public class ConfigEditorActivity extends AppCompatActivity implements EditorContext, FragmentManager.OnBackStackChangedListener {
    private static final String TAG = "ConfigEditorActivity";
    private final ArrayList<EditorOpener> openers = new ArrayList<>();
    private final Stack<EditorArgs> args = new Stack<>();

    private Value root;
    private ActionBar actionBar;
    private FragmentManager fragmentManager;
    private boolean changed;

    public static void present(Context context, ConfroidPackage confroidPackage, ProviderType providerType) {
        ConfigProvider provider = providerType.getProvider();

        String receiver = UUID.randomUUID().toString();
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                Bundle content = intent.getBundleExtra(ConfroidIntents.EXTRA_CONTENT);
                if (content == null) {
                    return;
                }
                confroidPackage.setConfig(Configuration.fromBundle(content));
                provider.savePackage(context, confroidPackage, null, null);
            }
        }, new IntentFilter(receiver));

        Intent intent = new Intent();
        intent.setAction(ConfroidIntents.INTENT_CONFIG_EDITOR);
        intent.setPackage(ConfroidIntents.PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        intent.putExtra(ConfroidIntents.EXTRA_RECEIVER, receiver);
        intent.putExtra(ConfroidIntents.EXTRA_CONTENT, confroidPackage.getConfig().toBundle());

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_editor);

        actionBar = getSupportActionBar();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        Intent intent = getIntent();
        Bundle content = intent.getBundleExtra(ConfroidIntents.EXTRA_CONTENT);
        if (content == null) {
            Log.e(TAG, "Missing extra " + ConfroidIntents.EXTRA_CONTENT);
            finish();
            return;
        }

        openers.add(new MapEditorFragment.Opener());
        openers.add(new BoolEditorFragment.Opener());
        openers.add(new TextEditorFragment.Opener());
        openers.add(new ArrayEditorFragment.Opener());

        root = Configuration.fromBundle(content).getContent();
        pushEditor(EditorArgs.create(this, getString(R.string.app_name), root));
    }


    @Override
    public void onChange(Value newValue) {
        currentEditorArgs().setValue(newValue);
        changed = true;
    }

    @Override
    public void pushEditor(EditorArgs args) {
        actionBar.setTitle(args.getName());

        for (EditorOpener opener : openers) {
            if (opener.canHandle(args)) {
                this.args.push(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        );
                transaction.replace(R.id.fragment_container, opener.createEditor());
                transaction.addToBackStack(null);
                transaction.commit();
                return;
            }
        }

        throw new IllegalArgumentException(
            "There is no registered editor to handle " + args.getValue().toString()
        );
    }

    @Override
    public EditorArgs currentEditorArgs() {
        return args.peek();
    }

    @Override
    public Value resolveReference(Value value) {
        if (value.isMap()) {
            Value ref = value.getMap().get(BundleUtils.REF_KEYWORD);
            if (ref != null) {
                value = Configuration.getReferencedValue(
                    ref.getInteger(),
                    root
                );
            }
        }
        return value;
    }


    @Override
    public void onBackPressed() {
        if (!args.isEmpty()) {
            args.pop();
        }

        if (!args.isEmpty()) {
            actionBar.setTitle(args.peek().getName());
        }

        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            finish();

            String receiver = getIntent().getStringExtra(ConfroidIntents.EXTRA_RECEIVER);
            if (receiver != null) {
                Intent response = new Intent(receiver);
                if (changed) {
                    response.putExtra(ConfroidIntents.EXTRA_CONTENT, new Configuration(root).toBundle());
                }
                sendBroadcast(response);
            }
        }
    }
}
