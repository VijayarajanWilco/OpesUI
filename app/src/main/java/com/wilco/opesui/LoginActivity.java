package com.wilco.opesui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wilco.opesui.mapmyplan.MapMyPlan;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout password_input, email_input;
    TextInputEditText email_input_edit, password_input_edit, eMail_popUp;
    TextView forget,skip;
    PopupWindow mpopup;
    ImageView facebook, linkedIn, gmail;

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-" +
                    "\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\" +
                    "x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[" +
                    "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|" +
                    "[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\" +
                    "x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        password_input_edit = (TextInputEditText) findViewById(R.id.password_input_edit);
        email_input_edit = (TextInputEditText) findViewById(R.id.email_input_edit);
        password_input = (TextInputLayout) findViewById(R.id.password_input);
        email_input = (TextInputLayout) findViewById(R.id.email_input);
        forget = (TextView) findViewById(R.id.forget);
        skip = (TextView) findViewById(R.id.skip);
        Button caption = (Button) findViewById(R.id.caption);

        facebook = (ImageView) findViewById(R.id.facebook);
        linkedIn = (ImageView) findViewById(R.id.linkedin);
        gmail = (ImageView) findViewById(R.id.gmail);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFacebookApplication(v);
            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchlinkedIn(v);

            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGmail(v);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,MapMyPlan.class);
                startActivity(intent);

            }
        });

        try {

            email_input_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkEmail(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            password_input_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkPassword();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
//                checkFieldsForEmptyValues();
                    if (s.toString().length() <= 5) {
                        checkPassword();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            caption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String eMAil = email_input_edit.getText().toString();
                    if (checkEmail(eMAil) && checkPassword()) {
                        Intent intent = new Intent(LoginActivity.this, MapMyPlan.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(v.getContext(), "Check email & Pwd", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View popUpView = getLayoutInflater().inflate(R.layout.popup_forget,
                        null); // inflating popup layout
                mpopup = new PopupWindow(popUpView, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, true); // Creation of popup

                int OFFSET_X = 120;
                int OFFSET_Y = 0;
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.setBackgroundDrawable(new BitmapDrawable());
                // Displaying the popup at the specified location, + offsets.
                //  mpopup.showAtLocation(popUpView, Gravity.BOTTOM, OFFSET_X, +OFFSET_Y);

                mpopup.showAtLocation(popUpView, Gravity.CENTER, OFFSET_X, +OFFSET_Y);

                final TextView forgetPwd = (TextView) popUpView.findViewById(R.id.forgetPassword);
                final EditText email_input_popUp = (EditText) popUpView.findViewById(R.id.eMail_popUp);
                final Button submit = (Button) popUpView.findViewById(R.id.submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String eMAilPopup = email_input_popUp.getText().toString();
                        if (EMAIL_ADDRESS_PATTERN.matcher(eMAilPopup).matches()) {
                            Toast.makeText(LoginActivity.this, "Password send to your email id", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MapMyPlan.class);
                            startActivity(intent);
                            mpopup.dismiss();
                        } else {

                            Toast.makeText(LoginActivity.this, "Please enter a valid email id", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    // Launch Facebook Application after clicking the button
    public void launchFacebookApplication(View view) {
        Intent launchFacebookApplication = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
        startActivity(launchFacebookApplication);
    }

    // Launch Google Chrome after clicking the button
    public void launchGmail(View view) {
        Intent launchGoogleChrome = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        startActivity(launchGoogleChrome);

    }

    public void launchlinkedIn(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://you"));
        final PackageManager packageManager = getApplicationContext().getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=you"));
        }
        startActivity(intent);
    }

    private boolean checkEmail(String eMAil) {

        if (eMAil.length() == 0) {
            email_input.setError("Email address is required");
            return false;
        } else if (!EMAIL_ADDRESS_PATTERN.matcher(eMAil).matches()) {
            email_input.setError("Invalid Email address");
            return false;
        } else {
            email_input.setError(null);
            return true;
        }
    }

    public boolean checkPassword() {
        if (password_input_edit.getText().toString().length() == 0) {
            password_input.setError("Password is required");
            return false;
        } else if (password_input_edit.getText().toString().length() <= 5) {
            password_input.setError("Minimum six characters required");
            return false;
        } else {
            password_input.setError(null);
            return true;
        }
    }


}