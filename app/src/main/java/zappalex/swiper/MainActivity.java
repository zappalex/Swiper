package zappalex.swiper;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zappalex.swiper.utils.FormatManager;
import zappalex.swiper.utils.SharedPreferencesManager;
import zappalex.swiper.utils.ValueResources;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.total_balance_view)
    TextView balanceView;
    @Bind(R.id.value_input)
    EditText valueInput;



    private Context mContext;
    private AppWidgetManager mAppWidgetManager;
    private RemoteViews mRemoteViews;
    private ComponentName mComponentName;
    private SharedPreferencesManager mSharedPreferences;
    private FormatManager mFormatManager;

    private double mTotalBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContext = this;
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.balance_widget);
        mComponentName = new ComponentName(mContext, BalanceWidget.class);
        mFormatManager = new FormatManager().getInstance();

        mSharedPreferences = SharedPreferencesManager.getInstance(mContext);
        mTotalBalance = mSharedPreferences.getSharedPrefDbl(ValueResources.BALANCE_SHARED_PREF, ValueResources.DEFAULT_BALANCE_VAL);

        updateBalances(mFormatManager.formatBalance(mTotalBalance));

    }


    @OnClick(R.id.exit_btn)
    public void onExit(){
        finish();
    }


    @OnClick(R.id.expense_btn)
    public void onExpense(){

        if(!valueInput.getText().equals(null) && !
                valueInput.getText().toString().isEmpty()  &&
                Double.parseDouble(valueInput.getText().toString()) < ValueResources.MAX_VAL){

            mTotalBalance = decreaseBalance(mTotalBalance, Double.parseDouble(valueInput.getText().toString()));
            mSharedPreferences.setSharedPrefDbl(ValueResources.BALANCE_SHARED_PREF, mTotalBalance);
            updateBalances(mFormatManager.formatBalance(mTotalBalance));

            valueInput.setText("");
        }
    }

    @OnClick(R.id.reset_btn)
    public void onReset(){
        if(!valueInput.getText().equals(null) &&
                !valueInput.getText().toString().isEmpty() &&
                Double.parseDouble(valueInput.getText().toString()) < ValueResources.MAX_VAL){

            mTotalBalance = Double.parseDouble(valueInput.getText().toString());
            mSharedPreferences.setSharedPrefDbl(ValueResources.BALANCE_SHARED_PREF, mTotalBalance);

            updateBalances(mFormatManager.formatBalance(mTotalBalance));
            valueInput.setText("");
        }
    }

    protected Double decreaseBalance(Double total, Double expense){
        return total - expense;
    }


    protected void updateBalances(String valueText){
        updateBalanceView(valueText);
        updateWidgetBalance(valueText);
    }

    protected void updateBalanceView(String valueText){

        if(mTotalBalance < 0){
            balanceView.setTextColor(getResources().getColor(R.color.red));
        }else{
            balanceView.setTextColor(getResources().getColor(R.color.green));
        }
        balanceView.setText(valueText);
    }

    protected void updateWidgetBalance(String valueText){
        mRemoteViews.setTextViewText(R.id.total_balance_view, valueText);
        mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);

    }



}
