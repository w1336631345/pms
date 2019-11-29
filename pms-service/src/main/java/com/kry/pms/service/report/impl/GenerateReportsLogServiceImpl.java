package com.kry.pms.service.report.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.GenerateReportsLogDao;
import com.kry.pms.model.persistence.report.GenerateReportsLog;
import com.kry.pms.service.report.GenerateReportsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GenerateReportsLogServiceImpl implements GenerateReportsLogService {

    @Autowired
    GenerateReportsLogDao generateReportsLogDao;

    @Override
    public GenerateReportsLog add(GenerateReportsLog entity) {
        GenerateReportsLog grl = generateReportsLogDao.save(entity);
        return grl;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public GenerateReportsLog modify(GenerateReportsLog generateReportsLog) {
        return null;
    }

    @Override
    public GenerateReportsLog findById(String id) {
        return null;
    }

    @Override
    public List<GenerateReportsLog> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<GenerateReportsLog> listPage(PageRequest<GenerateReportsLog> prq) {
        return null;
    }
}
