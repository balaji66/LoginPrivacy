package durga.balaji66.com.loginsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mButtonLogin;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    DatabaseHelper mDatabaseHelper;
    ScrollView mNestedScrollView;
    User mUser;
    CheckBox mcheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListeners();
        initObjects();
        if (!new UserPerfManager(this).isUserLogOut()) {
            startHomeActivity();
        }
    }

    private void initViews() {
        mcheckBox = (CheckBox) findViewById(R.id.checkBox);
        mButtonLogin = (Button) findViewById(R.id.buttonLogin);
        mEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmailId);
        mPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        mNestedScrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    private void initListeners() {
        mButtonLogin.setOnClickListener(this);
    }

    private void initObjects() {
        mDatabaseHelper = new DatabaseHelper(this);
        mUser = new User();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                verifyFromSQLite();

                break;
        }
    }

    public void verifyFromSQLite() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (email.equals("")) {
            mEmail.setError("Please Enter Email Id");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please Enter correct Email Format");
        } else if (password.equals("")) {
            mPassword.setError("Please Enter Password");
        } else if (mDatabaseHelper.checkCandidate(mEmail.getText().toString().trim(), mPassword.getText().toString().trim())) {
            attemptLoginActivity();
        } else {
            Snackbar.make(mNestedScrollView, "Email or Password not matching", Snackbar.LENGTH_LONG).show();
        }
    }

    public void attemptLoginActivity() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (mcheckBox.isChecked()) {
            saveLoginDetails(email, password);
            startHomeActivity();
        } else
            startHomeActivity();
    }

    public void saveLoginDetails(String email, String password) {
        new UserPerfManager(this).saveLoginDetails(email, password);
    }

    public void startHomeActivity() {
        Intent accountsIntent = new Intent(LoginActivity.this, SignInActivity.class);
        accountsIntent.putExtra("EMAIL", mEmail.getText().toString().trim());
        startActivity(accountsIntent);
        emptyEditText();
        finish();
    }

    private void emptyEditText() {
        mEmail.setText("");
        mPassword.setText("");
    }
}