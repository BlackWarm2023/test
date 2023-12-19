package com;

import com.commen.utils.JwtUtils;
import com.sys.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description: TODO
 * @author: BlackWarm
 * @date: 2023年 09月 22日  17:52
 */
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testCreateJwt(){
        User user = new User();
        user.setUsername("lisi");
        user.setPhone("3344556677");
        String token = jwtUtils.creatToken(user);
        System.out.println(token);
    }

    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMmQ5MTRhYy1jYzg3LTQxOGQtYjkzOS04N2ViZDUyNGU0NjIiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMzM0NDU1NjY3N1wiLFwidXNlcm5hbWVcIjpcImxpc2lcIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2OTUzNzY5MTgsImV4cCI6MTY5NTM3ODcxOH0.O-ft2yo7bZC1icN61pgwRydWkRlI0mejhRoDB4jHqSE";
        Claims claims = jwtUtils.parseToken(token);
        System.out.println(claims);
    }

    @Test
    public void testParseJwt2(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMmQ5MTRhYy1jYzg3LTQxOGQtYjkzOS04N2ViZDUyNGU0NjIiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMzM0NDU1NjY3N1wiLFwidXNlcm5hbWVcIjpcImxpc2lcIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2OTUzNzY5MTgsImV4cCI6MTY5NTM3ODcxOH0.O-ft2yo7bZC1icN61pgwRydWkRlI0mejhRoDB4jHqSE";
        User user = jwtUtils.parseToken(token,User.class);
        System.out.println(user);
    }

}
