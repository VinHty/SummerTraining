package com.example.jucc.summertraining;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    /*
    /shopFragment 对象
     */
    private ShopFragment Fish0, Fish1, Fish2, Fish3, Fish4, Fish5;
    //存放fragment的list
    private List<Fragment> list = new ArrayList<>();
    //存放fish的list
    public List<Fish> fishList = null;
    //fragment的适配器
    private FragmentAdapter fragmentAdapter;
    //viewPager对象
    private ViewPager viewPager;
    //返回button对象
    private Button back;
    //数据库实例
    private DatabaseMethod method;

    //获取coins对象
    public TextView getCoins() {
        return coins;
    }

    //textview对象，显示金币
    private TextView coins;
    //每条鱼的描述
    public String[] description = {"我只是长得很蓝而已=￣ω￣=", "近海底层结群性洄游鱼类,栖息于泥质或泥沙底质的海区", "喜欢生活在平原上的暖和湖泊或水流缓慢的河川里,分布在除澳洲和南美洲外的全世界。", "主要分布于长江中、下游的中型湖泊。比较适合于静水性生活.", "温水性鱼类，适宜生长的水温为25—30℃.能适应较肥沃的水体环境", "蝴蝶鱼由于体色艳丽，深受我国观赏鱼爱好者的青睐.它们在我国沿海各地的水族馆中被大量饲养。"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        //实例化对象
        method = DatabaseMethod.getInstance(this);
        //获取各种控件
        findById();
        //new fragment 并且设定一些参数
        newFragmentAndFillList();
        //获取到鱼的list
        fishList = method.getAllSpecies();
        //实例化适配器对象
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), list);
        //绑定
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
    }

    /*
    /获得各种控件，同时给button back绑定监听器
     */
    private void findById() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back:
                        Intent intent = new Intent();
                        intent.setClass(ShopActivity.this, MainActivity.class);
                        ShopActivity.this.startActivity(intent);
                        finish();
                        break;
                }
            }
        });
        coins = (TextView) findViewById(R.id.coins);
    }

    //初始化fragment里的资源
    protected void init(ShopFragment shopFragment) {
        int id = shopFragment.getSpeciesID();
        Fish fish = fishList.get(id);
        shopFragment.setResources(fish.getName(), description[id], String.valueOf(fish.getPrice()), fish.getId());
        coins.setText("金币:"+method.getCoins());
    }

    /*
    /实例化对象，并且绑定参数0-5，区别不同种类的鱼
     */
    protected void newFragmentAndFillList() {
        Fish0 = new ShopFragment();
        Fish1 = new ShopFragment();
        Fish2 = new ShopFragment();
        Fish3 = new ShopFragment();
        Fish4 = new ShopFragment();
        Fish5 = new ShopFragment();

        Fish0.setArguments(getBundle(0));
        Fish1.setArguments(getBundle(1));
        Fish2.setArguments(getBundle(2));
        Fish3.setArguments(getBundle(3));
        Fish4.setArguments(getBundle(4));
        Fish5.setArguments(getBundle(5));
//        Fish0.setSpeciesID(0);
//        Fish1.setSpeciesID(1);
//        Fish2.setSpeciesID(2);
//        Fish3.setSpeciesID(3);
//        Fish4.setSpeciesID(4);
//        Fish5.setSpeciesID(5);
        list.add(Fish0);
        list.add(Fish1);
        list.add(Fish2);
        list.add(Fish3);
        list.add(Fish4);
        list.add(Fish5);
    }

    /*
    / 新建bundle对象来绑定参数，避免丢失
     */
    public Bundle getBundle(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("key", index);
        return bundle;
    }

}
