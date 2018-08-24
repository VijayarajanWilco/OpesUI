package com.wilco.opesui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.wilco.opesui.financeplan_modules.LifeStyleDept;
import com.wilco.opesui.mapmyplan.CustomGridViewActivity;


public class FinanceFragment extends Fragment {

    int position;
    private TextView textView, foundation;
    private Toolbar toolbar;

    GridView androidGridView;
    public static final String ARG_SECTION_NUMBER = "section_number";


    String[] gridViewString = {
            "Lifestyle dept", "Foundation Goals", "Home and savin", "Retirement and investment", "Emergency fund",
            "protection", "Super"

//            "Website", "Profile", "WordPress",
//            "Alram", "Android", "Mobile", "Website", "Profile", "WordPress",
//            "Alram", "Android", "Mobile", "Website", "Profile", "WordPress",

    };
    String[] financeBroking = {"Discover Loan option", "Bying a property", "Refinance your home loan",
            "free property report", "Home loan explained", "Get Inspired"};

    int[] gridViewImageId = {
            R.drawable.lifecycledept, R.drawable.superimg, R.drawable.savehome, R.drawable.retirement,
            R.drawable.emergency, R.drawable.protection, R.drawable.superimg

//            R.drawable.facebook,
//            R.drawable.mortage, R.drawable.people,
//            R.drawable.emergency, R.drawable.savehome, R.drawable.linkedin, R.drawable.facebook,
//            R.drawable.people, R.drawable.mortage,
//            R.drawable.gmail,
//            R.drawable.linkedin, R.drawable.twitter, R.drawable.people,
//            R.drawable.facebook, R.drawable.twitter,

    };

    int[] financeBrok = {
            R.drawable.loans, R.drawable.protection, R.drawable.finance, R.drawable.property,
            R.drawable.savehome, R.drawable.superimg

    };


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        FinanceFragment tabFragment = new FinanceFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Financial sector");
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//
//        toolbar.setBackgroundColor(Color.parseColor("#000080"));
//
//        toolbar.setTitle("Financial Sector");
//        toolbar.setTextAlignment(Gravity.CENTER);


        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1: {

                rootView = inflater.inflate(R.layout.financeplan_tab, container, false);

                CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity
                        (getActivity(), gridViewString, gridViewImageId);
                androidGridView = (GridView) rootView.findViewById(R.id.grid_view_image_text);
                androidGridView.setAdapter(adapterViewAndroid);
                androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int i, long id) {
                        Toast.makeText(getActivity(), "GridView Item: " + gridViewString[+i],
                                Toast.LENGTH_LONG).show();

                        if (i == 0) {

                            Intent intent = new Intent(getActivity(), LifeStyleDept.class) ;
                            startActivity(intent);

                        }
                    }
                });

                break;
            }
            case 2: {
                rootView = inflater.inflate(R.layout.financebroking_tab, container, false);

                CustomGridViewActivity adapterViewFinance = new CustomGridViewActivity
                        (getActivity(), financeBroking, financeBrok);
                androidGridView = (GridView) rootView.findViewById(R.id.grid_view_image_text);
                androidGridView.setAdapter(adapterViewFinance);

                break;
            }
        }
        return rootView;
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_finance, container, false);

    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        textView = (TextView) view.findViewById(R.id.textView);
//    }
}
