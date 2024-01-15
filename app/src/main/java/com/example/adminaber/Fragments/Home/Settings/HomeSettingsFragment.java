package com.example.adminaber.Fragments.Home.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import static com.example.adminaber.Utils.AndroidUtil.replaceFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminaber.Fragments.Home.MainHomeFragment;
import com.example.adminaber.Fragments.Home.Settings.Language.LanguageSettingsFragment;
import com.example.adminaber.R;


public class HomeSettingsFragment extends Fragment {
    private ImageView buttonBack;
    private TextView languageSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home_settings, container, false);
        buttonBack = root.findViewById(R.id.back);
        languageSetting = root.findViewById(R.id.setting_language);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragmentTransaction.replace(R.id.fragment_main_container, new MainHomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        languageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                replaceFragment(new LanguageSettingsFragment(),fragmentManager,fragmentTransaction,R.id.fragment_main_container);
            }
        });

        return root;
    }
}