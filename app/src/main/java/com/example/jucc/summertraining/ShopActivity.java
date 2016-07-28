package com.example.jucc.summertraining;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
   private ShopFragment Fish0,Fish1,Fish2,Fish3,Fish4,Fish5;
    private List<Fragment>  list =new ArrayList<>();
    public List<Fish > fishList=null;
    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private Button back;
    private DatabaseMethod method;
    private TextView coins;
    public String[] description={"sdf","sdf","sdfsdf","sdfsfsddfsdf","sdfsdfsfsf","sdfsfsdfsdf"};
    public static int times =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        method=DatabaseMethod.getInstance(this);
        findById();
        newFragmentAndFillList();
        fishList=method.getAllSpecies();
        fragmentAdapter=new FragmentAdapter(this.getSupportFragmentManager(),list);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);


    }
    private void findById(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back:
                        finish();
                        break;
                }
            }
        });
        coins= (TextView) findViewById(R.id.coins);
    }
    protected void init(ShopFragment shopFragment){
        times=times%6;
        Fish fish =fishList.get(times);
        shopFragment.setResources(fish.getName(),description[times],String.valueOf(fish.getPrice()),fish.getId(),times);
        coins.setText("金币:"+method.getCoins());
        times++;
    }


    protected void newFragmentAndFillList(){
        Fish0=new ShopFragment();
        Fish1=new ShopFragment();
        Fish2=new ShopFragment();
        Fish3=new ShopFragment();
        Fish4=new ShopFragment();
        Fish5=new ShopFragment();
        list.add(Fish0);
        list.add(Fish1);
        list.add(Fish2);
        list.add(Fish3);
        list.add(Fish4);
        list.add(Fish5);
    }



}
