package com.example.services;



import com.example.dto.OtpResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private OtpService otpService;
    @Autowired
    private ShahkarService shahkarService;
    // برای ذخیره کد تایید به صورت ساده در حافظه (مثلاً Map) یا دیتابیس
    private Map<String, String> verificationCodes = new ConcurrentHashMap<>();


//    public boolean checkPhoneExists(String Phone) {
//        return userRepository.existsByPhone(Phone);
//    }

    public boolean sendVerificationCode(String Phone) {
        String code = generateRandomCode();
        OtpResponse result = otpService.sendOtp(Phone , code);
        if (result.getSuccess()) {
            verificationCodes.put(Phone, code);
            System.out.println("Verification code for " + Phone + ": " + code);
            return true;
        }else
            return false;
    }

    public String verifyCode(String Phone, String code) {
        if (!verificationCodes.containsKey(Phone)) {

            throw new IllegalStateException("invalid verification code");
        }

//        int attempts = otpAttempts.getOrDefault(Phone, 0);
//        if (attempts >= 5) throw new IllegalStateException("تعداد تلاش‌ها بیش از حد مجاز است");

        if (!verificationCodes.get(Phone).equals(code)) {
//            otpAttempts.put(Phone, attempts + 1);
            throw new IllegalArgumentException("invalid verification code");
        }

        String token = UUID.randomUUID().toString();
//        OtpToken otpToken = new OtpToken();
//        otpToken.setToken(token);
//        otpToken.setPhone(Phone);
//        otpToken.setExpiryTime(LocalDateTime.now().plusMinutes(5));
//        otpTokenRepo.save(otpToken);

        verificationCodes.remove(Phone);
        return token;
    }

//    public void updatePassword(String Phone, String newPassword) {
//        User user = userRepository.findByPhone(Phone)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        user.setPassword(PasswordUtils.hashPassword(user.getUsername(), newPassword));
//        userRepository.save(user);
//        verificationCodes.remove(Phone);
//    }
//    public void resetPassword(String token, String newPassword) {
//        OtpToken otpToken = otpTokenRepo.findByToken(token)
//                .orElseThrow(() -> new IllegalArgumentException("invalid token"));
//
//        if (otpToken.isExpired()) {
//            throw new IllegalStateException("token expired");
//        }
//
//        User user = userRepository.findByPhone(otpToken.getPhone())
//                .orElseThrow(() -> new IllegalStateException("User not found"));
//
//        user.setPassword(PasswordUtils.hashPassword(user.getUsername(), newPassword));
//        userRepository.save(user);
//        otpTokenRepo.deleteByPhone(otpToken.getPhone());
//    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // کد 6 رقمی
        return String.valueOf(code);
    }

    public boolean adminlogin(String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return true;
        }
        return false;
    }
//    public int logincoinhandling(User user) {
//        LocalDateTime now = LocalDateTime.now();
//
//        // حتما شیء User رو مجددا از دیتابیس بگیریم تا managed باشه
//        User managedUser = userRepository.findById(user.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Optional<Logins> loginsopt = loginsRepository.findByUser(managedUser);
//        if (loginsopt.isEmpty()) {
//            Logins newLogin = new Logins();
//            newLogin.setUser(managedUser);  // حتما managedUser بگذارید
//            newLogin.setCoins(100);
//            newLogin.setLastLogin(now);
//            loginsRepository.save(newLogin);
//        } else {
//            Logins existingLogin = loginsopt.get();
//            if (existingLogin.getLastLogin().isBefore(now.minusHours(24))) {
//                existingLogin.setLastLogin(now);
//                existingLogin.setCoins(existingLogin.getCoins() + 1);
//                loginsRepository.save(existingLogin);
//            }
//        }
//
//        int coins = loginsRepository.findByUser(managedUser).get().getCoins();
//        System.out.println(managedUser.getUsername() + " coins : " + coins);
//        return coins;
//    }


//    public boolean submitregister(String token) {
//        try {
//            Optional<OtpToken> tokenOpt = otpTokenRepo.findByToken(token);
//            if (tokenOpt.isEmpty()) {
//                return false;
//            }
//            OtpToken otpToken = tokenOpt.get();
//
//            Optional<User> pendingUserOpt = pendingUserRepository.findByPhone(otpToken.getPhone());
//            if (pendingUserOpt.isEmpty()) {
//                return false;
//            }
//            User pendingUser = pendingUserOpt.get();
//            if (pendingUser.getStatus().equals(STATUS.VERIFIED))
//                return false;
//
//            pendingUser.setStatus(STATUS.VERIFIED);
//
//            pendingUserRepository.delete(pendingUser);
//
//            userRepository.save(pendingUser);
//
//            otpTokenRepo.delete(otpToken);
//
//            return true;
//
//        } catch (Exception e) {
//            System.err.println("Error in submitregister: " + e.getMessage());
//            return false;
//        }
//    }
//    @Transactional
//    public boolean updateUser(User user, Map<String, String> body) throws Exception {
//        // مقداردهی اولیه با مقادیر پیش‌فرض
//        String currentPassword = body.get("currentPassword");
//        String newPassword = body.get("newPassword");
//        String newPhone = body.getOrDefault("Phone", user.getPhone());
//        String newUsername = body.getOrDefault("Username", user.getUsername());
//        String newEmail = body.getOrDefault("Email", user.getEmail());
//
//        // بررسی و تغییر رمز عبور
//        if (newPassword != null && !newPassword.isEmpty()) {
//            if (newPassword.equals(currentPassword)) {
//                throw new IllegalStateException("New password must be different from current password");
//            }
//            if (!PasswordUtils.checkPassword(user.getUsername(), currentPassword, user.getPassword())) {
//                throw new IllegalStateException("Current password is incorrect");
//            }
//            user.setPassword(PasswordUtils.hashPassword(user.getUsername(), newPassword));
//        }
//
//        // بررسی و تغییر شماره تلفن
//        if (!newPhone.equals(user.getPhone())) {
//            if (userRepository.existsByPhone(newPhone)) {
//                throw new IllegalStateException("Phone number is already in use");
//            }
//            ShahkarResponse response = shahkarService.verify(newPhone, user.getStudent_id());
//            if (!response.getData()) {
//                throw new IllegalStateException("Phone number verification failed");
//            }
//            user.setPhone(newPhone);
//        }
//
//        if (!newUsername.equals(user.getUsername())) {
//            if (userRepository.existsByUsername(newUsername)) {
//                throw new IllegalStateException("Username is already in use");
//            }
//            user.setUsername(newUsername);
//        }
//
//        if (!newEmail.equals(user.getEmail())) {
//            if (userRepository.existsByEmail(newEmail)) {
//                throw new IllegalStateException("Email is already in use");
//            }
//            user.setEmail(newEmail);
//        }
//
//        userRepository.save(user);
//        return true;
//    }

}