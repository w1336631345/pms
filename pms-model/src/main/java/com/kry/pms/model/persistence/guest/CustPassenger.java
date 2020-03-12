package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 功能描述: <br>常用联系人
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/3/11 10:59
 */
@Data
@Entity
@Table(name = "t_cust_passenger")
public class CustPassenger {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    private String id;
    @Column
    private String customerId;
    @Column
    private String passengerId;
}
