package com.example.jucc.summertraining;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AchievementMediumFishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AchievementMediumFishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//中鱼fragment界面
public class AchievementMediumFishFragment extends ListFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Map<String, Object>> mData; //存取数据的list
    private MyAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AchievementMediumFishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementMediumFishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AchievementMediumFishFragment newInstance(String param1, String param2) {
        AchievementMediumFishFragment fragment = new AchievementMediumFishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //获得数据，绑定adapter，显示
        mData = getData();
        adapter = new MyAdapter(getContext());
        setListAdapter(adapter);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievement_medium_fish, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        //    throw new RuntimeException(context.toString()
        //            + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //获得中鱼数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseMethod dbMethod = DatabaseMethod.getInstance(getContext());
        List<Fish> myFish = dbMethod.achievementMid();
        Log.e("DB","list:    "+myFish.size());//从数据库读取中鱼的数据,以list的形式
        for(int i = 0; i < myFish.size(); i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("image",myFish.get(i).getId());
            map.put("name",myFish.get(i).getName());
            map.put("amount",myFish.get(i).getTimes());
            list.add(map);
        }
        return list;
    }

    //listview中每个item的布局class
    public final class ViewHolder{
        public ImageView leftimagerview;
        public TextView leftfishname;
        public TextView leftfishamount;
        public ImageView rightimageview;
        public TextView rightfishname;
        public TextView rightfishamount;
    }

    //存取数据的adapter
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        //adapter的构造函数
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size()/2;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        //listview中每行的view
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.fragment_achievement_medium_fish_listview,null);
                holder.leftimagerview = (ImageView) convertView.findViewById(R.id.mediumfish_leftimageview);
                holder.leftfishname = (TextView) convertView.findViewById(R.id.mediumfish_leftname);
                holder.leftfishamount = (TextView) convertView.findViewById(R.id.mediumfish_lefttextview);
                holder.rightimageview = (ImageView)convertView.findViewById(R.id.mediumfish_rightimageview);
                holder.rightfishname = (TextView)convertView.findViewById(R.id.mediumfish_rightname);
                holder.rightfishamount = (TextView)convertView.findViewById(R.id.mediumfish_righttextview);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            if(position == 0){
                //设置listview中每行的文本信息
                holder.leftimagerview.setImageResource((int)mData.get(0).get("image"));
                Log.e("image resource is ","" + mData.get(0).get("image"));
                holder.leftfishname.setText((String)mData.get(0).get("name"));
                holder.leftfishamount.setText(mData.get(0).get("amount") + "/1000");
                holder.rightimageview.setImageResource((int)mData.get(1).get("image"));
                holder.rightfishname.setText((String)mData.get(1).get("name"));
                holder.rightfishamount.setText(mData.get(1).get("amount") + "/1000");
            }else if(position == 1 || position == 2){
                //设置listview中每行的文本信息
                holder.leftimagerview.setImageResource((int)mData.get(2*position).get("image"));
                holder.leftfishname.setText((String)mData.get(2*position).get("name"));
                holder.leftfishamount.setText(mData.get(2*position).get("amount") + "/1000");
                holder.rightimageview.setImageResource((int)mData.get(2*position+1).get("image"));
                holder.rightfishname.setText((String)mData.get(2*position+1).get("name"));
                holder.rightfishamount.setText(mData.get(2*position+1).get("amount") + "/1000");
            }
            return convertView;
        }
    }
}
