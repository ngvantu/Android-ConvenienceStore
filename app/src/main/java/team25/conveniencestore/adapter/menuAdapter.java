package team25.conveniencestore.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import team25.conveniencestore.fragments.Tab1;
import team25.conveniencestore.fragments.Tab2;
import team25.conveniencestore.fragments.Tab3;
import team25.conveniencestore.fragments.Tab4;
import team25.conveniencestore.fragments.Tab5;
import team25.conveniencestore.fragments.Tab6;

public class menuAdapter extends FragmentStatePagerAdapter {

    private String ListTab[] = {"ĐÃ GẮN NHÃN","ĐÃ LƯU", "ĐÃ CHIA SẺ","SẮP TỚI","ĐẪ GHÉ THĂM","BẢN ĐỒ"};
    private Tab1 tab1;
    private Tab2 tab2;
    private Tab3 tab3;
    private Tab4 tab4;
    private Tab5 tab5;
    private Tab6 tab6;

    public menuAdapter(FragmentManager fm) {
        super(fm);
        tab1 =  new Tab1();
        tab2 = new Tab2();
        tab3 = new Tab3();
        tab4 = new Tab4();
        tab5 = new Tab5();
        tab6 = new Tab6();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0: return tab1;
            case 1: return tab2;
            case 2: return tab3;
            case 3: return tab4;
            case 4: return tab5;
            case 5: return tab6;
        }
        return null;
    }

    @Override
    public int getCount() {
        return ListTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return ListTab[position];
    }
}
