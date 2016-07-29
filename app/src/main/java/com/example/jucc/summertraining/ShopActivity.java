package com.example.jucc.summertraining;

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
    private ShopFragment Fish0, Fish1, Fish2, Fish3, Fish4, Fish5;
    private List<Fragment> list = new ArrayList<>();
    public List<Fish> fishList = null;
    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private Button back;
    private DatabaseMethod method;

    public TextView getCoins() {
        return coins;
    }

    private TextView coins;
    public String[] description = {"我只是长得很蓝而已=￣ω￣=", "近海底层结群性洄游鱼类,栖息于泥质或泥沙底质的海区", "喜欢生活在平原上的暖和湖泊或水流缓慢的河川里,分布在除澳洲和南美洲外的全世界。", "主要分布于长江中、下游的中型湖泊。比较适合于静水性生活.", "温水性鱼类，适宜生长的水温为25—30℃.能适应较肥沃的水体环境", "蝴蝶鱼由于体色艳丽，深受我国观赏鱼爱好者的青睐.它们在我国沿海各地的水族馆中被大量饲养。"};
    public static int times = 0;
    public int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        method = DatabaseMethod.getInstance(this);
        findById();
        newFragmentAndFillList();
        fishList = method.getAllSpecies();
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), list);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
    }

    private void findById() {
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
        coins = (TextView) findViewById(R.id.coins);
    }

    protected void init(ShopFragment shopFragment) {
        int id = shopFragment.getSpeciesID();
        Fish fish = fishList.get(id);
        shopFragment.setResources(fish.getName(), description[id], String.valueOf(fish.getPrice()), fish.getId());
        coins.setText("金币:" + method.getCoins());
        times++;
    }


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

    public Bundle getBundle(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("key", index);
        return bundle;
    }

}
