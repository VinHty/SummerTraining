package com.example.jucc.summertraining;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by Vin on 2016/7/23.
 */

public class ShopFragment extends Fragment implements View.OnClickListener{
    private TextView title,description,price;
    private ImageView img;
    private Button purchase;
    private DatabaseMethod method;
    int speciesID;




    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_shop_activity,container,false);
        title= (TextView) view.findViewById(R.id.title);
        description= (TextView) view.findViewById(R.id.description);
        price= (TextView) view.findViewById(R.id.price);
        img= (ImageView) view.findViewById(R.id.img);
        purchase= (Button) view.findViewById(R.id.purchase);
        method= DatabaseMethod.getInstance(getContext());
        purchase.setOnClickListener(this);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getClass().getSimpleName(),"click img");
            }
        });
        return view;
    }

    public void setResources(String title,String description,int price,int id,int speciesID){
        this.title.setText(title);
        this.description.setText(description);
        this.price.setText(price);
        this.img.setImageResource(id);
        this.speciesID=speciesID;
    }


    public String getPrice(){
        return this.price.getText().toString();
    }


    @Override
    public void onClick(View v) {
                switch (v.getId()){
                    case R.id.purchase:
                        Fish fish = createFish();
                        method.buyFish(fish);

                }

        }

    public Fish createFish(){
        int price=Integer.parseInt(getPrice());
        return new Fish(speciesID,price);
    }
}
