package org.ze.smartc2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements ActionBar.TabListener{
    Button  btn_bab, btn_view, btn_parking, btn_police, btn_map,btn_close_police,btn_close_hospital;
    EditText edt1;
    TextView txt_info, txt_dis;
    Location mylocation;
    String param;
    final static String u = "http://192.168.137.178/smart-city/getdata_android.php";
    String result;
    ProgressDialog pDialog;
    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main1);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

//        findview();
//        btn_bab.setOnClickListener(abc);
//        btn_view.setOnClickListener(abc);
//        btn_police.setOnClickListener(abc);
//        btn_parking.setOnClickListener(abc);
//        btn_close_hospital.setOnClickListener(abc);
//        btn_close_police.setOnClickListener(abc);

    }



    Button.OnClickListener abc = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(btn_bab)) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("選擇距離")
                        .setPositiveButton("1公里", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("table", "bab");
                                bundle.putInt("dis",1000);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("5公里", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("table", "bab");
                                bundle.putInt("dis",5000);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("10公里", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("table", "bab");
                                bundle.putInt("dis",10000);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .show();


            }
            if (v.equals(btn_view)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("選擇距離")
                        .setPositiveButton("1公里", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("table", "view");
                                bundle.putInt("dis",1000);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("5公里", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("table", "view");
                                bundle.putInt("dis",5000);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("10公里", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("table", "view");
                                bundle.putInt("dis",10000);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .show();

            }
            if (v.equals(btn_parking)) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("table", "parking");
                intent.putExtras(bundle);
                startActivity(intent);
            }
            if (v.equals(btn_police)) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("table", "police");
                intent.putExtras(bundle);
//                chechpermission();
                startActivity(intent);
            }

            if(v.equals(btn_close_police)){

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("table", "police_close");
                intent.putExtras(bundle);
                startActivity(intent);

            }
            if(v.equals(btn_close_hospital)) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("table", "hospital_close");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }



    };

    private void findview() {

        btn_bab = (Button) findViewById(R.id.btn_bab);
        btn_view = (Button) findViewById(R.id.btn_view);
        btn_parking = (Button) findViewById(R.id.btn_parking);
        btn_police = (Button) findViewById(R.id.btn_police);
        btn_close_police = (Button)findViewById(R.id.btn_close_police);
        btn_close_hospital = (Button)findViewById(R.id.btn_close_hospital);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0:
                    fragment = new Features();
                    break;
                case 1:
                    fragment = new Aboutme();
                    //fragment = new RaggaeMusicFragment();
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }
}
