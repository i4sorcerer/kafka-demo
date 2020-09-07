package kafka.demo.domain.controller;

import kafka.demo.domain.dao.UserMapper;
import kafka.demo.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User的controller
 *
 * @author : sk
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    /**
     * 注入userMapper
     */
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/name")
    public User getName(@RequestParam Integer id){
        log.info("userMapper getName id->"+id);
//        return userMapper.selectName(id);
        return userMapper.selectByPrimaryKey(id);
    }
}
