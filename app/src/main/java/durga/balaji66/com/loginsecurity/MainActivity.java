package durga.balaji66.com.loginsecurity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button mLogin;
    private Button mRegister;
    private Button mAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }
    private void initViews()
    {
     mLogin=(Button)findViewById(R.id.buttonUserLogin);
     mRegister=(Button)findViewById(R.id.buttonRegister);
     mAdmin=(Button)findViewById(R.id.buttonAdmin);
    }
    private void initListeners()
    {
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mAdmin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonUserLogin:
                Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.buttonRegister:
                Intent registerIntent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.buttonAdmin:
                Intent adminIntent=new Intent(MainActivity.this,AdminActivity.class);
                startActivity(adminIntent);
                break;
        }
    }
}
