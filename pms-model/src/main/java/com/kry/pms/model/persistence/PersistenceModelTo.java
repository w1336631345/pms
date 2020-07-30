package com.kry.pms.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @author Louis Lueng
 *
 */
@Getter
@Setter
@MappedSuperclass
public class PersistenceModelTo implements Serializable,Cloneable {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(64)")
	protected String id;
	@Column(updatable = false, columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
	protected LocalDateTime createDate;
	@Column(updatable = false, columnDefinition = "varchar(255) COMMENT '创建人'")
	protected String createUser;
	@Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
	protected LocalDateTime updateDate;
	@Column(columnDefinition = "varchar(255) COMMENT '修改人'")
	protected String updateUser;
	@Column(columnDefinition = "int(1) default '0' COMMENT '删除状态'")
	protected Integer deleted = 0;
	@Column(columnDefinition = "varchar(32) default 'draft' COMMENT '状态'")
	protected String status;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '企业编码'")
	protected String corporationCode;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '酒店编码'")
	protected String hotelCode;
    @Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
