package org.example.ecommercesystem.Service;

import org.example.ecommercesystem.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {

    ArrayList<Merchant> merchants = new ArrayList<>();

    public ArrayList<Merchant> getMerchants() {
        return merchants;
    }

    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    public boolean updateMerchant(String ID, Merchant merchant) {
        for(Merchant m: merchants) {
            if (ID.equals(m.getID())) {
                merchants.set(merchants.indexOf(m), merchant);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchant(String ID) {
        for(Merchant m: merchants) {
            if (ID.equals(m.getID())) {
                merchants.remove(m);
                return true;
            }
        }
        return false;
    }

}
