package com.example.jucc.summertraining;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//成就界面
public class AchievementActivity extends FragmentActivity implements View.OnClickListener{

    private Button button_return;
    private Button button_little;
    private Button button_medium;
    private Button button_big;

    private AchievementLittleFishFragment littleFishFragment;
    private AchievementMediumFishFragment mediumFishFragment;
    private AchievementBigFishFragment bigFishFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        //一些变量的初始化
        fragmentManager = getSupportFragmentManager();

        button_return = (Button)findViewById(R.id.activity_achievement_return);
        button_little = (Button)findViewById(R.id.activity_achievement_littlefish);
        button_medium = (Button)findViewById(R.id.activity_achievement_mediumfish);
        button_big = (Button)findViewById(R.id.activity_achievement_bigfish);

        button_return.setOnClickListener(this);
        button_little.setOnClickListener(this);
        button_medium.setOnClickListener(this);
        button_big.setOnClickListener(this);

        //默认加载小鱼成就界面
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        littleFishFragment = new AchievementLittleFishFragment();
        transaction.add(R.id.activity_achievement_content, littleFishFragment);
        transaction.show(littleFishFragment);
        transaction.commit();

    }

    //点击事件
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.activity_achievement_return:
                finish();
                break;
            case R.id.activity_achievement_littlefish:
                setTabSelection(0);
                break;
            case R.id.activity_achievement_mediumfish:
                setTabSelection(1);
                break;
            case R.id.activity_achievement_bigfish:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    //设置每个按钮点击之后显示出的fragment
    private void setTabSelection(int index)
    {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index)
        {
            case 0:
                if (littleFishFragment == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    littleFishFragment = new AchievementLittleFishFragment();
                    transaction.add(R.id.activity_achievement_content, littleFishFragment);
                } else
                {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(littleFishFragment);
                }
                break;
            case 1:
                if (mediumFishFragment == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mediumFishFragment = new AchievementMediumFishFragment();
                    transaction.add(R.id.activity_achievement_content, mediumFishFragment);
                } else
                {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mediumFishFragment);
                }
                break;
            case 2:
                if (bigFishFragment == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    bigFishFragment = new AchievementBigFishFragment();
                    transaction.add(R.id.activity_achievement_content, bigFishFragment);
                } else
                {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(bigFishFragment);
                }
                break;
            default:  if (littleFishFragment == null)
            {
                // 如果MessageFragment为空，则创建一个并添加到界面上
                littleFishFragment = new AchievementLittleFishFragment();
                transaction.add(R.id.activity_achievement_content, littleFishFragment);
            } else
            {
                // 如果MessageFragment不为空，则直接将它显示出来
                transaction.show(littleFishFragment);
            }
                break;
        }
        transaction.commit();
    }

    //隐藏已有的fragment
    private void hideFragments(FragmentTransaction transaction)
    {
        if (littleFishFragment != null)
        {
            transaction.hide(littleFishFragment);
        }
        if (mediumFishFragment != null)
        {
            transaction.hide(mediumFishFragment);
        }
        if (bigFishFragment != null)
        {
            transaction.hide(bigFishFragment);
        }
    }

}
