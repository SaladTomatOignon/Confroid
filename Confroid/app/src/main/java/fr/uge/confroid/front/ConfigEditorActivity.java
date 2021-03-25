package fr.uge.confroid.front;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.fragments.ArrayEditorFragment;
import fr.uge.confroid.front.fragments.BoolEditorFragment;
import fr.uge.confroid.front.fragments.MapEditorFragment;
import fr.uge.confroid.front.fragments.TextEditorFragment;
import fr.uge.confroid.front.models.Editor;
import fr.uge.confroid.front.models.EditorPage;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroidlib.BundleUtils;
import fr.uge.confroidlib.ConfroidIntents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Function;

public class ConfigEditorActivity extends AppCompatActivity implements Editor, FragmentManager.OnBackStackChangedListener {
    private static final String TAG = "ConfigEditorActivity";
    private final ArrayList<Function<Value, Fragment>> fragments = new ArrayList<>();

    private Value root;
    private Stack<EditorPage> pages = new Stack<>();
    private ActionBar actionBar;
    private FragmentManager fragmentManager;

    public static void present(Context context, ConfroidPackage confroidPackage) {
        Intent intent = new Intent(context, ConfigEditorActivity.class);
        Bundle content = confroidPackage.getConfig().toBundle();
        intent.putExtra(ConfroidIntents.EXTRA_CONTENT, content);
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

        fragments.add(MapEditorFragment::newInstance);
        fragments.add(BoolEditorFragment::newInstance);
        fragments.add(TextEditorFragment::newInstance);
        fragments.add(ArrayEditorFragment::newInstance);

        root = Configuration.fromBundle(content).getContent();
        pushPage(new EditorPage(getString(R.string.app_name), root));
    }


    @Override
    public void pushPage(EditorPage page) {
        actionBar.setTitle(page.getName());

        for (Function<Value, Fragment> fn : fragments) {
            Fragment editor = fn.apply(page.getValue());
            if (editor != null) {
                pages.push(page);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, editor);
                transaction.addToBackStack(null);
                transaction.commit();
                return;
            }
        }

        throw new IllegalArgumentException(
            "There is no registered editor to handle " + page.getValue().toString()
        );
    }


    @Override
    public EditorPage peekPage() {
        return pages.peek();
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
        if (!pages.isEmpty()) {
            pages.pop();
        }

        if (!pages.isEmpty()) {
            actionBar.setTitle(pages.peek().getName());
        }

        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            finish();
        }
    }

}
