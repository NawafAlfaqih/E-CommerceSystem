package org.example.ecommercesystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.ecommercesystem.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
@RequiredArgsConstructor
public class UserService {

    ArrayList<User> users = new ArrayList<>();

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
}
