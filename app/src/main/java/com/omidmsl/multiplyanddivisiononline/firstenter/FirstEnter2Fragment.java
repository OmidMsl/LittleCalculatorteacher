package com.omidmsl.multiplyanddivisiononline.firstenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.MainActivity;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class FirstEnter2Fragment extends Fragment {

    EditText city, school;
    Context context;
    Button confirmButton;
    Progress progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_first_enter_2, container, false);

        context = getActivity();
        city = root.findViewById(R.id.ffe2_editText_city);
        school = root.findViewById(R.id.ffe2_editText_school);
        confirmButton = root.findViewById(R.id.ffe2_button_confirm);

        progress = Progress.getInstance();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String citStr = city.getText().toString().trim(),
                        schoolStr = school.getText().toString().trim();
                if (citStr.isEmpty()){
                    Snackbar.make(root, "باید نام شهر را وارد کنید",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (schoolStr.isEmpty()){
                    Snackbar.make(root, "باید نام مدرسه را وارد کنید",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (!Connection.isOnline(context)) {
                    Snackbar.make(root, "اینترنت شما متصل نیست!",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    progress.showProgress(context, "در حال ثبت نام ...", true);
                    Bundle extras = getArguments();
                    Connection.RequestPackage rp = new Connection.RequestPackage();
                    rp.setFileName("addTeacher.php");
                    rp.setMethod("POST");
                    rp.setParameter("name", extras.getString(Teacher.KEY_NAME));
                    rp.setParameter("city", citStr);
                    rp.setParameter("school", schoolStr);
                    new addTeacherTask().execute(rp);
                }
            }
        });

        return root;
    }


    private class addTeacherTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                int id = JsonParser.getLastTeacherId(s);
                SharedPreferences sp = context.getSharedPreferences("teacherInfo", MODE_PRIVATE);
                sp.edit().putInt(Teacher.KEY_ID, id).apply();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Teacher.KEY_ID, id);
                context.startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }
}