package com.sourav.service;

import com.sourav.domain.WithdrawlStatus;
import com.sourav.modal.User;
import com.sourav.modal.Withdrawl;
import com.sourav.repository.WithdrawlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawlServiceImpl implements WithdrawlService {

    @Autowired
    private WithdrawlRepository withdrawlRepository;

    @Override
    public Withdrawl requestWithdrawl(Long amount, User user) {
        Withdrawl withdrawl = new Withdrawl();
        withdrawl.setAmount(amount);
        withdrawl.setUser(user);
        withdrawl.setStatus(WithdrawlStatus.PENDING);
        return withdrawlRepository.save(withdrawl);
    }

    @Override
    public Withdrawl procedWithdrawl(Long withdrawlId, boolean accept) throws Exception {
        Optional<Withdrawl> withdrawl = withdrawlRepository.findById(withdrawlId);
        if(withdrawl.isEmpty()){
            throw new Exception("Withdrawl not found");
        }
        Withdrawl withdrawl1 = withdrawl.get();
        withdrawl1.setDate(LocalDateTime.now());
        if(accept){
            withdrawl1.setStatus(WithdrawlStatus.SUCCESS);
        }else{
            withdrawl1.setStatus(WithdrawlStatus.PENDING);
        }
        return withdrawlRepository.save(withdrawl1);
    }

    @Override
    public List<Withdrawl> getUserWithdrawlHistory(User user) {
        return withdrawlRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawl> getAllWithdrawlRequest() {
        return withdrawlRepository.findAll();
    }
}
