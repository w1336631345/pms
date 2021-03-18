package com.kry.pms.api.sys;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.base.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.model.http.response.busi.SettleInfoVo;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.sys.AccountService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/sys/account")
public class AccountController extends BaseController<Account> {
    @Autowired
    AccountService accountService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public HttpResponse<Account> add(@RequestBody Account account) {
        Account result = accountService.add(account);
        if (null == result){
            return getDefaultResponse().error("账号重复创建失败，请重试");
        }else{
            return getDefaultResponse().addData(result);
        }
    }

    @PutMapping
    public HttpResponse<Account> modify(@RequestBody Account account) {
        if ("N".equals(account.getStatus()) && 0 != account.getTotal()){  //如果要停用必须判断余额不为0，否则不允许停用
            return getDefaultResponse().error("余额为0的情况下才允许停用");
        }
        return getDefaultResponse().addData(accountService.modify(account));
    }

    @DeleteMapping
    public HttpResponse<String> delete(String id) {
        HttpResponse<String> rep = new HttpResponse<>();
        accountService.delete(id);
        return rep;
    }

    @GetMapping(path = "/personPrice")
    public HttpResponse<Double> queryRoomPrice(String id) {
        HttpResponse<Double> rep = new HttpResponse<>();
        DtoResponse<Double> response = accountService.queryRoomPrice(id);
        BeanUtils.copyProperties(response, rep);
        return rep;
    }

    @GetMapping("/settleInfo/{type}/{id}")
    public HttpResponse<SettleInfoVo> getSettleInfo(@PathVariable String type, @PathVariable String id, String extFee, String orderNum) {
        HttpResponse<SettleInfoVo> response = new HttpResponse<SettleInfoVo>();
        SettleInfoVo settleInfoVo = accountService.getSettleInfo(type, id, extFee, getCurrentHotleCode(), orderNum);
        if (settleInfoVo == null) {
            response.setCode(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            response.setMessage("无法生成结帐信息");
        } else if (!settleInfoVo.isSettlEnable()) {
            response.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            response.setMessage(settleInfoVo.getMessage());
        }
        return response.addData(settleInfoVo);
    }

    @GetMapping
    public HttpResponse<PageResponse<Account>> query(HttpServletRequest request)
            throws InstantiationException, IllegalAccessException {
        HttpResponse<PageResponse<Account>> rep = new HttpResponse<PageResponse<Account>>();
        PageRequest<Account> req = parse2PageRequest(request);
        return rep.addData(accountService.listPage(req));
    }

    /**
     * 功能描述: <br>查询包价所需的入账账户
     * 〈〉
     *
     * @Param: [request]
     * @Return: com.kry.pms.base.HttpResponse<java.util.List < com.kry.pms.model.persistence.sys.Account>>
     * @Author: huanghaibin
     * @Date: 2020/1/20 16:35
     */
    @GetMapping(path = "/getInner")
    public HttpResponse<List<Account>> list(HttpServletRequest request) {
        HttpResponse<List<Account>> rep = new HttpResponse<List<Account>>();
        return rep.addData(accountService.findByHotelCodeAndType(getCurrentHotleCode()));
    }

    @GetMapping(path = "/getById")
    public HttpResponse<Account> getById(String id) {
        HttpResponse<Account> rep = new HttpResponse<Account>();
        Account a = accountService.findById(id);
        rep.setData(a);
        return rep;
    }
    /**
     * 功能描述: <br>根据id查询账户信息，增加查询了客户的市场属性
     * 〈〉
     * @Param: '
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.sys.Account>
     * @Author: huanghaibin
     * @Date: 2021/3/1 13:49
     */
    @GetMapping(path = "/getByIdAndCustMarket")
    public HttpResponse<Account> getByIdAndCustMarket(String id) {
        HttpResponse<Account> rep = new HttpResponse<Account>();
        Account a = accountService.getByIdAndCustMarket(id);
        rep.setData(a);
        return rep;
    }


}
