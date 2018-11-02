package cn.cohg.mydome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.andy.library.ChannelActivity;
import com.andy.library.ChannelBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TabLayout tabLayout;
    private Button button;
    private ViewPager pager;
    private List<Fragment> list;
    private PagerAdapter pagerAdapter;
    private final String TAB_DATA_KEY = "TabJson";
    private List<ChannelBean> tabList;
    private SharedPreferences sp;
    private List<ChannelBean> tabListAll = new ArrayList<>();
    private String jsonbeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(TAB_DATA_KEY, MODE_PRIVATE);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        pager = (ViewPager) findViewById(R.id.page);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);
        LoadData();


    }

    private void LoadData() {
        list = new ArrayList<>();
        initDat1a();
        //        创建Fragmnet对象
        String str = sp.getString(TAB_DATA_KEY, null);
        if (str == null) {
            for (int i = 0; i < tabList.size(); i++) {
                list.add(new Myfragment());
            }
        } else {
            List<ChannelBean> listAll = new Gson().fromJson(str, new TypeToken<List<ChannelBean>>() {
            }.getType());
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).isSelect()) {
                    list.add(new Myfragment());
                }
            }
        }
        //将Viewpager和Tablayout绑定
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(pagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(pager); //通过判断来个tablayout加载标题（点击按钮和未点击按钮） if(str==null){ for(int i=0;i<tabList.size();i++){ tabLayout.getTabAt(i).setText(tabList.get(i).getName()); } }else { List<ChannelBean> listAll= new Gson().fromJson(str, new TypeToken<List<ChannelBean>>() {}.getType()); for(int i = 0;i<listAll.size();i++){ if (listAll.get(i).isSelect()==true) tabLayout.getTabAt(i).setText(listAll.get(i).getName()); } }

        if (str == null) {
            for (int i = 0; i < tabList.size(); i++) {
                tabLayout.getTabAt(i).setText(tabList.get(i).getName());
            }
        } else {
            List<ChannelBean> listAll = new Gson().fromJson(str, new TypeToken<List<ChannelBean>>() {
            }.getType());
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).isSelect() == true)
                    tabLayout.getTabAt(i).setText(listAll.get(i).getName());
            }
        }


        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == ChannelActivity.REQUEST_CODE && resultCode == ChannelActivity.RESULT_CODE) {
                jsonbeans = data.getStringExtra(ChannelActivity.RESULT_JSON_KEY); //保存
                sp.edit().putString(TAB_DATA_KEY, jsonbeans).commit(); //一定要写在commit的下面
                LoadData();
                Log.e("requestCode", "requestCode=" + jsonbeans);
            }
        }

        private void initDat1a () {
            tabList = new ArrayList<>();
            tabList.add(new ChannelBean("推荐", true));
            tabList.add(new ChannelBean("热点", true));
            tabList.add(new ChannelBean("北京", true));
            tabList.add(new ChannelBean("社会", true));
            tabList.add(new ChannelBean("头条", true));
            tabList.add(new ChannelBean("看点", true));
            tabList.add(new ChannelBean("体育", true));
            tabList.add(new ChannelBean("关注", false));
            tabList.add(new ChannelBean("育儿", false));
            tabList.add(new ChannelBean("购物", false));
            tabList.add(new ChannelBean("分享", false));
            tabList.add(new ChannelBean("NBA", false));
            tabList.add(new ChannelBean("乐视", false));
        }


        @Override
        public void onClick (View v){
            LoadData();
            String string = sp.getString(TAB_DATA_KEY, null);
            if (string == null) {
                ChannelActivity.startChannelActivity(MainActivity.this, tabList);
            } else {
                ChannelActivity.startChannelActivity(MainActivity.this, string);
            }

        }

        @Override
        public void onWindowFocusChanged ( boolean hasFocus){
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus && Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }

        class PagerAdapter extends FragmentPagerAdapter {
            private List<Fragment> list;

            public PagerAdapter(FragmentManager fm, List<Fragment> list) {
                super(fm);
                this.list = list;
            }

            @Override
            public Fragment getItem(int position) { //        把位置传过去
                Bundle bundle = new Bundle();
                bundle.putInt("name", position + 1);
                list.get(position).setArguments(bundle);
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        }


}
