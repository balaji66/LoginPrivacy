package traineeproject.squareandcube.com.loginsecurity;

import android.content.Context;
import android.content.SharedPreferences;

public class AdminPrefManager {
    Context mContext;

    AdminPrefManager(Context context) {
        this.mContext = context;
    }

    public void saveLoginDetails(String email, String password) {
        SharedPreferences preferences = mContext.getSharedPreferences("AdminLoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AdminEmail", email);
        editor.putString("AdminPassword", password);
        editor.apply();
    }

    public boolean isUserLogOut() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("AdminLoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("AdminEmail", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("AdminPassword", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public void clear() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("AdminLoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("AdminEmail");
        editor.remove("AdminPassword");
        editor.apply();
    }
}
