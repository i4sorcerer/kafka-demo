package kafka.demo.domain.dao;

import kafka.demo.domain.model.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Select("select name from user where id=#{id}")
    @Results({
            @Result(property = "name",column = "name")
    })
    User selectName (Integer id);
}