package com.omidmsl.multiplyanddivisiononline.ui.chats;

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

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    Context context;
    private List<StudentWithMessageInfo> swmil;

    public ChatAdapter(Context context, List<StudentWithMessageInfo> swmil) {
        this.context = context;
        this.swmil = swmil;
    }

    @Override
    public int getItemCount() {
        return swmil.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_chat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.display(position);
    }

    public void updateData(List<StudentWithMessageInfo> swmil) {
        this.swmil = swmil;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView fatherName;
        private final TextView lastMessage;
        private final TextView numOfNewMessages;


        MyViewHolder(final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView_name);
            fatherName = itemView.findViewById(R.id.textView_father_name);
            lastMessage = itemView.findViewById(R.id.textView_last_message);
            numOfNewMessages = itemView.findViewById(R.id.textView_num_of_new);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putInt(Student.KEY_ID, swmil.get(getAdapterPosition()).student.getId());
                    Navigation.findNavController(v).navigate(R.id.action_navigation_chats_to_navigation_conversation , args);
                }
            });

        }


        void display(int position) {
            StudentWithMessageInfo swmi= swmil.get(position);
            name.setText(swmi.student.getName());
            fatherName.setText("فرزند: " + swmi.student.getFatherName());
            lastMessage.setText((swmi.isFromTeacher()? "شما: " :
                    (swmi.student.getName().split(" ")[0] + " : ")) + swmi.getLastMessage());
            if (swmi.getNumOfNewMessages()==0)
                numOfNewMessages.setVisibility(View.INVISIBLE);
            else numOfNewMessages.setText(swmi.getNumOfNewMessages()>9?
                    "+" : String.valueOf(swmi.getNumOfNewMessages()));
        }
    }
    public static class StudentWithMessageInfo{
        public Student student;
        private int numOfNewMessages;
        private String lastMessage;
        private boolean isFromTeacher;

        public StudentWithMessageInfo() {
            student = new Student();
        }

        public int getNumOfNewMessages() {
            return numOfNewMessages;
        }

        public void setNumOfNewMessages(int numOfNewMessages) {
            this.numOfNewMessages = numOfNewMessages;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public boolean isFromTeacher() {
            return isFromTeacher;
        }

        public void setFromTeacher(boolean fromTeacher) {
            isFromTeacher = fromTeacher;
        }
    }
}
