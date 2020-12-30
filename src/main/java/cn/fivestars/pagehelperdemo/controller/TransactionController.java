package cn.fivestars.pagehelperdemo.controller;

import cn.fivestars.pagehelperdemo.dto.TransactionDto;
import cn.fivestars.pagehelperdemo.service.TransactionService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 事务测试
 *
 * @author liangchenzhou1024@163.com
 * @version 1.0
 * @date 2020/12/26
 */
@RestController
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Resource
    private TransactionService transactionService;

    @GetMapping("/t")
    public String testTransaction() {
        List<TransactionDto> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TransactionDto transactionDto = new TransactionDto((long) i, "name1_" + i, "name2_" + i);
            list.add(transactionDto);
        }
        logger.info("list.size={}", list.size());
        transactionService.batchInsert(list);
        logger.info("batchInsert over");

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            List<TransactionDto> update1 = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                TransactionDto transactionDto = new TransactionDto((long) i, "newName1_" + i, "name2_" + i);
                update1.add(transactionDto);
            }
            long start = 0L;
            logger.info("batchUpdate1 start, start={}", start = System.currentTimeMillis());
            transactionService.batchUpdate1(update1);
            logger.info("batchUpdate1 over, const={}", System.currentTimeMillis() - start);

        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            List<TransactionDto> update2 = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                TransactionDto transactionDto = new TransactionDto((long) i, "name1_" + i, "newName2_" + i);
                update2.add(transactionDto);
            }
            long start = 0L;
            logger.info("batchUpdate2 start, start={}", start = System.currentTimeMillis());
            transactionService.batchUpdate2(update2);
            logger.info("batchUpdate2 over, const={}", System.currentTimeMillis() - start);

        });
        CompletableFuture.allOf(future1, future2).join();

        PageHelper.startPage(1, 10);
        List<TransactionDto> transactionDtos = transactionService.selectAll();
        logger.info(" transactionDtos.size = {}", transactionDtos.size());
        logger.info(" transactionDtos = {}", transactionDtos);

        transactionService.deleteAll();
        transactionDtos = transactionService.selectAll();
        logger.info(" transactionDtos.size = {}", transactionDtos.size());
        logger.info(" transactionDtos = {}", transactionDtos);

        return "t";
    }
}
