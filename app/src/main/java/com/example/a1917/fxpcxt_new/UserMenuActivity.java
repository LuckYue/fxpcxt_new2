package com.example.a1917.fxpcxt_new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a1917.fxpcxt_new.adapter.UserFragmentAdapter;
import com.example.a1917.fxpcxt_new.fragment.CompanyFragment;
import com.example.a1917.fxpcxt_new.fragment.DangerFragment;
import com.example.a1917.fxpcxt_new.fragment.MyFragment;
import com.example.a1917.fxpcxt_new.fragment.RoleFragment;
import com.example.a1917.fxpcxt_new.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;



public class UserMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;

    private static RelativeLayout[] relatives;
    private static TextView[] texts;
    private static ImageView[] images;
    private static ViewPager viewPager;
    private static UserFragmentAdapter mAdapter;
    private final static int[] imagesNomal = {R.drawable.companyinfor,
            R.drawable.rolemanager,
            R.drawable.dangercheck,
            R.drawable.usermanager,
            R.drawable.me};
    private final static int[] imagesPressed = {R.drawable.companyinfor_selected,
            R.drawable.rolemanager_selected,
            R.drawable.dangercheck_selected,
            R.drawable.usermanager_selected,
            R.drawable.me_selected};
    private final static int[] relativeLayouts={R.id.companyinfor,
            R.id.rolemanager,R.id.dangercheck,R.id.usermanger};
    private final static int[] buttomText={R.id.companytext,
            R.id.roletext,R.id.dangertext,R.id.usertext};
    private final static int[] buttomInage={R.id.companyimg,
            R.id.roleimg,R.id.dangerimg,R.id.userimg};
    private static int textColorNormal = Color.parseColor("#ff64717f");
    private static int textColorPressed = Color.parseColor("#ff31363d");

    private CompanyFragment companyFragment;
    private RoleFragment roleFragment;
    private DangerFragment dangerFragment;
    private UserFragment userFragment;
    private MyFragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        initLayouts();
        setCurrentView(0);

       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        right = findViewById(R.id.right);
        left = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//             this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return left.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


   @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        //将数据传到Intent中
       Intent intent;
        if (id == R.id.nav_hazardChange) {
            intent=new Intent(UserMenuActivity.this,ChangeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recheck) {
            intent=new Intent(UserMenuActivity.this,HazardExamineActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_uploadFile) {
            intent=new Intent(UserMenuActivity.this,UploadFileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_uploadFace) {
            intent=new Intent(UserMenuActivity.this,UploadFaceActivity.class);
            startActivity(intent);
        }
        /*else if (id == R.id.electrical_safety) {
            type="电气安全";
            flag=0;
        } else if (id == R.id.fire_control) {
            type="消防";
            flag=0;
        }if (id == R.id.occupational_health1) {
           type="职业健康";
           flag=1;
       } else if (id == R.id.security_management1) {
           type="安全管理";
           flag=1;
       } else if (id == R.id.equick1) {
           type="特种设备";
           flag=1;
       } else if (id == R.id.electrical_safety1) {
           type="电气安全";
           flag=1;
       } else if (id == R.id.fire_control1) {
           type="消防";
           flag=1;
       }
       Bundle bundle=new Bundle();
       bundle.putSerializable("type",type);
       Intent intent;
       if(flag==0){
           intent=new Intent(UserMenuActivity.this,ChangeActivity.class);
       }else{
           intent=new Intent(UserMenuActivity.this,HazardExamineActivity.class);
       }
        intent.putExtras(bundle);*/
        return true;
    }
    private void clearTextsAndImages() {
        for (int i = 0; i <4; i++) {
            texts[i].setTextColor(textColorNormal);
            images[i].setImageResource(imagesNomal[i]);
        }

    }
    private void initLayouts() {
        viewPager=findViewById(R.id.viewPager);
        companyFragment=new CompanyFragment();
        roleFragment=new RoleFragment();
        dangerFragment=new DangerFragment();
        userFragment=new UserFragment();
//        myFragment=new MyFragment();
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(companyFragment);
        fragments.add(roleFragment);
        fragments.add(dangerFragment);
        fragments.add(userFragment);
//        fragments.add(myFragment);
        //加载适配器
        mAdapter = new UserFragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(mAdapter);
        relatives = new RelativeLayout[4];
        texts = new TextView[4];
        images = new ImageView[4];
        for (int i = 0; i < 4; i++) {
            relatives[i] =  findViewById(relativeLayouts[i]);
            texts[i] = findViewById(buttomText[i]);
            images[i] = findViewById(buttomInage[i]);
            relatives[i].setOnClickListener(this::onClick);
        }
    }

    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.companyinfor:
                setCurrentView(0);
                break;
            case R.id.rolemanager:
                setCurrentView(1);
                break;
            case R.id.dangercheck:
                setCurrentView(2);
                break;
            case R.id.usermanger:
                setCurrentView(3);
                break;
//            case R.id.me:
//                setCurrentView(4);
//                break;
            default:
                break;
        }

    }
    private void setCurrentView(int index) {
        viewPager.setCurrentItem(index);
        clearTextsAndImages();
        texts[index].setTextColor(textColorPressed);
        images[index].setImageResource(imagesPressed[index]);
    }
   /* private static RelativeLayout[] relatives;
    private static TextView[] texts;
    private static ImageView[] images;
    private static ViewPager viewPager;
    private static UserFragmentAdapter mAdapter;
    private final static int[] imagesNomal = {R.drawable.companyinfor,
            R.drawable.rolemanager,
            R.drawable.dangercheck,
            R.drawable.usermanager,
            R.drawable.me};
    private final static int[] imagesPressed = {R.drawable.companyinfor_selected,
            R.drawable.rolemanager_selected,
            R.drawable.dangercheck_selected,
            R.drawable.usermanager_selected,
            R.drawable.me_selected};
    private final static int[] relativeLayouts={R.id.companyinfor,
            R.id.rolemanager,R.id.dangercheck,R.id.usermanger,R.id.me};
    private final static int[] buttomText={R.id.companytext,
            R.id.roletext,R.id.dangertext,R.id.usertext,R.id.metext};
    private final static int[] buttomInage={R.id.companyimg,
            R.id.roleimg,R.id.dangerimg,R.id.userimg,R.id.meimg};
    private static int textColorNormal = Color.parseColor("#ff64717f");
    private static int textColorPressed = Color.parseColor("#ff31363d");

    private CompanyFragment companyFragment;
    private RoleFragment roleFragment;
    private DangerFragment dangerFragment;
    private UserFragment userFragment;
    private MyFragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        initLayouts();
        setCurrentView(0);
    }
    *//**
     * 重置所有文本和图片
     *//*
    private void clearTextsAndImages() {
        for (int i = 0; i < 5; i++) {
            texts[i].setTextColor(textColorNormal);
            images[i].setImageResource(imagesNomal[i]);
        }

    }
    private void initLayouts() {
        viewPager=findViewById(R.id.viewPager);
        companyFragment=new CompanyFragment();
        roleFragment=new RoleFragment();
        dangerFragment=new DangerFragment();
        userFragment=new UserFragment();
        myFragment=new MyFragment();
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(companyFragment);
        fragments.add(roleFragment);
        fragments.add(dangerFragment);
        fragments.add(userFragment);
        fragments.add(myFragment);
        //加载适配器
        mAdapter = new UserFragmentAdapter(getSupportFragmentManager(),
                fragments);
        viewPager.setAdapter(mAdapter);
        relatives = new RelativeLayout[5];
        texts = new TextView[5];
        images = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            relatives[i] =  findViewById(relativeLayouts[i]);
            texts[i] = findViewById(buttomText[i]);
            images[i] = findViewById(buttomInage[i]);
            relatives[i].setOnClickListener(this::onClick);
        }
    }

    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.companyinfor:
                setCurrentView(0);
                break;
            case R.id.rolemanager:
                setCurrentView(1);
                break;
            case R.id.dangercheck:
                setCurrentView(2);
                break;
            case R.id.usermanger:
                setCurrentView(3);
                break;
            case R.id.me:
                setCurrentView(4);
                break;
            default:
                break;
        }

    }
    private void setCurrentView(int index) {
        viewPager.setCurrentItem(index);
        clearTextsAndImages();
        texts[index].setTextColor(textColorPressed);
        images[index].setImageResource(imagesPressed[index]);
    }*/
}
