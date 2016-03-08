package zappalex.swiper.utils;

import java.text.DecimalFormat;

public class FormatManager {

    private static FormatManager mFormatManager;
    private DecimalFormat mDecimalFormat;

    public FormatManager(){
        mDecimalFormat = new DecimalFormat("##,###.00");
    }

    public static synchronized FormatManager getInstance(){

        if(mFormatManager ==  null){
            mFormatManager = new FormatManager();
        }
        return mFormatManager;
    }

    public String formatBalance(Double balance){

        if (balance < 0 ){
            return "-$" + mDecimalFormat.format(balance*(-1));
        }else {
            return "$" + mDecimalFormat.format(balance);
        }
    }



}