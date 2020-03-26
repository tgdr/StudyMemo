package buu.njj.studymemo.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.IncreaseReduceTextView;
import buu.njj.studymemo.R;

public class SimulateSettingFragment extends DialogFragment {

    private View mRootView;
    @BindView(R.id.myIncreaseReduceTextView)
    IncreaseReduceTextView increaseReduceTextView;
    int number=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_simulate_setting,null);
        ButterKnife.bind(this,v);



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        increaseReduceTextView.setOnItemClickListener(new IncreaseReduceTextView.OnItemClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
}
