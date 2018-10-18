package club.nsuer.nsuer;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class GradeValueChart implements IValueFormatter {

    private DecimalFormat mFormat;

    public GradeValueChart() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {


        String val = Utils.getGpaGrade(value);


        return val + " " + " \n "; // e.g. append a dollar-sign
    }
}