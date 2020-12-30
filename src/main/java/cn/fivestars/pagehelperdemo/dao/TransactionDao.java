package cn.fivestars.pagehelperdemo.dao;

import cn.fivestars.pagehelperdemo.dto.TransactionDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事务测试
 *
 * @author liangchenzhou1024@163.com
 * @date 2020/12/26
 */
@Repository
@Mapper
public interface TransactionDao {

    int batchInsert(List<TransactionDto> list);

    int batchUpdate1(List<TransactionDto> list);

    int batchUpdate11(List<TransactionDto> list);

    int batchUpdate2(List<TransactionDto> list);

    int batchUpdate22(List<TransactionDto> list);

    List<TransactionDto> selectAll();

    int deleteAll();
}
