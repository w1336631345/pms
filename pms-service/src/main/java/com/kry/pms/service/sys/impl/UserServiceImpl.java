package com.kry.pms.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.UserDao;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.sys.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User add(User user) {
        return userDao.saveAndFlush(user);
    }

    @Override
    public void delete(String id) {
        User user = userDao.findById(id).get();
        if (user != null) {
            user.setDeleted(Constants.DELETED_TRUE);
        }
        modify(user);
    }

    @Override
    public User modify(User user) {
        return userDao.saveAndFlush(user);
    }

    @Override
    public User findById(String id) {
        return userDao.getOne(id);
    }

    @Override
    public List<User> getAllByHotelCode(String code) {
//		return null;//默认不实现
        return userDao.findByHotelCode(code);
    }

    @Override
    public PageResponse<User> listPage(PageRequest<User> prq) {
        Example<User> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(userDao.findAll(ex, req));
    }

    @Override
    public User getAuditUser(String username, String password, String hotelCode) {
        User user = userDao.findByUsernameAndPasswordAndHotelCode(username, password, hotelCode);
        return user;
    }

    @Override
    public User findByOpenId(String openId) {
        return userDao.findByOpenIdAndStatus(openId, Constants.Status.NORMAL);
    }

    @Override
    public User findByUnionIdAndHotelCode(String openId, String hotelCode) {
        return userDao.findByUnionIdAndHotelCode(openId, hotelCode);
    }

    @Override
    public boolean bindWx(User user, String openId) {
        user = findById(user.getId());
        user.setOpenId(openId);
        modify(user);
        return true;
    }

    @Override
    public List<User> findByUnionId(String unionId) {
        return userDao.findByUnionId(unionId);
    }

    @Override
    public boolean bindWxUnionId(User user, String unionId) {
        user = findById(user.getId());
        user.setUnionId(unionId);
        modify(user);
        return true;
    }

    @Override
    public boolean queryExist(String hotelCode, String code) {
        return userDao.findTopByHotelCodeAndUsernameAndDeleted(hotelCode, code, Constants.DELETED_FALSE) != null;
    }


}
