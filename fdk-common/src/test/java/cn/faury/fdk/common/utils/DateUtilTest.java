package cn.faury.fdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DateUtilTest {

    @Test
    public void parse() throws Exception {
        Assert.assertTrue("fail: parse with null", null == DateUtil.parse(null, DateUtil.FORMAT_DATE));
        Date date = DateUtil.parse("2018-01-02 03:04:05", DateUtil.FORMAT_DATE_TIME);
        Assert.assertNotNull("fail: parse return null", date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 2, 3, 4, 5);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertTrue("fail: parse return value error", date.getTime() == calendar.getTime().getTime());
    }

    @Test
    public void parse1() throws Exception {
        Assert.assertTrue("fail: parse with null", null == DateUtil.parse(null));
        Date date = DateUtil.parse("2018-01-02");
        Assert.assertNotNull("fail: parse return null", date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 2, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertTrue("fail: parse return value error", date.getTime() == calendar.getTime().getTime());
    }

    @Test
    public void format() throws Exception {
        Assert.assertTrue("fail: parse with null", null == DateUtil.format((Date) null, DateUtil.FORMAT_DATE));
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 2, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertTrue("fail:string not equal", "2018-01-02".equals(DateUtil.format(calendar.getTime(), DateUtil.FORMAT_DATE)));
    }

    @Test
    public void formatDate() throws Exception {
        Assert.assertTrue("fail: parse with null", null == DateUtil.formatDate((Date) null));
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 2, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertTrue("fail:string not equal", "2018-01-02".equals(DateUtil.formatDate(calendar.getTime())));
    }

    @Test
    public void formatDateTime() throws Exception {
        Assert.assertTrue("fail: parse with null", null == DateUtil.formatDateTime((Date) null));
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 2, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertTrue("fail:string not equal", "2018-01-02 00:00:00".equals(DateUtil.formatDateTime(calendar.getTime())));
    }

    @Test
    public void getCurrentDate() throws Exception {
        Assert.assertNotNull("fail:return null", null != DateUtil.getCurrentDate());
    }

    @Test
    public void getCurrentDateStr() throws Exception {
        Assert.assertNotNull("fail:return null", null != DateUtil.getCurrentDateStr());
    }

    @Test
    public void getCurrentDateStr1() throws Exception {
        Assert.assertNotNull("fail:return null", null != DateUtil.getCurrentDateStr(DateUtil.FORMAT_DATE_TIME));
    }

    @Test
    public void getCurrentDateTimeStr() throws Exception {
        Assert.assertNotNull("fail:return null", null != DateUtil.getCurrentDateTimeStr());
    }

    @Test
    public void getCurrentTimestamp() throws Exception {
        Assert.assertNotNull("fail:return null", null != DateUtil.getCurrentTimestamp());
    }

    @Test
    public void descTime() throws Exception {
        long ms = (24 * 3600 + 2 * 3600 + 3 * 60 + 4) * 1000 + 5;
        Assert.assertTrue("fail: return desc not equal", "1天2小时3分4秒".equals(DateUtil.descDateTime(ms)));
        Assert.assertTrue("fail: return desc not equal", "99毫秒".equals(DateUtil.descDateTime(99)));
    }

    @Test
    public void toCalendar() throws Exception {
        Calendar date = Calendar.getInstance();
        date.set(2018, Calendar.JANUARY, 2, 3, 4, 5);
        date.set(Calendar.MILLISECOND, 6);
        Calendar calendar = DateUtil.toCalendar(date.getTime());
        Assert.assertArrayEquals("fail: return value error",new int[]{0,2,3,4,5,6},new int[]{
                calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)
                ,calendar.get(Calendar.HOUR_OF_DAY)
                ,calendar.get(Calendar.MINUTE)
                ,calendar.get(Calendar.SECOND)
                ,calendar.get(Calendar.MILLISECOND)
        });
    }

    @Test
    public void isDateClose() throws Exception {
        Calendar date = Calendar.getInstance();
        date.set(2018, Calendar.JANUARY, 2, 3, 4, 5);
        date.set(Calendar.MILLISECOND, 6);
        Date date1 = date.getTime();
        date.set(Calendar.MILLISECOND, 3000);
        Date date2 = date.getTime();
        Assert.assertTrue("fail: date is close",false == DateUtil.isDateClose(date1,date2,1));
        Assert.assertTrue("fail: date is not close",true==DateUtil.isDateClose(date1,date2,10));
    }
}