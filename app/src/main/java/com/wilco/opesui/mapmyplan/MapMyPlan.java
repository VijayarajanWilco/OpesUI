package com.wilco.opesui.mapmyplan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.wilco.opesui.FinancialPlanning;
import com.wilco.opesui.LoginActivity;
import com.wilco.opesui.R;
import com.wilco.opesui.dashboard.DashBoard;
import com.wilco.opesui.mail.SendEmail;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class MapMyPlan extends AppCompatActivity implements View.OnClickListener, Serializable {

    private List<QuesAnsModel.DataResult> data = new ArrayList<QuesAnsModel.DataResult>();
    public static Bus bus;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    private Communicator communicator;
    LinearLayout linearLayout;
    Button btnTag;
    Button submit;
    public String buttonText;
    private Toolbar toolbar;
    private List<QuesAnsModel> quesAnsModelList = new ArrayList<>();
    public List<String> stringList;
    public Hashtable<String, String> ht = new Hashtable<>();
    HashMap<String,Integer> hm=new HashMap<String,Integer>();
    @SuppressLint("UseSparseArrays")
    Hashtable<Integer, String[]> hashtable=new Hashtable<>();

    ArrayList<String> getSelection = new ArrayList<>();

    SharedPreferences sharedpreferences;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceType")

    public String str;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_my_plan);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Map My Plan");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            toolbar.setTextAlignment(Gravity.CENTER);
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        communicator = new Communicator();
        usePost();
        linearLayout = (LinearLayout) findViewById(R.id.baseLayout);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                for (int x = 0; x < linearLayout.getChildCount(); x++) {

                    if (linearLayout.getChildAt(x).getTag() == "columnHeader") {
                        Log.v("TAG", "Que ==> " + linearLayout.getChildAt(x).getTag());
                        LinearLayout aa = (LinearLayout) linearLayout.getChildAt(x);
                        Log.v("TAG", "Que ==> " + aa.getChildAt(0).getTag());
                        LinearLayout bb = (LinearLayout) aa.getChildAt(1);
                        Log.v("TAG", "Options count ==> " + bb.getChildCount());
                    }
                }

                Intent intent = new Intent(MapMyPlan.this, SendEmail.class);
                ht.replace(",", "");
                intent.putExtra("selection", ht.toString());

                startActivity(intent);
                ht.clear();
                linearLayout.requestLayout();

                //linearLayout.removeAllViews();
               // linearLayout.clearChildFocus(buttonText);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.mapPlan:
                Intent intent = new Intent(MapMyPlan.this, FinancialPlanning.class);
                startActivity(intent);
                return true;

            case R.id.dashBoard:
                Intent i = new Intent(MapMyPlan.this, DashBoard.class);
                startActivity(i);
                return true;

            case R.id.logout:
                Intent in = new Intent(MapMyPlan.this, LoginActivity.class);
                startActivity(in);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void usePost() {
        communicator.loadJSON();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe
    public void onServerEvent(final ServerEvent serverEvent) throws JSONException {
        //Toast.makeText(this, "" + serverEvent.getQuesAnsModel().getData(), Toast.LENGTH_SHORT).show();

        if (serverEvent.getQuesAnsModel().getStatus() && serverEvent.getQuesAnsModel().getCode() == 200)
            if (serverEvent.getQuesAnsModel().getData() != null) {
                quesAnsModelList = serverEvent.getQuesAnsModel().getData();
                displayQuestions(quesAnsModelList.get(0));

            }
    }

    // Function to divide a by b and
    // return floor value it
    static int divide(int dividend, int divisor) {
        // Calculate sign of divisor i.e.,
        // sign will be negative only iff
        // either one of them is negative
        // otherwise it will be positive
        int sign = ((dividend < 0) ^ (divisor < 0)) ? -1 : 1;

        // Update both divisor and
        // dividend positive
        dividend = Math.abs(dividend);
        divisor = Math.abs(divisor);

        // Initialize the quotient
        int quotient = 0;

        while (dividend >= divisor) {
            dividend -= divisor;
            ++quotient;
        }

        return sign * quotient;
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent) {
        Toast.makeText(this, "Service Error" + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @SuppressLint({"NewApi", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.O)


    @Override
    public void onClick(View v) {

        try {
            str = String.valueOf(v.getTag());
            /* regex for search text */
            stringList = Arrays.asList(str.split(","));
            v = ((Button) linearLayout.findViewById(Integer.parseInt(stringList.get(1))));

            buttonText = ((Button) v).getText().toString();
            submit.setVisibility(View.INVISIBLE);

            Log.d("linearLayout", "click btnTag : " + v.getTag());
            // Log.d("linearLayout", "v btnTag : " + v.getRootView().hashCode());
            Log.d("linearLayout", "v btnTag : " + buttonText);


            for (int i = 0; i < quesAnsModelList.size(); i++) {
                final QuesAnsModel model = quesAnsModelList.get(i);
                if (model.getId().get(0).matches(stringList.get(1))) {
                    int hCode = v.getParent().getParent().hashCode();
                    boolean flag = false;
                    for (int k = 0; k < linearLayout.getChildCount(); k++) {
                        if (flag) {
                            for (int v1 = k; v1 <= linearLayout.getChildCount(); v1++) {
                                linearLayout.removeViewAt(k);
                                linearLayout.requestLayout();
                            }
                            break;
                        }

                        if (hCode == linearLayout.getChildAt(k).hashCode()) {
                            LinearLayout aa = ((LinearLayout) linearLayout.getChildAt(k));
                            aa.getChildCount(); // always 2 children's (TV , LL)
                            LinearLayout bb = (LinearLayout) aa.getChildAt(1); // 1 - for LL , 0 - TV

                            for (int b = 0; b < bb.getChildCount(); b++) {
                                if (bb.getChildAt(b).getTag() == v.getTag()) {
//                                    clicked = true;


                                    v.setBackground(getResources().getDrawable(R.drawable.button_selected));

                                } else {
                                    bb.getChildAt(b).setBackground(getResources().getDrawable(R.drawable.button_unselected));

                                }
                            }

                            flag = true;
                        } else if (v.getParent().getParent().getParent().getParent().hashCode() == linearLayout.getChildAt(k).hashCode()) {
                            LinearLayout aa = ((LinearLayout) linearLayout.getChildAt(k));
                            aa.getChildCount(); // always 2 children's (TV , LL)
                            LinearLayout bb = (LinearLayout) aa.getChildAt(1); // 1 - for LL , 0 - TV

                            for (int b = 0; b < bb.getChildCount(); b++) {
                                LinearLayout cc = (LinearLayout) bb.getChildAt(b);

                                for (int c = 0; c < cc.getChildCount(); c++) {
                                    LinearLayout dd = (LinearLayout) cc.getChildAt(c);
                                    dd.getChildCount();

                                    for (int d = 0; d < dd.getChildCount(); d++) {
                                        if (dd.getChildAt(d).getTag() == v.getTag()) {

                                            v.setBackground(getResources().getDrawable(R.drawable.button_selected));

                                        } else {
                                            dd.getChildAt(d).setBackground(getResources().getDrawable(R.drawable.button_unselected));
                                            //   v.setPressed(false);

                                        }

                                    }
                                }
                            }

                            flag = true;
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayQuestions(model);
                        }
                    });

                    break;
                }

                else {
                    if (linearLayout.getChildCount() < i && model.getQuestion().contentEquals(stringList.get(0))) {

                        int hCode = v.getParent().getParent().hashCode();
                        boolean flag = false;

                        for (int k = 0; k < linearLayout.getChildCount(); k++) {

                            if (flag) {
                                for (int v1 = k; v1 <= linearLayout.getChildCount(); v1++) {
                                    linearLayout.removeViewAt(k);
                                    linearLayout.requestLayout();
                                }
                                break;
                            }

                            if (hCode == linearLayout.getChildAt(k).hashCode()) {

                                LinearLayout aa = ((LinearLayout) linearLayout.getChildAt(k));
                                aa.getChildCount(); // always 2 children's (TV , LL)
                                LinearLayout bb = (LinearLayout) aa.getChildAt(1); // 1 - for LL , 0 - TV

                                for (int b = 0; b < bb.getChildCount(); b++) {

                                    if (bb.getChildAt(b).getTag() == v.getTag()) {
                                        v.setBackground(getResources().getDrawable(R.drawable.button_selected));

                                    } else {
                                        bb.getChildAt(b).setBackground(getResources().getDrawable(R.drawable.button_unselected));

                                    }
                                }

                                flag = true;
                            }
                            else if (v.getParent().getParent().getParent().getParent().hashCode() ==
                                    linearLayout.getChildAt(k).hashCode()) {

                                LinearLayout aa = ((LinearLayout) linearLayout.getChildAt(k));
                                aa.getChildCount(); // always 2 children's (TV , LL)
                                LinearLayout bb = (LinearLayout) aa.getChildAt(1); // 1 - for LL , 0 - TV

                                for (int b = 0; b < bb.getChildCount(); b++) {
                                    LinearLayout cc = (LinearLayout) bb.getChildAt(b);

                                    for (int c = 0; c < cc.getChildCount(); c++) {

                                        LinearLayout dd = (LinearLayout) cc.getChildAt(c);
                                        dd.getChildCount();

                                        for (int d = 0; d < dd.getChildCount(); d++) {
                                            if (dd.getChildAt(d).getTag() == v.getTag()) {

                                                v.setBackground(getResources().getDrawable(R.drawable.button_selected));

                                            } else {
                                                dd.getChildAt(d).setBackground(getResources().getDrawable(R.drawable.button_unselected));
                                                // v.setPressed(false);
                                            }
                                        }
                                    }
                                }
                                flag = true;
                            }
                            submit.setVisibility(View.VISIBLE);
                        }
                    }

                    for (int l=0; l < v.getTag().hashCode();l++) {
                        linearLayout.addView(v);
                    }

                    str = str.replaceAll(",+[0-9]+$", "");
                    String[] keys = {str};
                    String[] values = {buttonText};
                    for (int s = 0; s < keys.length; s++)
                    {
                        hashtable.put(model.getId().size(),keys);
                        ht.put(keys[s] + "\n", values[s] + "\n");

                    }


//                    hm.put(buttonText,btnTag.getId());
//                    for(Object key1:hm.keySet()){
//
//                        for(Object key2:hm.keySet()){
//                            if(!key1.toString().equals(key2.toString())){
//                                int x=hm.get(key1);
//                                int y=hm.get(key2);
//                                if(x==y){
//                                    hm.remove(key2);
//                                }
//                            }
//
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            Log.d("Exception", "e : " + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void displayQuestions(QuesAnsModel quesAnsModel) {

        if (quesAnsModel.getId().size() != 0) {
            LinearLayout columnHeader = new LinearLayout(this);
            columnHeader.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            columnHeader.setOrientation(LinearLayout.VERTICAL);
            LinearLayout rowHeader = new LinearLayout(this);
            rowHeader.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            rowHeader.setOrientation(LinearLayout.HORIZONTAL);
            rowHeader.setGravity(Gravity.CENTER);
            rowHeader.setTag("rowHeader");

            TextView quesText = new TextView(this);
            quesText.setTextColor(Color.BLACK);
            quesText.layout(5, 10, 5, 5);

            LinearLayout.LayoutParams paramTV = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            paramTV.setMargins(5, 20, 5, 20);
            quesText.setLayoutParams(paramTV);
            quesText.setTextSize(18f);
            quesText.setText(quesAnsModel.getQuestion());
            quesText.setTag(quesAnsModel.getQuestion());
            columnHeader.addView(quesText);
            columnHeader.setTag("columnHeader");

            int noOfOptions = quesAnsModel.getOptions().size();
            if (noOfOptions <= 3) {
                // one coloumn
                for (int j = 0; j < noOfOptions; j++) {

                    //  btnTag.setTextColor(Color.WHITE);
                    // btnTag.setButtonDrawable(R.drawable.null_selector);

                    btnTag = new Button(this);
                    btnTag.setId(Integer.parseInt(quesAnsModel.getOptionsId().get(j)));
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                    );
                    param.setMargins(5, 5, 5, 5);

                    btnTag.setLayoutParams(param);
                    btnTag.setOnClickListener(this);
                    btnTag.setText(quesAnsModel.getOptions().get(j));
                    btnTag.setTag(quesAnsModel.getQuestion() + "," + quesAnsModel.getOptionsId().get(j));
                    btnTag.setBackground(getResources().getDrawable(R.drawable.button_unselected));

                    rowHeader.addView(btnTag);
                }
            } else {
                // more then one
                LinearLayout columnChild = new LinearLayout(this);
                columnChild.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                columnChild.setOrientation(LinearLayout.VERTICAL);
                columnChild.setGravity(Gravity.CENTER);
                columnChild.setTag("columnChild");

                int quotient = divide(noOfOptions, 3);
                int noOfRows = noOfOptions / 3 + quotient;
                int noOfQueForColoumn = 3;
                int index = 0;

                if (noOfRows == 2 && noOfOptions == 4) {
                    noOfQueForColoumn = 2;
                }

                for (int j = 0; j < noOfRows; j++) {
                    LinearLayout rowChild = new LinearLayout(this);
                    rowChild.setLayoutParams(new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    rowChild.setOrientation(LinearLayout.HORIZONTAL);
                    rowChild.setGravity(Gravity.CENTER);
                    rowChild.setTag("rowChild");

                    for (int z = 0; z < noOfQueForColoumn; z++) {
                        if (index == noOfOptions)
                            break;

                        btnTag = new Button(this);
                        btnTag.setId(Integer.parseInt(quesAnsModel.getOptionsId().get(index)));

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f
                        );
                        param.setMargins(5, 5, 5, 5);
                        btnTag.setLayoutParams(param);
                        btnTag.setOnClickListener(this);
                        btnTag.setText(quesAnsModel.getOptions().get(index));
                        btnTag.setTag(quesAnsModel.getQuestion() + "," + quesAnsModel.getOptionsId().get(index));
                        btnTag.setBackground(getResources().getDrawable(R.drawable.button_unselected));

                        rowChild.addView(btnTag);
                        index = index + 1;
                    }

                    if (rowChild.getChildCount() != 0)
                        columnChild.addView(rowChild);
                }

                rowHeader.addView(columnChild);

            }

            columnHeader.addView(rowHeader);
            linearLayout.addView(columnHeader);
            linearLayout.setTag("" + quesAnsModel.getId().get(0));

            Log.d("linearLayout", "Tag : " + linearLayout.getTag());
            Log.d("linearLayout", "child's : " + linearLayout.getChildCount());
        }
    }
}
