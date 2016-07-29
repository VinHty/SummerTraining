package com.example.jucc.summertraining;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vin on 2016/7/23.
 */

public class ShopFragment extends Fragment implements View.OnClickListener {
    //鱼名，鱼描述，鱼的价格
    private TextView title, description, price;
    //鱼的图片资源
    private ImageView img;
    //购买按钮
    private Button purchase;
    //数据库对象
    private DatabaseMethod method;

    //获取speciesID
    public int getSpeciesID() {
        return getArguments().getInt("key");
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_shop_activity, container, false);
        //实例化各种控件
        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        price = (TextView) view.findViewById(R.id.price);
        img = (ImageView) view.findViewById(R.id.img);
        purchase = (Button) view.findViewById(R.id.purchase);
        method = DatabaseMethod.getInstance(getContext());
        purchase.setOnClickListener(this);
        //调用容器activity的初始化方法，初始化本fragment
        ((ShopActivity) getActivity()).init(this);
        return view;
    }

    //资源文件初始化，分别设定名字，描述，价格，图片ID
    public void setResources(String title, String description, String price, int id) {
        this.title.setText(title);
        this.description.setText(description);
        this.img.setImageResource(id);
        this.price.setText("价格:" + price);
        //如果已拥有，则设定为拥有
        if (checkIfHave() != 0) {
            purchase.setText("已拥有");
            purchase.setOnClickListener(null);
        }

    }

    //获取到price里的价格对应的字符串
    public String getPrice() {
        return this.price.getText().toString().split(":")[1];
    }


    @Override
    public void onClick(View v) {
        int coins = Integer.parseInt(method.getCoins());
        int price = Integer.parseInt(getPrice());
        if (coins >= price) {
            //金币足够 则购买鱼
            Fish fish = createFish();
            method.buyFish(fish);
            new AlertDialog.Builder(getActivity()).setTitle("提示")//设置对话框标题
                    .setMessage("购买成功")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                        @Override

                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                        }

                    }).show();//在按键响应事件中显示此对话框
            //更改文字和listener 避免重复点击
            coins = coins - price;
            this.purchase.setText("已拥有");
            this.purchase.setOnClickListener(null);
            ((ShopActivity) getActivity()).getCoins().setText("金币:" + coins);
        } else new AlertDialog.Builder(getActivity()).setTitle("提示")//设置对话框标题
                .setMessage("您的金币不足")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                    }

                }).show();//在按键响应事件中显示此对话框
    }

/*
/创建鱼对象，设定其种类和价格
 */

    public Fish createFish() {
        int price = Integer.parseInt(getPrice());
        return new Fish(getSpeciesID(), price);
    }

    //检查是否拥有该类的鱼
    public int checkIfHave() {
        return method.isAvailable(getSpeciesID());
    }

}
