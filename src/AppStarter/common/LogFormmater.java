package AppStarter.common;


import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Set the Format of the logger
 *
 */
public class LogFormmater extends Formatter {
    @Override
    public String format(LogRecord record) {
        Calendar cal = Calendar.getInstance();
        String str = "";

        // get date
        cal.setTimeInMillis(record.getMillis());
        str += String.format("%02d%02d%02d-%02d:%02d:%02d ",
                cal.get(Calendar.YEAR) - 2000,
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND));

        // level of the log
        str += "[" + record.getSourceClassName() + "] -- ";

        // message of the log
        str += record.getMessage();
        return str + "\n";
    }
}
