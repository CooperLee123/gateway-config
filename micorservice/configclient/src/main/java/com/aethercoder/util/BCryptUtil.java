package com.aethercoder.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Created by hepengfei on 09/03/2018.
 */
public class BCryptUtil {

    public static String encodeBCrypt(String passwordToHash, String salt) {
        String hashed = BCrypt.hashpw(passwordToHash, BCrypt.gensalt());
        return hashed;
    }

    public static boolean checkMatch(String text, String encrypted) {
//        String hashed = BCrypt.hashpw(text, BCrypt.gensalt());
        return BCrypt.checkpw(text, encrypted);
    }



    public static void main(String[] args) {
// Hash a password for the first time
        String password = "testpassword1";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashed);
// gensalt's log_rounds parameter determines the complexity
// the work factor is 2**log_rounds, and the default is 10
//        String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt(12));

// Check that an unencrypted password matches one that has
// previously been hashed
//        String candidate = "testpassword";
//String candidate = "wrongtestpassword";
        boolean a = checkMatch("1523331388847unvhRYweIIDklsY_8pM+se%$http://qbao_api/s/gt&/*w+cWO@CMGp{|aq1=mHRmq1259eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NDM2MjU3MSIsImV4cCI6MTUyMzQzOTcyNX0.kd5IghcwLd5AH0TIDhRyVXAs63-ONa0ZfGArCNQ2Vuc1Utl880wYVCiaLB0qJ461o4VwGFZCAsR1-LJLTkYA8A", "$2a$10$Xl64hN8J/woAxJWTrSXy8uF1HsCeVS2hYthS1gpu7SmTn9wkqDdqq");
        System.out.println(a);
        if (BCrypt.checkpw(password, hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }
}
