package com.example.bookstore.service;

import com.example.bookstore.entity.Discount;
import com.example.bookstore.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DiscountServiceImpl implements DiscountService {
    private DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }


    @Override
    public void save(Discount discount) {
        discountRepository.save(discount);
    }



    @Override
    public String createDiscountCode() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @Override
    public Discount findById(String id) {
        return discountRepository.findById(id).orElse(null);
    }

    @Override
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public void merge(Discount discount) {
        discountRepository.saveAndFlush(discount);
    }


}
