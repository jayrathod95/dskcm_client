import org.joda.time.Period;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jay_rathod on 2/26/2017.
 */
public class JodaTime {
    @Test
    public void test() throws ParseException {
        Date date = new Date();
        System.out.println(date.toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1 = dateFormat.parse("2017-02-22 21:18:49");
        System.out.println(date1.toString());

        Period period = new Period(date1.getTime(), date.getTime());
        System.out.println(period.getYears() + "," + period.getMonths() + "," + period.getDays() + "," + period.getHours() + "," + period.getMinutes());
    }
}
