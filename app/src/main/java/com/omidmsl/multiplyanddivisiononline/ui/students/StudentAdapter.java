package com.omidmsl.multiplyanddivisiononline.ui.students;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.sardari.daterangepicker.utils.PersianCalendar;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    Context context;
    private List<StudentWithTestInfo> swtil;

    public StudentAdapter(Context context, List<StudentWithTestInfo> swtil) {
        this.context = context;
        this.swtil = swtil;
    }

    @Override
    public int getItemCount() {
        return swtil.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_student, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.display(position);
    }

    public void updateData(List<StudentWithTestInfo> swtil) {
        this.swtil = swtil;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView fatherName;
        private final TextView testInfo;


        MyViewHolder(final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView_name);
            fatherName = itemView.findViewById(R.id.textView_father_name);
            testInfo = itemView.findViewById(R.id.textView_tests_summery);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putInt(Student.KEY_ID, swtil.get(getAdapterPosition()).student.getId());
                    Navigation.findNavController(v).navigate(R.id.action_StudentsFragment_to_TestsFragment , args);
                }
            });

        }


        void display(int position) {
            StudentWithTestInfo swti= swtil.get(position);
            name.setText(swti.student.getName());
            fatherName.setText("فرزند: " + swti.student.getFatherName());
            testInfo.setText("تعداد تست ها: " + swti.numOfTests + "\n" + "آخرین تست: "
                    + swti.timeOfLastTest.getPersianShortDate());
        }
    }
    public static class StudentWithTestInfo{
        public Student student;
        private int numOfTests;
        private PersianCalendar timeOfLastTest;

        public StudentWithTestInfo() {
            student = new Student();
        }

        public int getNumOfTests() {
            return numOfTests;
        }

        public void setNumOfTests(int numOfTests) {
            this.numOfTests = numOfTests;
        }

        public PersianCalendar getTimeOfLastTest() {
            return timeOfLastTest;
        }

        public void setTimeOfLastTest(PersianCalendar timeOfLastTest) {
            this.timeOfLastTest = timeOfLastTest;
        }
    }
}
