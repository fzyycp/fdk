package cn.faury.fdk.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class KdniaoTrackUtilTest {
    @Test
    public void getOrderTracesByJson() throws Exception {
        KdniaoTrackUtil kdniaoTrackUtil = new KdniaoTrackUtil("1287687","f5a22c62-9b9a-4576-90ba-54c3b8e5a4ac");
        System.out.println(kdniaoTrackUtil.getOrderTracesByJson("YD","3950240569359"));
    }

}