package com.wilco.opesui.mail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.wilco.opesui.FinancialPlanning;
import com.wilco.opesui.R;

import com.itextpdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class SendEmail extends AppCompatActivity implements Serializable {

    Button send;
    EditText name, phone;
    TextView selectionAns, emailTo;
    TextInputLayout nameTxt, phoneTxt;
    Context c;
    String GMail = "vijayarajang@wilcosource.com"; //replace with you GMail
    String GMailPass = "9952827545"; // replace with you GMail Password

    String to = "vijayarajan1003@gmail.com";

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-" +
                    "\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\" +
                    "x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[" +
                    "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|" +
                    "[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\" +
                    "x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    );

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        c = this;

        name = (EditText) findViewById(R.id.nameEdit);
        phone = (EditText) findViewById(R.id.phoneEdit);
        emailTo = (TextView) findViewById(R.id.emailTo);
        nameTxt = (TextInputLayout) findViewById(R.id.nameTxt);
        phoneTxt = (TextInputLayout) findViewById(R.id.phoneTxt);
        selectionAns = (TextView) findViewById(R.id.selectionAns);

        String nameTxt = name.getText().toString().trim();
        String email = emailTo.getText().toString().trim();
        String phoneNum = phone.getText().toString();

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("selection");

        getIntent().removeExtra("selection");

        if (message != null) {
            message = message.replaceAll("\\p{P}+\n", "");
            createandDisplayPdf(message);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(shareIntent);
            selectionAns.setText(message);

        }

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("createandDisplayPdf");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkName();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkName();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() <= 2) {
                    checkName();
                }
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkPhoneNum();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPhoneNum();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 9) {
                    checkPhoneNum();
                }
            }
        });
        send = (Button) findViewById(R.id.send);
        String messageText = selectionAns.getText().toString().trim() + "\n";
        String finalMessageText = messageText;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkName() && checkPhoneNum()) {

                    String str_to = to;
                    //  String str_message = selectionAns.getText().toString();
                    String str_subject = phone.getText().toString();

                    // Check if there are empty fields
                    if (!str_to.equals("") &&
                            !finalMessageText.equals("") &&
                            !str_subject.equals("")) {

                        //Check if 'To:' field is a valid email
                        if (isValidEmail(str_to)) {
                            //   et_to.setError(null);


                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    createandDisplayPdf(messageText);
                                    Toast.makeText(c, "Sending... Please wait", Toast.LENGTH_LONG).show();

                                    Intent intent1 = new Intent(SendEmail.this, FinancialPlanning.class);
                                    startActivity(intent1);
                                }
                            });
                            sendEmail(str_to, str_subject, finalMessageText);
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    // et_to.setError("Not a valid email");
                                }
                            });
                        }
                    } else {
                        Toast.makeText(c, "There are empty fields.", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    public void createandDisplayPdf(String text) {

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WilcoSource";

            File dirFile = new File(path);
            if (!dirFile.exists())
                dirFile.mkdirs();

            // Create a document
            Document document = new Document();

            File fileDest = new File(dirFile, "newFile.pdf");//System.currentTimeMillis()

            FileOutputStream fileOutputStream = new FileOutputStream(fileDest);

            PdfWriter.getInstance(document, fileOutputStream);

            //open to write a document
            document.open();


            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Wilco Source");
            document.addCreator("XYZ");

            Paragraph p1 = new Paragraph(text);
            Font paraFont = new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            document.add(p1);

            document.close();
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }/* finally {
            doc.close();
        }*/

        viewPdf("newFile.pdf", "WilcoSource");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {
//        Environment.getExternalStorageDirectory().getAbsolutePath()
        File pdfFile = new File(getExternalFilesDir(Environment.getExternalStorageDirectory().getAbsolutePath()), "/" +
                directory + "/" + file);

        if (pdfFile.exists()) {
            Uri path = Uri.fromFile(pdfFile);

            // Setting the intent for pdf reader
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = Intent.createChooser(pdfIntent, "Open File");
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(SendEmail.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private void sendEmail(final String to, final String subject, final String finalMessageText) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(GMail,
                            GMailPass);
                    sender.sendMail("Mobile Number : " + subject,
                            finalMessageText,
                            GMail,
                            to);
                    Log.w("sendEmail", "Email successfully sent!");

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, "Email successfully sent!", Toast.LENGTH_LONG).show();
//                           to.set;
//                            et_message.setText("");
//                            et_subject.setText("");
                        }
                    });

                } catch (final Exception e) {
                    Log.e("sendEmail", e.getMessage(), e);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, "Email not sent. \n\n Error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        }).start();
    }

    private boolean checkName() {
        if (name.getText().toString().length() == 0) {
            nameTxt.setError("Name is required");
            return false;
        } else if (name.getText().toString().length() <= 2) {
            nameTxt.setError("Maximum 3 characters Required");
            return false;

        } else {
            nameTxt.setError(null);
            return true;
        }
    }

    private boolean checkPhoneNum() {
        if (phone.getText().toString().length() == 0) {
            phoneTxt.setError("Phone Number is Required");
            return false;
        } else if (phone.getText().toString().length() <= 9) {
            phoneTxt.setError("Maximum 10 characters Required");
            return false;
        } else {
            phoneTxt.setError(null);
            return true;
        }
    }

    // Check if parameter 'emailAddress' is a valid email
    public final static boolean isValidEmail(CharSequence emailAddress) {
        if (emailAddress == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
        }
    }
}
