package org.example.ecommercesystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    ArrayList<User> users = new ArrayList<>();
    private String OTP = "";
    private int randomSpot = 0;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean updateUser(String ID, User user) {
        for(User u: users) {
            if (ID.equals(u.getID())) {
                users.set(users.indexOf(u), user);
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String ID) {
        for(User u: users) {
            if (ID.equals(u.getID())) {
                users.remove(u);
                return true;
            }
        }
        return false;
    }

    public User getUserByID(String userID) {
        for (User u: users) {
            if (userID.equals(u.getID())){
                return u;
            }
        }
        return null;
    }

    public User getUserByEmail(String email) {
        for (User u: users) {
            if (email.equals(u.getEmail())){
                return u;
            }
        }
        return null;
    }

    public String generateOTP(String email) {

        OTP = "";
        Random random = new Random();
        randomSpot = random.nextInt(6);

        if(email.equals(getUserByEmail(email).getEmail())) {
            for (int i = 0; i < 6; i++) {
                if (i == randomSpot)
                    OTP = OTP + users.indexOf(getUserByEmail(email));
                else
                    OTP = OTP + random.nextInt(10);
            }
        }
        return OTP;
    }

    public int resetPassword(String otp, String password) {
        User user = users.get(indexOfUserFromOtp());
        if (!OTP.equals(otp))
            return -1;
        else if (password.equals(user.getPassword()))
            return -2;
        else if (!isValidPassword(password))
            return -3;
        else {
            user.setPassword(password);
            return 0;
        }
    }

    public int indexOfUserFromOtp() {
        return Character.getNumericValue(OTP.charAt(randomSpot));
    }

    public boolean isValidPassword(String password) { //Todo: make password regex
        return true;
    }
    
}
