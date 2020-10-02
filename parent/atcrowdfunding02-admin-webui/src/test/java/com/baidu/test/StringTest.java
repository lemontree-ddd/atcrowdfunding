package com.baidu.test;

import com.baidu.constant.CrowdConstant;
import com.baidu.util.CrowdUtil;
import org.junit.Test;

/**
 * @Author Administrator
 * @create 2020/7/15 0015 18:05
 */
public class StringTest {

    @Test
    public void testMd5() {
        String source = "123123";
        String encoded = CrowdUtil.md5(source);
        System.out.println(encoded);
    }
}
