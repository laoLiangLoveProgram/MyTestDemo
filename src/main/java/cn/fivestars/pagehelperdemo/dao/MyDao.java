package cn.fivestars.pagehelperdemo.dao;

import cn.fivestars.pagehelperdemo.dto.MyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MyDao {

    List<Integer> select();

    List<MyDto> selectById(Long id);

    List<MyDto> selectByName(String name);

    List<MyDto> selectByDto(MyDto dto);


    List<MyDto> selectByDtoList(@Param("list") List<MyDto> list);


    List<MyDto> selectByIdList(@Param("list") List<Long> list);


    List<MyDto> selectByIdAndDtoList(@Param("id")Long id, @Param("list") List<MyDto> list);

    List<MyDto> selectByIdAndNameList(@Param("id")Long id, @Param("list") List<String> list);

    List<MyDto> selectByNameList(List<String> list);

    List<MyDto> selectByChild(MyDto myDto);

    List<MyDto> selectByChildList(@Param("list") List<MyDto> list);


}
