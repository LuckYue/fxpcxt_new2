package com.example.a1917.fxpcxt_new;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1917.fxpcxt_new.adapter.UserFragmentAdapter;
import com.example.a1917.fxpcxt_new.fragment.CompanyFragment;
import com.example.a1917.fxpcxt_new.fragment.DangerFragment;
import com.example.a1917.fxpcxt_new.fragment.MyFragment;
import com.example.a1917.fxpcxt_new.fragment.RoleFragment;
import com.example.a1917.fxpcxt_new.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class UserMenuActivity extends AppCompatActivity {

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
    /**
     * 重置所有文本和图片
     */
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
    }
}
