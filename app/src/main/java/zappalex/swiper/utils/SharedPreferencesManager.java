package zappalex.swiper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {

    private static SharedPreferencesManager mSharedPreferencesManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;


    public SharedPreferencesManager(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesEditor = sharedPreferences.edit();

    }

    public static synchronized SharedPreferencesManager getInstance( Context context){

        if(mSharedPreferencesManager == null){
            mSharedPreferencesManager = new SharedPreferencesManager(context);
        }
        return mSharedPreferencesManager;
    }

    public Double getSharedPrefDbl(String key, Double defaultVal){
        return Double.longBitsToDouble(sharedPreferences.getLong(key, Double.doubleToLongBits(defaultVal)));
    }

    public void setSharedPrefDbl(String key, Double val ){

        if (val == null){
            sharedPreferencesEditor.putLong(key, Double.doubleToRawLongBits(ValueResources.DEFAULT_BALANCE_VAL));
        }else{
            sharedPreferencesEditor.putLong(key, Double.doubleToRawLongBits(val));
        }
        sharedPreferencesEditor.commit();
    }

}
