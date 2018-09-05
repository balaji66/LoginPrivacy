package traineeproject.squareandcube.com.loginsecurity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    DatePickerDialog picker;
    private static final int PICTURE_SELECTED = 1;
    private Button mEdit, mUpdate;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mMobileNo;
    private EditText mDob;
    private EditText mId;
    private DatabaseHelper databaseHelper;
    private User user;
    private DatePickerDialog datePickerDialog;
    private CircleImageView imageView;
    String emailFromPref, email, emailFromIntent;
    TextView error;
    Bitmap bitmap;
    String finalDateOfBirth = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        gettingMail();
        emailFromIntent = getIntent().getStringExtra("EMAIL");
        initViews();
        initListeners();
        falseEditText();
        initObjects();
        mDob.setKeyListener(null);
        if (emailFromPref.isEmpty()) {
            email = emailFromIntent;
        } else
            email = emailFromPref;
        getAllCandidate();

        //mEdit.setVisibility(View.VISIBLE);
        //mUpdate.setVisibility(View.GONE);
    }

    public void gettingMail() {
        emailFromPref = new UserPerfManager(this).getEmail();
    }

    private void initViews() {
        error = (TextView) findViewById(R.id.error);
        mId = (EditText) findViewById(R.id.editTexyId);
        imageView = (CircleImageView) findViewById(R.id.imageView);
        mEdit = (Button) findViewById(R.id.buttonEdit);
        mUpdate = (Button) findViewById(R.id.buttonUpdate);
        mFirstName = (EditText) findViewById(R.id.editTextFirstName);
        mLastName = (EditText) findViewById(R.id.editTextLastName);
        mEmail = (EditText) findViewById(R.id.editTextEmailId);
        mPassword = (EditText) findViewById(R.id.editTextPassword);
        mMobileNo = (EditText) findViewById(R.id.editTextPhoneNumber);
        mDob = (EditText) findViewById(R.id.editTextDateOfBirth);
    }

    private void initListeners() {
        mEdit.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        imageView.setOnClickListener(this);
        mDob.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(this);
        user = new User();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mein_menu, menu);
        return true;
    }

    public void getAllCandidate() {
        //String s = getIntent().getStringExtra("EMAIL");
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from durga WHERE candidate_email = '" + email + "'", null);
        cursor.moveToFirst();
        mId.setText(cursor.getString(0));
        mFirstName.setText(cursor.getString(1));
        mLastName.setText(cursor.getString(2));
        mDob.setText(cursor.getString(3));
        mEmail.setText(cursor.getString(4));
        mMobileNo.setText(cursor.getString(5));
        mPassword.setText(cursor.getString(6));
        byte b[] = cursor.getBlob(7);
        if (b != null) {
            Bitmap bp = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(bp);
        } else {
            imageView.setImageResource(R.drawable.ic_placeholder);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logOut:
                alertDialog();
                break;
        }
        return true;
    }

    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new UserPerfManager(getApplicationContext()).clear();
                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("Are You want to logout");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertDialogBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("Are You want to Exit");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        alertDialogBackPressed();
    }

    private void falseEditText() {
        imageView.setEnabled(false);
        mId.setEnabled(false);
        mFirstName.setEnabled(false);
        mLastName.setEnabled(false);
        mEmail.setEnabled(false);
        mPassword.setEnabled(false);
        mMobileNo.setEnabled(false);
        mDob.setEnabled(false);
    }

    private void trueEditText() {
        imageView.setEnabled(true);
        mId.setEnabled(false);
        mFirstName.setEnabled(true);
        mLastName.setEnabled(true);
        mEmail.setEnabled(false);
        mPassword.setEnabled(true);
        mMobileNo.setEnabled(true);
        mDob.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEdit:
                trueEditText();
                mEdit.setVisibility(View.GONE);
                mUpdate.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonUpdate:
                updateButton();
                //falseEditText();
                break;
            case R.id.imageView:
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                in.putExtra("crop", true);
                in.putExtra("outputX", 100);
                in.putExtra("outputY", 100);
                in.putExtra("scale", true);
                in.putExtra("return-data", true);
                startActivityForResult(in, PICTURE_SELECTED);
                break;
            case R.id.editTextDateOfBirth:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignInActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        finalDateOfBirth = mDob.getText().toString();

                    }
                }, year, month, day);
                picker.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICTURE_SELECTED:
                if (resultCode == RESULT_OK) {
                    //Toast.makeText(getApplicationContext(),requestCode,Toast.LENGTH_LONG).show();
                    Uri uri = data.getData();
                    //String[]projection={MediaStore.Images.Media.DATA};
                    //Cursor cursor=getContentResolver().query(uri,projection,null,null,null);
                    //cursor.moveToFirst();
                    //int columnIndex=cursor.getColumnIndex(projection[0]);
                    //String filepath=cursor.getString(columnIndex);
                    //cursor.close();
                    //Bitmap yourSelectedImage= BitmapFactory.decodeFile(filepath);
                    //Drawable d=new BitmapDrawable(yourSelectedImage);
                    //imageView.setBackground(d);
                    Picasso.get()
                            .load(uri)
                            .centerCrop()
                            .resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight())
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder)
                            .into(imageView);
                    //imageView.setImageDrawable(d);

                }

        }
    }


    public void updateButton() {
        String fname = mFirstName.getText().toString();
        String lname = mLastName.getText().toString();
        String password = mPassword.getText().toString();
        String dateofbirth = mDob.getText().toString();
        String phoneNo = mMobileNo.getText().toString();
        String email = mEmail.getText().toString();

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();
        error.setVisibility(View.INVISIBLE);
        mFirstName.setCursorVisible(true);
        mLastName.setCursorVisible(true);
        mEmail.setCursorVisible(true);
        mDob.setCursorVisible(true);
        mMobileNo.setCursorVisible(true);
        mPassword.setCursorVisible(true);
        /*if(finalDateOfBirth.isEmpty())
        {
            mDob.setError("date is mandatory");
        }*/
        if (imageView.getDrawable() == null) {
            mFirstName.setCursorVisible(false);
            mLastName.setCursorVisible(false);
            mEmail.setCursorVisible(false);
            mDob.setCursorVisible(false);
            mMobileNo.setCursorVisible(false);
            mPassword.setCursorVisible(false);
            //textInputLayout.setError("image is mandatory");
            Toast.makeText(getApplicationContext(), "image is mandatory", Toast.LENGTH_LONG).show();
            error.setVisibility(View.VISIBLE);
        } else if (fname.equals("") && imageView.getDrawable() != null) {
            mFirstName.setError("First Name is Mandatory");
        } else if (lname.equals("") && !fname.equals("") && imageView.getDrawable() != null) {
            mLastName.setError("Last Name is Mandatory");
        } else if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lname.equals("") && !fname.equals("") && imageView.getDrawable() != null) {
            mEmail.setError("Enter Correct Email Format");
        } else if (phoneNo.equals("") || !Patterns.PHONE.matcher(phoneNo).matches() && !email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lname.equals("") && !fname.equals("") && imageView.getDrawable() != null) {
            mMobileNo.setError(" Mobile Number is Mandatory");
        } else if (phoneNo.length() != 10 && !phoneNo.equals("") || !Patterns.PHONE.matcher(phoneNo).matches() && !email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lname.equals("") && !fname.equals("") && imageView.getDrawable() != null) {
            mMobileNo.setError("Please Enter 10 digit Mobile Number");
        } else if (dateofbirth.equals("")) {
            mDob.setError("Date of birth is mandatory");
        } else if (password.equals("")) {
            mPassword.setError("Password is Mandatory");
        } else {
            user.setId(mId.getId());
            user.setmFirstName(mFirstName.getText().toString().trim());
            user.setmLastName(mLastName.getText().toString().trim());
            user.setmDob(mDob.getText().toString().trim());
            user.setmEmailId(mEmail.getText().toString().trim());
            user.setmMobileNo(mMobileNo.getText().toString().trim());
            user.setmPassword(mPassword.getText().toString().trim());
            if (databaseHelper.updateCandidate(user, data)) {
                Toast.makeText(SignInActivity.this, "updated successfully", Toast.LENGTH_LONG).show();
                falseEditText();
                mEdit.setVisibility(View.VISIBLE);
                mUpdate.setVisibility(View.GONE);
            } else {
                Toast.makeText(SignInActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }
    }

}
