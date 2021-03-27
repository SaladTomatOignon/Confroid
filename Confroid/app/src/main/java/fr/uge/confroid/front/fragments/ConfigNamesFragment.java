package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import fr.uge.confroid.R;
import fr.uge.confroid.front.adapters.ConfigNameListAdapter;
import fr.uge.confroid.front.models.ConfigNameListItem;
import fr.uge.confroid.providers.ProviderType;

public class ConfigNamesFragment extends Fragment {
    private static final String SOURCE_KEYWORD = "DATA_SOURCE";

    private View view;
    private ProviderType providerType;

    public static ConfigNamesFragment newInstance(ProviderType source) {
        ConfigNamesFragment fragment = new ConfigNamesFragment();

        Bundle bundle = new Bundle();
        bundle.putString(SOURCE_KEYWORD, source.toString());
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.providerType = ProviderType.valueOf(getArguments().getString(SOURCE_KEYWORD));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_config_names, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity));

        View emptyState = view.findViewById(R.id.empty_state);
        emptyState.setVisibility(View.VISIBLE);

        providerType.getProvider().getNames(activity, configsName -> {
                    List<ConfigNameListItem> items = configsName
                            .stream()
                            .map(name -> ConfigNameListItem.create(activity, name, providerType))
                            .collect(Collectors.toList());

                    if (!items.isEmpty()) {
                        emptyState.setVisibility(View.GONE);
                    }

                    recycler.setAdapter(new ConfigNameListAdapter(items));
                },
                error -> Toast.makeText(activity, error, Toast.LENGTH_SHORT).show());
    }
}