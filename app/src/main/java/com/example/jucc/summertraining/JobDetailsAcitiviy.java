package com.example.jucc.summertraining;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//当用户在主界面点击任务按钮的时候，跳转至该界面，即任务界面
public class JobDetailsAcitiviy extends AppCompatActivity implements View.OnClickListener{

    private Button button_return;
    private Button button_add;

    private Button mTabListFinishedJob;
    private Button mTabListUnfinishedJob;

    private ListFinishedJobFragment mListFinishedJobFragment;
    private ListUnfinishedJobFragment mListUnFinishedJobFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details_acitiviy);

        button_return = (Button)findViewById(R.id.activity_job_details_return);
        button_add = (Button) findViewById(R.id.activity_job_details_add);
        fragmentManager = getSupportFragmentManager();

        mTabListFinishedJob = (Button)findViewById(R.id.fragment_list_finished_job_linearlayout) ;
        mTabListUnfinishedJob = (Button) findViewById(R.id.fragment_list_unfinished_job_linearlayout);
        mTabListFinishedJob.setOnClickListener(this);
        mTabListUnfinishedJob.setOnClickListener(this);

        //返回按钮的监听事件
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(JobDetailsAcitiviy.this,MainActivity.class);
                startActivity(returnIntent);
            }
        });

        //添加按钮的监听事件
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addJobActivity = new Intent(JobDetailsAcitiviy.this,AddJobActivity.class);
                startActivity(addJobActivity);
            }
        });


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        mListUnFinishedJobFragment = new ListUnfinishedJobFragment();
        transaction.add(R.id.id_content, mListUnFinishedJobFragment);
        transaction.show(mListUnFinishedJobFragment);
        transaction.commit();


    }

    @Override
    public void onClick(View v){
        switch ((v.getId()))
        {
            case R.id.fragment_list_finished_job_linearlayout:
                setTabSelection(0);
                break;
            case R.id.fragment_list_unfinished_job_linearlayout:
                setTabSelection(1);
                break;
            default:
                break;
        }

    }


    private void setTabSelection(int index)
    {
        // 重置按钮
        //resetBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index)
        {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                //((ImageButton) mTabListFinishedJob.findViewById(R.id.fragment_list_finished_job_linearlayout))
                //        .setImageResource(R.drawable.tab_weixin_pressed);
                if (mListFinishedJobFragment == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mListFinishedJobFragment = new ListFinishedJobFragment();
                    transaction.add(R.id.id_content, mListFinishedJobFragment);
                } else
                {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mListFinishedJobFragment);
                }
                break;
            case 1:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                //((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
                 //       .setImageResource(R.drawable.tab_find_frd_pressed);
                if (mListUnFinishedJobFragment == null)
                {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mListUnFinishedJobFragment = new ListUnfinishedJobFragment();
                    transaction.add(R.id.id_content, mListUnFinishedJobFragment);
                } else
                {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mListUnFinishedJobFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
  /*  private void resetBtn()
    {
        ((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
                .setImageResource(R.drawable.tab_weixin_normal);
        ((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
                .setImageResource(R.drawable.tab_find_frd_normal);
        ((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
                .setImageResource(R.drawable.tab_address_normal);
        ((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
                .setImageResource(R.drawable.tab_settings_normal);
    }
    */

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction)
    {
        if (mListFinishedJobFragment != null)
        {
            transaction.hide(mListFinishedJobFragment);
        }
        if (mListUnFinishedJobFragment != null)
        {
            transaction.hide(mListUnFinishedJobFragment);
        }
    }

}




