package com.omidmsl.multiplyanddivisiononline.firstenter;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.omidmsl.multiplyanddivisiononline.MainActivity;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;

public class FirstEnterActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_enter);

        SharedPreferences sp = this.getSharedPreferences("teacherInfo", MODE_PRIVATE);
        int sid = sp.getInt(Teacher.KEY_ID, -2);
        if (sid!=-2){
            Intent intent = new Intent(FirstEnterActivity.this, MainActivity.class);
            intent.putExtra(Teacher.KEY_ID, sid);
            startActivity(intent);
            finish();
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_1, R.id.nav_2)
                .build();


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}