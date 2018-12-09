package team25.conveniencestore.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import team25.conveniencestore.R;
import team25.conveniencestore.adapter.menuAdapter;

public class menu_tab1 extends AppCompatActivity {

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tab1);

        initView();
    }

    private void initView() {

        viewPager = findViewById(R.id.menu_tab1_viewPager);
        viewPager.setAdapter(new menuAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.menu_tab1_tab);
        tabLayout.setupWithViewPager(viewPager);

    }
}
