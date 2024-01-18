package com.example.medicare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

public class AppointmentFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_appointments, container, false);

        // Initialize the TabLayout and ViewPager2
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        // Initialize array list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Upcoming");
        arrayList.add("Missed");
        arrayList.add("Open");

        // Initialize adapter
        MainAdapter adapter = new MainAdapter(this);
        for (String title : arrayList) {
            MainFragment fragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment);
        }
        viewPager.setAdapter(adapter);

        // Connect TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(arrayList.get(position))
        ).attach();

        return view;
    }

    private static class MainAdapter extends FragmentStateAdapter {
        private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        public MainAdapter(Fragment fragment) {
            super(fragment.getChildFragmentManager(), fragment.getLifecycle());
        }

        public void addFragment(Fragment fragment) {
            fragmentArrayList.add(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentArrayList.size();
        }
    }
}
