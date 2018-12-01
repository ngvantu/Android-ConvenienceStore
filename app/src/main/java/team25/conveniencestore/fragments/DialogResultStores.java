package team25.conveniencestore.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team25.conveniencestore.R;
import team25.conveniencestore.placeinfo_fragments.SectionsPageAdapter;

public class DialogResultStores extends DialogFragment{

    private static String TAG = "DialogResultStores";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.dialog_result_stores, container, false);

        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.dlg_tablayout);
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.dlg_viewpager);
        Fragment tab1 = new ResultStoresFragment();
        Fragment tab2 = new ResultStoresFragment();

        Bundle bundle = new Bundle();
        if (getArguments() != null) {
            bundle.putParcelableArrayList(ResultStoresFragment.LIST_RESULTS, getArguments().getParcelableArrayList(ResultStoresFragment.LIST_RESULTS));
        }
        tab1.setArguments(bundle);
        tab2.setArguments(bundle);
        adapter.addFragment(tab1, "Kết quả");
        adapter.addFragment(tab2, "Yêu thích");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return mView;
    }
}
