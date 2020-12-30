package cn.fivestars.pagehelperdemo.dao;

import cn.fivestars.pagehelperdemo.dto.TransactionDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author liangchenzhou1024@163.com
 * @date 2020/12/26
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TransactionDaoTest {
    Logger logger = LoggerFactory.getLogger(TransactionDaoTest.class);
    @Mock
    private TransactionDao transactionDao;

    @Test
    public void testBatchUpdate() {
        List<TransactionDto> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            TransactionDto transactionDto = new TransactionDto((long) i, "name1_" + i, "name2_" + i);
            list.add(transactionDto);
        }
        logger.info("list.size={}", list.size());
        transactionDao.batchInsert(list);
        logger.info("batchInsert over");

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            List<TransactionDto> update1 = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                TransactionDto transactionDto = new TransactionDto((long) i, "newName1_" + i, "name2_" + i);
                update1.add(transactionDto);
            }
            transactionDao.batchUpdate1(update1);
        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            List<TransactionDto> update2 = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                TransactionDto transactionDto = new TransactionDto((long) i, "name1_" + i, "newName2_" + i);
                update2.add(transactionDto);
            }
            transactionDao.batchUpdate2(update2);
        });
        CompletableFuture.allOf(future1, future2).join();

        List<TransactionDto> transactionDtos = transactionDao.selectAll();
        Assertions.assertNotNull(transactionDtos);
        Assertions.assertEquals(10000, transactionDtos.size());
        for (int i = 0; i < 10000; i++) {
            Assertions.assertEquals((long) i, java.util.Optional.ofNullable(transactionDtos.get(i).getId()));
        }
    }

    @Test
    public void testBatchUpdate11() {
        List<TransactionDto> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            TransactionDto transactionDto = new TransactionDto((long) i, "name1_" + i, "name2_" + i);
            list.add(transactionDto);
        }
        logger.info("list.size={}", list.size());
        transactionDao.batchInsert(list);
        logger.info("batchInsert over");

        List<TransactionDto> update11 = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            TransactionDto transactionDto = new TransactionDto((long) i, "newName1_" + i, "name2_" + i);
            update11.add(transactionDto);
        }
        transactionDao.batchUpdate11(update11);
        List<TransactionDto> transactionDtos = transactionDao.selectAll();
        logger.info("transactionDtos={}", transactionDtos);


    }


}
