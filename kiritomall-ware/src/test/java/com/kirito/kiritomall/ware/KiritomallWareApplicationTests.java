package com.kirito.kiritomall.ware;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KiritomallWareApplicationTests {

    @Test
    public void contextLoads() {

        String salt = RandomStringUtils.randomAlphanumeric(20);
        System.out.println(salt);
        String sha256Hash = new Sha256Hash("0",salt).toHex();
        System.out.println(sha256Hash);
    }

}
