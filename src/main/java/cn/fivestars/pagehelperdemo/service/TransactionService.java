package cn.fivestars.pagehelperdemo.service;

import cn.fivestars.pagehelperdemo.dao.TransactionDao;
import cn.fivestars.pagehelperdemo.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试事务
 *
 * @author liangchenzhou1024@163.com
 * @date 2020/12/26
 */
@Service
@Transactional
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Resource
    private TransactionDao transactionDao;

    public void batchInsert(List<TransactionDto> list) {
        transactionDao.batchInsert(list);
    }

    public void batchUpdate1(List<TransactionDto> list) {
        transactionDao.batchUpdate1(list);
    }

    public void batchUpdate2(List<TransactionDto> list) {
        transactionDao.batchUpdate2(list);
    }

    public List<TransactionDto> selectAll() {
        return transactionDao.selectAll();
    }

    public int deleteAll() {
        return transactionDao.deleteAll();
    }
}
