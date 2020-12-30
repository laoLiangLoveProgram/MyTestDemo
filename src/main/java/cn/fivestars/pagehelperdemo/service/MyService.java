package cn.fivestars.pagehelperdemo.service;

import cn.fivestars.pagehelperdemo.dao.MyDao;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MyService {


    @Resource
    private MyDao myDao;

    public void select() {
        PageHelper.startPage(0, 5);
        List<Integer> result = myDao.select();
        System.out.println(result.toString());
    }
}
