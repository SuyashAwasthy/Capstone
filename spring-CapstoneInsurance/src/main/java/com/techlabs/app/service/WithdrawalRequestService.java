package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.entity.WithdrawalRequest;

public interface WithdrawalRequestService {
    WithdrawalRequest createWithdrawalRequest(WithdrawalRequest withdrawalRequest);
    WithdrawalRequest updateWithdrawalRequest(long withdrawalRequestId, WithdrawalRequest withdrawalRequest);
    void deleteWithdrawalRequest(long withdrawalRequestId);
    WithdrawalRequest getWithdrawalRequestById(long withdrawalRequestId);
    List<WithdrawalRequest> getAllWithdrawalRequests();
    List<WithdrawalRequest> getWithdrawalRequestsByStatus(String status);
}
