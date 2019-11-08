package com.kry.pms.dao;

import com.kry.pms.model.ScheduleJobModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleJobDaoRepository extends JpaRepository<ScheduleJobModel, Integer>, JpaSpecificationExecutor<ScheduleJobModel> {
    public ScheduleJobModel findByIdAndStatus(Integer id, Integer status);

    public ScheduleJobModel getById(Integer id);

    public List<ScheduleJobModel> findAllByStatus(Integer status);

    public List<ScheduleJobModel> findAll();

    public List<ScheduleJobModel> findByHotelCode(String hotelCode);

    public List<ScheduleJobModel> findByHotelCodeAndStatus(String hotelCode, Integer status);

    public List<ScheduleJobModel> findByGroupNameAndJobNameAndStatus(String groupName, String jobName, Integer status);

    public List<ScheduleJobModel> findAllByStatusInOrderByCreateDateDesc(List<Integer> statusList);

    List<ScheduleJobModel> findByGroupNameAndJobName(String groupName, String jobName);
}
