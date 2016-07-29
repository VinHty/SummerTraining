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
    private TextView title, description, price;
    private ImageView img;
    private Button purchase;
    private DatabaseMethod method;

    public int getSpeciesID() {
        return getArguments().getInt("key");
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_shop_activity, container, false);
        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        price = (TextView) view.findViewById(R.id.price);
        img = (ImageView) view.findViewById(R.id.img);
        purchase = (Button) view.findViewById(R.id.purchase);
        method = DatabaseMethod.getInstance(getContext());
        purchase.setOnClickListener(this);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getClass().getSimpleName(), "click img");
            }
        });
        ((ShopActivity) getActivity()).init(this);
        return view;
    }

    public void setResources(String title, String description, String price, int id) {
        this.title.setText(title);
        this.description.setText(description);
        this.price.setText(price);
        this.img.setImageResource(id);
        if (checkIfHave() != 0) {
            purchase.setText("已拥有");
            purchase.setOnClickListener(null);
        }
    }


    public String getPrice() {
        return this.price.getText().toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.purchase:
                if (Integer.parseInt(method.getCoins()) >= Integer.parseInt(getPrice())) {
                    Fish fish = createFish();
                    method.buyFish(fish);
                    new AlertDialog.Builder(getActivity()).setTitle("提示")//设置对话框标题
                            .setMessage("购买成功")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                                @Override

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                }

                            }).show();//在按键响应事件中显示此对话框
                } else new AlertDialog.Builder(getActivity()).setTitle("提示")//设置对话框标题
                        .setMessage("您的金币不足")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                            }

                        }).show();//在按键响应事件中显示此对话框
        }

    }

    public Fish createFish() {
        int price = Integer.parseInt(getPrice());
        return new Fish(getSpeciesID(), price);
    }

    public int checkIfHave() {
        return method.isAvailable(getSpeciesID());
    }

}
