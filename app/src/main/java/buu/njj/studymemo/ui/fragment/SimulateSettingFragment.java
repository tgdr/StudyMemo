package buu.njj.studymemo.ui.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import buu.njj.studymemo.IncreaseReduceTextView;
import buu.njj.studymemo.R;
import buu.njj.studymemo.StudyApplication;
import buu.njj.studymemo.bean.GridSubject;
import buu.njj.studymemo.dao.UserInfoDBhelper;

public class SimulateSettingFragment extends DialogFragment {
    private static final String TAG = "SimulateSettingFragment";
    @BindView(R.id.edit_time)
    EditText edittime;
    @BindView(R.id.txt_positive)
    TextView tvpos;
    @BindView(R.id.sp_type)
    Spinner sptype;
    @BindView(R.id.txt_nagitive)
    TextView tvnag;
    private View mRootView;
    @BindView(R.id.myIncreaseReduceTextView)
    IncreaseReduceTextView increaseReduceTextView;
    StudyApplication app;
    @BindView(R.id.sp_subject)
    Spinner spsubject;
    int number=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_simulate_setting,null);
        ButterKnife.bind(this,v);
        EventBus.getDefault().register(this);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (StudyApplication) getActivity().getApplication();
        increaseReduceTextView.setOnItemClickListener(v -> {
            Log.e("ffff", "onClick: ");
            PurchaseQuantityDialogFragment pqFragment = new PurchaseQuantityDialogFragment();
            number = increaseReduceTextView.getNumber();
            pqFragment.setInitNumber(number);
            pqFragment.setOnInputCompleteListener(new PurchaseQuantityDialogFragment
                    .OnInputCompleteListener() {
                @Override
                public void onInputComplete(String data) {
                    Log.e("FFFFFF", "onInputComplete: "+data );
                    number = Integer.parseInt(data);
                    increaseReduceTextView.setNumber(number);
                }
            });
            pqFragment.show(getFragmentManager(),
                    "DialogFragment");
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   // List<String> tablename = new ArrayList<>();
                    List<GridSubject> userStudyType = UserInfoDBhelper.getUserStudyType(app.getUserName());
//                    for(int i=0;i<userStudyType.size();i++){
//                        tablename.add(userStudyType.get(i).getMsg()+"");
//                    }
                    EventBus.getDefault().post(userStudyType);

                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick({R.id.txt_positive,R.id.txt_nagitive})
    public void myclickListener(View v){
        switch (v.getId()){
            case R.id.txt_positive:

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e(TAG, "run: numbernumber"+number);
                            List<CardData> cardDatas =SimuDBhelper.getChapteritembyType("jiandati",sptype.getSelectedItem().toString(),increaseReduceTextView.getNumber());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TabSimFragment tabSimFragment = new TabSimFragment();
                                    tabSimFragment.setOnDataSet(new TabSimFragment.onDataSet() {
                                        @Override
                                        public List<CardData> setdata() {
                                            return cardDatas;
                                        }

                                        @Override
                                        public long setTime() {
                                            return Long.parseLong(edittime.getText().toString())*60;
                                        }
                                    });
                                    getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,tabSimFragment).commit();
                                }
                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dismiss();
                    }
                }).start();*/


                break;
            case R.id.txt_nagitive:
                dismiss();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setSubjectData(List<GridSubject> gridSubjects){
        List<String> subjectname = new ArrayList<>();
        for(int i=0;i<gridSubjects.size();i++){
            subjectname.add(gridSubjects.get(i).getMsg()+"");
       }
        SpinnerAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,subjectname);
        spsubject.setAdapter(adapter);
        spsubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ArrayList<String> typelist = (ArrayList<String>) UserInfoDBhelper.getQuestionType(1);

                                EventBus.getDefault().post(typelist);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setTypes(ArrayList<String> types){
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,types);
        sptype.setAdapter(adapter);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
