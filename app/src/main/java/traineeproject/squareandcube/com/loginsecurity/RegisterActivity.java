package traineeproject.squareandcube.com.loginsecurity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICTURE_SELECTED = 1;
    private static final int RESULT_CROP = 400;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailId;
    private EditText mMobileNo;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mDateOfBirth;
    ImageView image;
    private CircleImageView mImageView;
    private DatabaseHelper mDatabaseHelper;
    private User mUser;
    private Button mRegister;
    private TextView mLink;
    DatePickerDialog mPicker;
    TextView mError;
    TextInputLayout mTextInputLayout;
    Bitmap mBitmap;
    String mFinalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListeners();
        initObjects();
        mDateOfBirth.setKeyListener(null);
    }

    private void initObjects() {
        mDatabaseHelper = new DatabaseHelper(this);
        mUser = new User();
    }

    private void initViews() {
        mTextInputLayout = (TextInputLayout) findViewById(R.id.textinputLayout);
        mError = (TextView) findViewById(R.id.error);
        image=(ImageView)findViewById(R.id.image);
        mRegister = (Button) findViewById(R.id.buttonRegister1);
        mLink = (TextView) findViewById(R.id.textViewLoginLink);

        mFirstName = (EditText) findViewById(R.id.editTextFirstName);
        mLastName = (EditText) findViewById(R.id.editTextLastName);
        mEmailId = (EditText) findViewById(R.id.editTextEmailId);
        mPassword = (EditText) findViewById(R.id.editTextPassword);
        mMobileNo = (EditText) findViewById(R.id.editTextPhoneNumber);
        mDateOfBirth = (EditText) findViewById(R.id.editTextDateOfBirth);
        mConfirmPassword = (EditText) findViewById(R.id.editTextReEnterPassword);
        //mImageView = (CircleImageView) findViewById(R.id.imageView);
    }

    private void initListeners() {
        mRegister.setOnClickListener(this);
        mLink.setOnClickListener(this);
        //mImageView.setOnClickListener(this);
        mDateOfBirth.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister1:
                postDataToSQLite();
                break;
            case R.id.textViewLoginLink:
                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.image:
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, PICTURE_SELECTED);
                break;
            case R.id.editTextDateOfBirth:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                mPicker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDateOfBirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mFinalDate = mDateOfBirth.getText().toString();
                    }
                }, year, month, day);
                mPicker.show();
                break;
        }
    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case RESULT_CROP:
                try {
                    Bundle extras = data.getExtras();
                    assert extras != null;
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    // Set The Bitmap Data To ImageView
                    image.setImageBitmap(selectedBitmap);
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Intent returnFromGalleryIntent = new Intent(RegisterActivity.this,RegisterActivity.class);
                    setResult(RESULT_CANCELED, returnFromGalleryIntent);
                }

            case PICTURE_SELECTED:
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                try
                {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                //return Image Path to the Main Activity
                Intent returnFromGalleryIntent = new Intent();
                returnFromGalleryIntent.putExtra("picturePath",picturePath);
                setResult(RESULT_OK,returnFromGalleryIntent);
                //finish();
                performCrop(picturePath);

                }
                catch(Exception e){
                    e.printStackTrace();
                    Intent returnFromGalleryIntent = new Intent(RegisterActivity.this,RegisterActivity.class);
                    setResult(RESULT_CANCELED, returnFromGalleryIntent);
                    //finish();
                }

                   /* try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                       // mImageView.setImageBitmap(selectedImage);
                        image.setImageBitmap(selectedImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
              /*  Picasso.get()
                        .load(uri)
                        .centerCrop()
                        .resize(mImageView.getMeasuredWidth(), mImageView.getMeasuredHeight())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(mImageView);
              */  //imageView.setImageDrawable(d);
            }
            else{
                //Log.i(TAG,"RESULT_CANCELED");
                Intent returnFromGalleryIntent = new Intent(RegisterActivity.this,RegisterActivity.class);
                setResult(RESULT_CANCELED, returnFromGalleryIntent);
                //finish();
            }

            }
        }

    public void postDataToSQLite() {
        String fName = mFirstName.getText().toString();
        String lName = mLastName.getText().toString();
        String email = mEmailId.getText().toString();
        String mobile = mMobileNo.getText().toString();
        String password = mPassword.getText().toString();
        String confirmpassword = mConfirmPassword.getText().toString();
        String date1 = mDateOfBirth.getText().toString();

        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        mBitmap = image.getDrawingCache();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        mError.setVisibility(View.INVISIBLE);
        mFirstName.setCursorVisible(true);
        mLastName.setCursorVisible(true);
        mEmailId.setCursorVisible(true);
        mDateOfBirth.setCursorVisible(true);
        mMobileNo.setCursorVisible(true);
        mPassword.setCursorVisible(true);
        mConfirmPassword.setCursorVisible(true);

        if (image.getDrawable() == null) {
            mFirstName.setCursorVisible(false);
            mLastName.setCursorVisible(false);
            mEmailId.setCursorVisible(false);
            mDateOfBirth.setCursorVisible(false);
            mMobileNo.setCursorVisible(false);
            mPassword.setCursorVisible(false);
            mConfirmPassword.setCursorVisible(false);

            //textInputLayout.setError("image is mandatory");
            Toast.makeText(getApplicationContext(), "image is mandatory", Toast.LENGTH_LONG).show();
            mError.setVisibility(View.VISIBLE);
        } else if (fName.equals("") && image.getDrawable() != null) {
            mFirstName.setError("First Name is Mandatory");
        } else if (lName.equals("") && !fName.equals("") && image.getDrawable() != null) {
            mLastName.setError("Last Name is Mandatory");
        } else if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lName.equals("") && !fName.equals("") && image.getDrawable() != null) {
            mEmailId.setError("Enter Correct Email Format");
        } else if (mobile.equals("") || !Patterns.PHONE.matcher(mobile).matches() && !email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lName.equals("") && !fName.equals("") && image.getDrawable() != null) {
            mMobileNo.setError(" Mobile Number is Mandatory");
        } else if (mobile.length() != 10 && !mobile.equals("") || !Patterns.PHONE.matcher(mobile).matches() && !email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lName.equals("") && !fName.equals("") && image.getDrawable() != null) {
            mMobileNo.setError("Please Enter 10 digit Mobile Number");
        } else if (date1.equals("") /*&& finalDate.equals("")*/) {
            mDateOfBirth.setError("Date of birth is mandatory");
        } else if (password.equals("")) {
            mPassword.setError("Password is Mandatory");
        } else if (confirmpassword.equals("") || !confirmpassword.equals(password)) {
            mConfirmPassword.setError("Password and ConfirmPassword Should Match");
        } else if (!mDatabaseHelper.checkUser(mEmailId.getText().toString().trim())) {
            mUser.setmFirstName(mFirstName.getText().toString().trim());
            mUser.setmLastName(mLastName.getText().toString().trim());
            mUser.setmEmailId(mEmailId.getText().toString().trim());
            mUser.setmPassword(mPassword.getText().toString().trim());
            mUser.setmDob(mDateOfBirth.getText().toString().trim());
            mUser.setmMobileNo(mMobileNo.getText().toString().trim());
            mUser.setImage(bytes);
            mDatabaseHelper.addUser(mUser, bytes);
            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
            intent.putExtra("EMAIL", mEmailId.getText().toString().trim());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Email Already Exists", Toast.LENGTH_LONG).show();
        }
    }
}
