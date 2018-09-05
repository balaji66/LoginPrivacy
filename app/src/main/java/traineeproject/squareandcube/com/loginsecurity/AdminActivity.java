package traineeproject.squareandcube.com.loginsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mButtonLogin;
    TextInputEditText mId;
    TextInputEditText mPassword;
    ScrollView mScrollView;
    CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        initViews();
        initListeners();
        if (!new AdminPrefManager(this).isUserLogOut()) {
            startHomeActivity();
        }
    }

    private void initViews() {
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mButtonLogin = (Button) findViewById(R.id.buttonLoginAdmin);
        mId = (TextInputEditText) findViewById(R.id.textInputEditTextId);
        mPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
    }

    private void initListeners() {
        mButtonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLoginAdmin:
                String id = mId.getText().toString();
                String password = mPassword.getText().toString();
                String password1 = "password";
                String mId1 = "admin";
                if (id.equals("")) {
                    mId.setError("Id is Mandatory");
                } else if (password.equals("")) {
                    mPassword.setError("Password is Mandatory");
                } else if (id.equals(mId1) && password.equals(password1)) {
                    //startHomeActivity();
                    attemptLogin();
                } else {
                    Snackbar.make(mScrollView, "Email or Password not Matching", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void startHomeActivity() {
        Intent intentAdminLogin = new Intent(AdminActivity.this, AllRegisteredUsersActivity.class);
        startActivity(intentAdminLogin);
        finish();
    }

    private void attemptLogin() {
        String email = mId.getText().toString();
        String password = mPassword.getText().toString();
        if (mCheckBox.isChecked()) {
            saveLoginDetails(email, password);
            startHomeActivity();
        } else {
            startHomeActivity();
        }

    }

    private void saveLoginDetails(String email, String password) {
        new AdminPrefManager(this).saveLoginDetails(email, password);
    }

}
