package com.omidmsl.multiplyanddivisiononline.ui.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.dialog.OfflineAlert;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;
import com.omidmsl.multiplyanddivisiononline.ui.students.StudentAdapter;
import com.omidmsl.multiplyanddivisiononline.ui.students.StudentsFragment;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ChatsFragment extends Fragment {

    ChatAdapter chatAdapter;
    int teacherId;
    Context context;
    Progress progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chats, container, false);

        RecyclerView rv = root.findViewById(R.id.rv_chats);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        context = getActivity();
        progress = Progress.getInstance();

        SharedPreferences sp = context.getSharedPreferences("teacherInfo", MODE_PRIVATE);
        teacherId = sp.getInt(Teacher.KEY_ID, -2);

        chatAdapter = new ChatAdapter(context, new ArrayList<ChatAdapter.StudentWithMessageInfo>());
        rv.setAdapter(chatAdapter);

        return root;
    }

    @Override
    public void onResume() {
        if (!Connection.isOnline(context)) {
            new OfflineAlert(context);
        } else {
            progress.showProgress(context, "در حال دریافت لیست دانش آموزان ...", true);
            Connection.RequestPackage rp = new Connection.RequestPackage();
            rp.setFileName("getStudentWithMessageInfo.php");
            rp.setMethod("POST");
            rp.setParameter("id", String.valueOf(teacherId));
            new getStudentsWithMessageInfoTask().execute(rp);
        }
        super.onResume();
    }

    private void loadStudents(ArrayList<ChatAdapter.StudentWithMessageInfo> swmil) {
        chatAdapter.updateData(swmil);
    }

    private class getStudentsWithMessageInfoTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                loadStudents((ArrayList<ChatAdapter.StudentWithMessageInfo>) JsonParser.parseSWMI(s));
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }

}