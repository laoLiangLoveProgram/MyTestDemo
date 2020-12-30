package cn.fivestars.pagehelperdemo.controller;

import cn.fivestars.pagehelperdemo.dao.MyDao;
import cn.fivestars.pagehelperdemo.dto.Child;
import cn.fivestars.pagehelperdemo.dto.MyDto;
import cn.fivestars.pagehelperdemo.service.MyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MyController {

    @Resource
    private MyService myService;

    @GetMapping("/hello")
    public String hello() {
        myService.select();

        return "hello";
    }

    @Resource
    private MyDao myDao;

    @GetMapping("/test1")
    public String testSelectByDto() {
        MyDto myDto = new MyDto(1L, "111");
        List<MyDto> myDtos = myDao.selectByDto(myDto);

        return myDtos.toString();

    }


    @GetMapping("/test2")
    public String testSelectByDtoList() {
        List<MyDto> list = new ArrayList<>();
        MyDto myDto1 = new MyDto(1L, "111");
        MyDto myDto2 = new MyDto(2L, "222");
        list.add(myDto1);
        list.add(myDto2);
        List<MyDto> myDtos = myDao.selectByDtoList(list);

        return myDtos.toString();

    }

    @GetMapping("/test3")
    public String testSelectByIdList() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);

        List<MyDto> myDtos = myDao.selectByIdList(list);

        return myDtos.toString();

    }

    @GetMapping("/test4")
    public String testselectByIdAndDtoList() {
        List<MyDto> list = new ArrayList<>();
        MyDto myDto1 = new MyDto(1L, "111");
        MyDto myDto2 = new MyDto(2L, "222");
        list.add(myDto1);
        list.add(myDto2);

        List<MyDto> myDtos = myDao.selectByIdAndDtoList(1L, list);

        return myDtos.toString();

    }

    @GetMapping("/test5")
    public String testselectByIdAndNameList() {
        List<String> list = new ArrayList<>();
        list.add("11111");
        list.add("22222");


        List<MyDto> myDtos = myDao.selectByIdAndNameList(1L, list);

        return myDtos.toString();

    }

    @GetMapping("/test6")
    public String testselectByNameList() {
        List<String> list = new ArrayList<>();
        list.add("11111");
        list.add("22222");


        List<MyDto> myDtos = myDao.selectByNameList(list);

        return myDtos.toString();

    }

    @GetMapping("/test7")
    public String testselectByChild() {
        Child child = new Child();
        child.setId(1L);
        MyDto myDto = new MyDto();
        myDto.setChild(child);
        List<MyDto> myDtos = myDao.selectByChild(myDto);

        return myDtos.toString();

    }

    @GetMapping("/test8")
    public String testselectByChildList() {
        Child child1 = new Child();
        child1.setId(1L);
        MyDto myDto1 = new MyDto();
        myDto1.setChild(child1);

        Child child2 = new Child();
        child2.setId(2L);
        MyDto myDto2 = new MyDto();
        myDto2.setChild(child2);

        List<MyDto> list = new ArrayList<>();
        list.add(myDto1);
        list.add(myDto2);
        List<MyDto> myDtos = myDao.selectByChildList(list);

        return myDtos.toString();
    }

    @GetMapping("/test9")
    public String testselectById() {
        List<MyDto> myDtos = myDao.selectById(1L);

        return myDtos.toString();
    }

    @GetMapping("/test10")
    public String testselectByName() {
        List<MyDto> myDtos = myDao.selectByName("11111");

        return myDtos.toString();
    }
}
