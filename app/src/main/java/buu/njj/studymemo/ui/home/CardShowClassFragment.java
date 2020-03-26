package buu.njj.studymemo.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import buu.njj.studymemo.R;
import buu.njj.studymemo.adapter.RecyclerViewAdapter;
import buu.njj.studymemo.bean.CardData;
import buu.njj.studymemo.ui.DBHelper;


public class CardShowClassFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<CardData> dataList;
    //DaoWeApplication app;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      //  app = (DaoWeApplication) getActivity().getApplication();
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        recyclerView = getView().findViewById(R.id.recycler_view);
        //初始化数据集
        initData();
        //初始化recyclerView


    }





    private void initData() {

        final ArrayList timeId = new ArrayList();
        Log.e("TAG", timeId.toString());
        final ArrayList question = new ArrayList();
        final ArrayList _id = new ArrayList();
        final ArrayList answer = new ArrayList();
        final ArrayList que_type = new ArrayList();
        final ArrayList chapter = new ArrayList();
        final ArrayList status = new ArrayList();
        DBHelper helper = new DBHelper();
        try {
            ResultSet rs= helper.getdata();
            while (rs.next()){
                question.add(rs.getString("question"));
                _id.add(rs.getInt("_id")+"");
                answer.add(rs.getString("answer"));
                que_type.add(rs.getString("que_type"));
                chapter.add(rs.getString("chapter"));
                status.add(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
