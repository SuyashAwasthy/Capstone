package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.entity.TaxSetting;

public interface TaxSettingService {
    TaxSetting createTaxSetting(TaxSetting taxSetting);
    TaxSetting updateTaxSetting(long taxId, TaxSetting taxSetting);
    void deleteTaxSetting(long taxId);
    TaxSetting getTaxSettingById(long taxId);
    List<TaxSetting> getAllTaxSettings();
}
