package fr.uge.confroid.front;

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
import fr.uge.confroid.front.models.EditorSession;
import fr.uge.confroid.storage.ConfroidPackage;
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
    private Stack<EditorSession> stack = new Stack<>();
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
        push(new EditorSession(getString(R.string.app_name), root));
    }

    @Override
    public void save() {
    }

    @Override
    public void push(EditorSession session) {
        getSupportActionBar().setTitle(session.getName());

        for (Function<Value, Fragment> fn : fragments) {
            Fragment editor = fn.apply(session.getValue());
            if (editor != null) {
                stack.push(session);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, editor);
                transaction.addToBackStack(null);
                transaction.commit();
                return;
            }
        }

        throw new IllegalArgumentException(
            "There is no registered editor to handle " + session.getValue().toString()
        );
    }

    @Override
    public EditorSession pop() {
        return stack.peek();
    }

    @Override
    public void onBackPressed() {
        if (!stack.isEmpty()) {
            stack.pop();
        }

        if (!stack.isEmpty()) {
            getSupportActionBar().setTitle(stack.peek().getName());
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
