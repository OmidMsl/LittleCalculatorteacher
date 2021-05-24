package com.omidmsl.multiplyanddivisiononline.ui.userInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.dialog.DeleteAccountDialog;
import com.omidmsl.multiplyanddivisiononline.dialog.OfflineAlert;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.firstenter.FirstEnterActivity;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;

import static android.content.Context.MODE_PRIVATE;

public class UserInfoFragment extends Fragment {

    EditText name, cityName, schoolName;
    Button deleteAccount;
    Progress progress;
    int id, operation;
    boolean needInternet;
    Context context;
    DeleteAccountDialog dad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_info, container, false);

        name = root.findViewById(R.id.fui_editText_name);
        cityName = root.findViewById(R.id.fui_editText_cname);
        schoolName = root.findViewById(R.id.fui_editText_sname);

        deleteAccount = root.findViewById(R.id.fui_button_delete_account);

        context = getActivity();
        progress = Progress.getInstance();
        SharedPreferences sp = context.getSharedPreferences("teacherInfo", MODE_PRIVATE);
        id = sp.getInt(Teacher.KEY_ID, -2);
        setHasOptionsMenu(true);

        if (!Connection.isOnline(context)) {
            new OfflineAlert(context);
            needInternet = true;
            operation = 1;
        } else {
            progress.showProgress(context, "Loading ...", false);
            needInternet = false;
            Connection.RequestPackage rp = new Connection.RequestPackage();
            rp.setFileName("getTeacher.php");
            rp.setMethod("POST");
            rp.setParameter("id", String.valueOf(id));
            new getTeacherTask().execute(rp);
        }

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dad = new DeleteAccountDialog(context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Connection.isOnline(context)) {
                            new OfflineAlert(context);
                            needInternet = true;
                            operation = 3;
                        } else {
                            progress.showProgress(context, "Loading ...", false);
                            needInternet = false;
                            Connection.RequestPackage rp = new Connection.RequestPackage();
                            rp.setFileName("deleteTeacher.php");
                            rp.setMethod("POST");
                            rp.setParameter("id", String.valueOf(id));
                            new deleteTeacherTask().execute(rp);
                        }
                    }
                });

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        if (needInternet) {
            if (!Connection.isOnline(context))
                new OfflineAlert(context);
            else {
                if (operation == 1) {
                    progress.showProgress(context, "Loading ...", false);
                    needInternet = false;
                    Connection.RequestPackage rp = new Connection.RequestPackage();
                    rp.setFileName("getTeacher.php");
                    rp.setMethod("POST");
                    rp.setParameter("id", String.valueOf(id));
                    new getTeacherTask().execute(rp);
                }
                needInternet = false;
            }
        }
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.add("ذخیره").setIcon(R.drawable.ic_save_white_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String nstr=name.getText().toString().trim(),
                        cnstr= cityName.getText().toString().trim(),
                        snstr= schoolName.getText().toString().trim();
                if (nstr.isEmpty())
                    Snackbar.make(getView(), "باید نام خود را وارد کنید.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else if (cnstr.isEmpty())
                    Snackbar.make(getView(), "باید نام شهر را وارد کنید.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else if (snstr.isEmpty())
                    Snackbar.make(getView(), "باید نام مدرسه را وارد کنید.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else if (!Connection.isOnline(context))
                    new OfflineAlert(context);
                else {
                    progress.showProgress(context, "Loading ...", false);
                    needInternet = false;
                    Connection.RequestPackage rp = new Connection.RequestPackage();
                    rp.setFileName("editTeacherInfo.php");
                    rp.setMethod("POST");
                    rp.setParameter("id", String.valueOf(id));
                    rp.setParameter("name", nstr);
                    rp.setParameter("city", cnstr);
                    rp.setParameter("school", snstr);
                    new EditTeacherTask().execute(rp);
                }
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void updateInfo(Teacher teacher){
        name.setText(teacher.getName());
        cityName.setText(teacher.getCity());
        schoolName.setText(teacher.getSchool());
    }


    private class getTeacherTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                updateInfo(JsonParser.parseTeacher(s).get(0));
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }

    private class EditTeacherTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                Toast.makeText(context, "اطلاعات با موفقیت ذخیره شدند.", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }

    private class deleteTeacherTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                SharedPreferences sp = context.getSharedPreferences("teacherInfo", MODE_PRIVATE);
                sp.edit().clear().apply();
                dad.dismiss();
                context.startActivity(new Intent(context, FirstEnterActivity.class));
                getActivity().finish();
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }
}