package zappalex.swiper;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import zappalex.swiper.BalanceWidget;
import zappalex.swiper.R;
import zappalex.swiper.utils.FormatManager;
import zappalex.swiper.utils.ValueResources;

import zappalex.swiper.utils.SharedPreferencesManager;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private TextView balanceView;
    private TextView secondaryBalanceView;
    private EditText valueInput;
    private RelativeLayout expenseButton;
    private RelativeLayout resetButton;
    private RelativeLayout submitButton;



    private Context mContext;
    private AppWidgetManager mAppWidgetManager;
    private RemoteViews mRemoteViews;
    private ComponentName mComponentName;
    private SharedPreferencesManager mSharedPreferences;
    private FormatManager mFormatManager;

    private double mTotalBalance = 0.00;
    private double mLastBalance = 0.00;
    private boolean isExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mContext = this;

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.balance_widget);
        mComponentName = new ComponentName(mContext, BalanceWidget.class);
        mFormatManager = new FormatManager().getInstance();

        mSharedPreferences = SharedPreferencesManager.getInstance(mContext);
        mTotalBalance = mSharedPreferences.getSharedPrefDbl(ValueResources.BALANCE_SHARED_PREF, ValueResources.DEFAULT_BALANCE_VAL);
        mLastBalance = mSharedPreferences.getSharedPrefDbl(ValueResources.LAST_BALANCE_SHARED_PREF, ValueResources.DEFAULT_BALANCE_VAL);
        updateBalances(mFormatManager.formatBalance(mTotalBalance), mFormatManager.formatBalance(mLastBalance));

    }

    protected void initViews(){
        balanceView = (TextView)findViewById(R.id.total_balance_view);
        secondaryBalanceView = (TextView)findViewById(R.id.last_balance_view);
        valueInput = (EditText)findViewById(R.id.value_input);
        expenseButton = (RelativeLayout)findViewById(R.id.expense_btn);
        resetButton = (RelativeLayout)findViewById(R.id.reset_btn);
        submitButton = (RelativeLayout)findViewById(R.id.submit);
    }



    @Override
    public void onResume(){
        super.onResume();
        isExpense = true;

    }


    protected void onExitClick(View view){
        finish();
    }



    protected void onExpenseClicked(View view){
        expenseButton.setBackgroundColor(getResources().getColor(R.color.blue));
        resetButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
        isExpense = true;

    }

    protected void onResetClicked(View view){
        expenseButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
        resetButton.setBackgroundColor(getResources().getColor(R.color.blue));
        isExpense = false;

    }

    protected void onSubmitClicked(View view){
        if (!valueInput.getText().equals(null) &&
                !valueInput.getText().toString().isEmpty() &&
                Double.parseDouble(valueInput.getText().toString()) < ValueResources.MAX_VAL) {

            mLastBalance = mTotalBalance;
            mSharedPreferences.setSharedPrefDbl(ValueResources.LAST_BALANCE_SHARED_PREF, mLastBalance);

            if(isExpense){
                mTotalBalance = decreaseBalance(mTotalBalance, Double.parseDouble(valueInput.getText().toString()));
            }else{
                mTotalBalance = Double.parseDouble(valueInput.getText().toString());
            }

            mSharedPreferences.setSharedPrefDbl(ValueResources.BALANCE_SHARED_PREF, mTotalBalance);
            updateBalances(mFormatManager.formatBalance(mTotalBalance), mFormatManager.formatBalance(mLastBalance));
            valueInput.setText("");
        }

    }


    protected Double decreaseBalance(Double total, Double expense){
        return total - expense;
    }


    protected void updateBalances(String valueText, String lastValueText){
        updateBalanceViews(valueText, lastValueText);
        updateWidgetBalance(valueText);
    }

    protected void updateBalanceViews(String valueText, String lastValueText){
//        balanceView.setTextColor(getResources().getColor(R.color.green));
        balanceView.setText(valueText);
        secondaryBalanceView.setText(lastValueText);
    }

    protected void updateWidgetBalance(String valueText){
        mRemoteViews.setTextViewText(R.id.total_balance_view, valueText);
        mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);

    }



}
