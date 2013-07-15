package example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: Richard
 * Date: 13/07/13
 */
public class TimeBoundaries {

	private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args){

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.MAX_VALUE);


		System.out.println(DF.format(cal.getTime()));

		cal.add(Calendar.MILLISECOND, 1);

		System.out.println(DF.format(cal.getTime()));

		cal.setTimeInMillis(Long.MIN_VALUE);


		System.out.println(DF.format(cal.getTime()));

		cal.add(Calendar.MILLISECOND, -1);

		System.out.println(DF.format(cal.getTime()));

	}

}
