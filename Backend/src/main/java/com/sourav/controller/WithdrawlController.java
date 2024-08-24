package com.sourav.controller;

import com.sourav.modal.User;
import com.sourav.modal.Wallet;
import com.sourav.modal.Withdrawl;
import com.sourav.service.UserService;
import com.sourav.service.WalletService;
import com.sourav.service.WithdrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawlController {

    @Autowired
    private WithdrawlService withdrawlService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawlRequest(@PathVariable Long amount, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawl withdrawl = withdrawlService.requestWithdrawl(amount,user);
        walletService.addBalance(userWallet, -withdrawl.getAmount());

        //WalletTransaction walletTransaction

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdrawl/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawl(@PathVariable Long id, @PathVariable boolean accept, @RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        Withdrawl withdrawl = withdrawlService.procedWithdrawl(id,accept);

        Wallet userWallet = walletService.getUserWallet(user);

        if(!accept){
            walletService.addBalance(userWallet, withdrawl.getAmount());
        }

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);
    }

    @GetMapping("/api/withdrawl")
    public ResponseEntity<List<Withdrawl>> getWithdrawlHistory(@RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        List <Withdrawl> withdrawl = withdrawlService.getUserWithdrawlHistory(user);

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawl")
    public ResponseEntity<List<Withdrawl>> getAllWithdrawlRequest(@RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawl> withdrawl=withdrawlService.getAllWithdrawlRequest();

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);
    }
}
