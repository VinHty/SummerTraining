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
import android.widget.Button;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFinishedJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFinishedJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFinishedJobFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Map<String, Object>> mData;
    private MyAdapter adapter;
    private Button button_listSuccessfulJob;
    private Button button_listFailJob;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListFinishedJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFinishedJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFinishedJobFragment newInstance(String param1, String param2) {
        ListFinishedJobFragment fragment = new ListFinishedJobFragment();
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

        View view = inflater.inflate(R.layout.fragment_list_finished_job, container, false);

        button_listFailJob = (Button)view.findViewById(R.id.activity_list_finished_job_failed);
        button_listSuccessfulJob = (Button)view.findViewById(R.id.activity_list_finished_job_successful);

        //列出所有成功完成的任务的监听事件
        button_listSuccessfulJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = getData(true);
                adapter = new MyAdapter(getContext());
                setListAdapter(adapter);
            }
        });

        //列出所有失败任务的监听事件
        button_listFailJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = getData(false);
                adapter = new MyAdapter(getContext());
                setListAdapter(adapter);

            }
        });

        //当用户刚进入查看已经完成任务界面时，默认显示已经成功完成的任务
        mData = getData(true);
        adapter = new MyAdapter(getContext());
        setListAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
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
            //throw new RuntimeException(context.toString()
             //       + " must implement OnFragmentInteractionListener");
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

    //从数据库里获得相应数据
    private List<Map<String, Object>> getData(Boolean isSuc) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseMethod dbMethod = DatabaseMethod.getInstance(getContext());
        List<Job> myJobs = dbMethod.getFinishJob(isSuc);  //从数据库读取完成的任务,以list的形式
        Log.e("myJobs size is","" + myJobs.size());
        for(int i = 0; i < myJobs.size(); i++){
            Map<String,Object> map = new HashMap<String,Object>();
            Log.e("job_name",myJobs.get(i).getTitle());
            Log.e("startTime",myJobs.get(i).getBeginTime());
            map.put("job_name",myJobs.get(i).getTitle());
            map.put("startTime",myJobs.get(i).getBeginTime());
            list.add(map);
        }
        return list;
    }

    //listView每个item的布局class
    public final class ViewHolder{
        public TextView job_name;
        public TextView startTime;
    }

    //用来存储数据的adapter
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        //构造函数
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        //listView的行数
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
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

        //每行的view
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.activity_list_finished_job_listview,null);
                holder.job_name = (TextView)convertView.findViewById(R.id.activity_list_finished_job_listview_jobname);
                holder.startTime = (TextView)convertView.findViewById(R.id.activity_list_finished_job_dialog_starttime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            //设置每行的文本信息
            holder.job_name.setText((String)mData.get(position).get("job_name"));
            holder.startTime.setText("开始时间:" + (String)mData.get(position).get("startTime"));
            return convertView;
        }
    }

}
