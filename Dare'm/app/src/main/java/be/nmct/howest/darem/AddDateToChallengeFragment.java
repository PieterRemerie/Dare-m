package be.nmct.howest.darem;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


public class AddDateToChallengeFragment extends Fragment {

    private DatePicker datePicker;
    String date;
    private Bundle bundle;

    public AddDateToChallengeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_date_to_challenge, container, false);
        bundle = getArguments();

        Calendar c = Calendar.getInstance();

        datePicker = (DatePicker) v.findViewById(R.id.datePicker);

        datePicker.init(c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH), new DatePicker.OnDateChangedListener(){

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth){
                Log.i("DATE", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                date = dayOfMonth + "-" + (month + 1) + "-" + year;
                showCreateChallengeFragment();
            }

        });


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void showCreateChallengeFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args = new Bundle();
        args.putString("challengeDate", date);
        if(bundle.getString("challengeName" )!= null){
            args.putString("challengeName",bundle.getString("challengeName" ));
        }
        if(bundle.getString("challengeDescr" )!= null){
            args.putString("challengeDescr",bundle.getString("challengeDescr" ));
        }
        if(bundle.getInt("categoryId" )!= 0){
            args.putInt("categoryId", bundle.getInt("categoryId" ));
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CreateChallengeFragment createChallengeFragment = new CreateChallengeFragment();
        createChallengeFragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout_in_create_challenge_activity, createChallengeFragment);
        fragmentTransaction.commit();

    }


}
