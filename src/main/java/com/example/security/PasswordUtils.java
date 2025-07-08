package com.example.security;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;

public class PasswordUtils {

   @Value("${salt.secret}")
    private static String SECRET_KEY ;

    public static String hashPassword(String username, String plainPassword) {
        String combined = username + plainPassword + SECRET_KEY; // ترکیب سه‌گانه
        return BCrypt.hashpw(combined, BCrypt.gensalt());
    }

    public static boolean checkPassword(String username, String inputPassword, String storedHash) {
        String combinedInput = username + inputPassword + SECRET_KEY;
        return BCrypt.checkpw(combinedInput, storedHash);
    }
}