package durga.balaji66.com.loginsecurity;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPerfManager {
    Context mContext;

    UserPerfManager(Context context) {
        this.mContext = context;
    }

    public void saveLoginDetails(String email, String password) {
        SharedPreferences preferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.apply();
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }

    public boolean isUserLogOut() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public void clear() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Email");
        editor.remove("Password");
        editor.apply();
    }


}
