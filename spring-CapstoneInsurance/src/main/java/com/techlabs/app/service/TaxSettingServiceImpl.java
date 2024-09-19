package com.techlabs.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.TaxSetting;
import com.techlabs.app.repository.TaxSettingRepository;

@Service
public class TaxSettingServiceImpl implements TaxSettingService {
	@Autowired
	private TaxSettingRepository taxSettingRepository;

	@Override
	public TaxSetting createTaxSetting(TaxSetting taxSetting) {
		return taxSettingRepository.save(taxSetting);
	}

	@Override
	public TaxSetting updateTaxSetting(long taxId, TaxSetting taxSetting) {
		if (taxSettingRepository.existsById(taxId)) {
			taxSetting.setTaxId(taxId);
			return taxSettingRepository.save(taxSetting);
		}
		throw new IllegalArgumentException("TaxSetting not found");
	}

	@Override
	public void deleteTaxSetting(long taxId) {
		taxSettingRepository.deleteById(taxId);
	}

	@Override
	public TaxSetting getTaxSettingById(long taxId) {
		Optional<TaxSetting> optionalTaxSetting = taxSettingRepository.findById(taxId);
		if (optionalTaxSetting.isPresent()) {
			return optionalTaxSetting.get();
		}
		throw new IllegalArgumentException("TaxSetting not found");
	}

	@Override
	public List<TaxSetting> getAllTaxSettings() {
		return taxSettingRepository.findAll();
	}
}
