package com.omidmsl.multiplyanddivisiononline;

import com.omidmsl.multiplyanddivisiononline.models.Message;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;
import com.omidmsl.multiplyanddivisiononline.models.Test;
import com.omidmsl.multiplyanddivisiononline.ui.chats.ChatAdapter;
import com.omidmsl.multiplyanddivisiononline.ui.students.StudentAdapter;
import com.sardari.daterangepicker.utils.PersianCalendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class JsonParser {

    public static List<Student> parseStudent(String str) {
        List<Student> studentList = new ArrayList<>();
        try {
            JSONArray jsa = new JSONArray(str);
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jsb = jsa.getJSONObject(i);
                Student student = new Student();
                student.setId(jsb.getInt("id"));
                student.setName(jsb.getString("name"));
                student.setFatherName(jsb.getString("father_name"));
                studentList.add(student);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    public static List<Teacher> parseTeacher(String str) {
        List<Teacher> teachers = new ArrayList<>();
        try {
            JSONArray jsa = new JSONArray(str);
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jsb = jsa.getJSONObject(i);
                Teacher teacher = new Teacher();
                teacher.setId(jsb.getInt("id"));
                teacher.setName(jsb.getString("name"));
                teacher.setCity(jsb.getString("city"));
                teacher.setSchool(jsb.getString("school"));
                teachers.add(teacher);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    public static int getLastTeacherId(String str) {
        int id = -2;
        try {
            JSONArray jsa = new JSONArray(str);
            JSONObject jsb = jsa.getJSONObject(jsa.length() - 1);
            id = jsb.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static List<Test> parseTest(String str) {
        List<Test> testList = new ArrayList<>();
        try {
            JSONArray jsa = new JSONArray(str);
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jsb = jsa.getJSONObject(i);
                Test test = new Test();
                test.setId(jsb.getInt("id"));
                test.setAllNum(jsb.getInt("num_of_all_questions"));
                test.setCorrectNum(jsb.getInt("num_of_correct_answers"));
                test.setMul(jsb.getInt("is_mul") == 1);
                test.setNumOfDigits(jsb.getInt("num_of_digits"));
                test.setDate(new PersianCalendar(jsb.getLong("date")));
                testList.add(test);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return testList;
    }

    public static List<StudentAdapter.StudentWithTestInfo> parseSWTI(String str) {
        List<StudentAdapter.StudentWithTestInfo> swtiList = new ArrayList<>();
        try {
            JSONArray jsa = new JSONArray(str);
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jsb = jsa.getJSONObject(i);
                StudentAdapter.StudentWithTestInfo swti = new StudentAdapter.StudentWithTestInfo();
                swti.student.setId(jsb.getInt("id"));
                swti.student.setName(jsb.getString("name"));
                swti.student.setFatherName(jsb.getString("father_name"));
                swti.setNumOfTests(jsb.getInt("num_of_tests"));
                swti.setTimeOfLastTest(new PersianCalendar(jsb.getLong("time_of_last_test")));
                swtiList.add(swti);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return swtiList;
    }

    public static List<ChatAdapter.StudentWithMessageInfo> parseSWMI(String str) {
        List<ChatAdapter.StudentWithMessageInfo> swmiList = new ArrayList<>();
        try {
            JSONArray jsa = new JSONArray(str);
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jsb = jsa.getJSONObject(i);
                ChatAdapter.StudentWithMessageInfo swmi = new ChatAdapter.StudentWithMessageInfo();
                swmi.student.setId(jsb.getInt("id"));
                swmi.student.setName(jsb.getString("name"));
                swmi.student.setFatherName(jsb.getString("father_name"));
                swmi.setNumOfNewMessages(jsb.getInt("num_of_new_messages"));
                swmi.setLastMessage(jsb.getString("last_message"));
                swmi.setFromTeacher(jsb.getInt("is_from_teacher")==1);
                swmiList.add(swmi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return swmiList;
    }

    public static List<Message> parseMessage(String str) {
        List<Message> messageList = new ArrayList<>();
        try {
            JSONArray jsa = new JSONArray(str);
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jsb = jsa.getJSONObject(i);
                Message message = new Message();
                message.setId(jsb.getInt("id"));
                message.setContent(jsb.getString("content"));
                message.setSenderId(jsb.getInt("sender_id"));
                message.setReceiverId(jsb.getInt("receiver_id"));
                message.setFromTeacher(jsb.getInt("is_from_teacher") == 1);
                message.setObserved(jsb.getInt("is_observed") == 1);
                message.setDate(new PersianCalendar(jsb.getLong("date")));
                messageList.add(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageList;
    }
}