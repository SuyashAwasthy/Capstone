package com.techlabs.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.WithdrawalRequest;
import com.techlabs.app.repository.WithdrawalRequestRepository;

@Service
public class WithdrawalRequestServiceImpl implements WithdrawalRequestService {
	@Autowired
	private WithdrawalRequestRepository withdrawalRequestRepository;

	@Override
	public WithdrawalRequest createWithdrawalRequest(WithdrawalRequest withdrawalRequest) {
		return withdrawalRequestRepository.save(withdrawalRequest);
	}

	@Override
	public WithdrawalRequest updateWithdrawalRequest(long withdrawalRequestId, WithdrawalRequest withdrawalRequest) {
		if (withdrawalRequestRepository.existsById(withdrawalRequestId)) {
			withdrawalRequest.setWithdrawalRequestId(withdrawalRequestId);
			return withdrawalRequestRepository.save(withdrawalRequest);
		}
		throw new IllegalArgumentException("WithdrawalRequest not found");
	}

	@Override
	public void deleteWithdrawalRequest(long withdrawalRequestId) {
		withdrawalRequestRepository.deleteById(withdrawalRequestId);
	}

	@Override
	public WithdrawalRequest getWithdrawalRequestById(long withdrawalRequestId) {
		Optional<WithdrawalRequest> optionalWithdrawalRequest = withdrawalRequestRepository
				.findById(withdrawalRequestId);
		if (optionalWithdrawalRequest.isPresent()) {
			return optionalWithdrawalRequest.get();
		}
		throw new IllegalArgumentException("WithdrawalRequest not found");
	}

	@Override
	public List<WithdrawalRequest> getAllWithdrawalRequests() {
		return withdrawalRequestRepository.findAll();
	}

	@Override
	public List<WithdrawalRequest> getWithdrawalRequestsByStatus(String status) {
		return withdrawalRequestRepository.findByStatus(status);
	}
}
