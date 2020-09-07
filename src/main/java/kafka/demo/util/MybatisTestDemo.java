package kafka.demo.util;

import jdk.nashorn.api.scripting.ScriptUtils;
import kafka.demo.domain.dao.UserMapper;
import kafka.demo.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.BeanFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * MyBatis深入学习demo
 * 1. TypeHandler
 * 2. plugin,拦截器
 *
 * @author : sk
 */
@Slf4j
public class MybatisTestDemo {
    public static void main(String[] args) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
//        InputStream in =Resources.getResourceAsStream("mybatis-config.xml");// ok
//        InputStream in =new ClassPathResource("mybatis-config.xml").getInputStream();// ok
//        InputStream in =MybatisTestDemo.class.getClassLoader().getResourceAsStream("mybatis-config.xml");// ok
//        InputStream in = Resource.class.getResourceAsStream("/mybatis-config.xml");
//        System.out.println(in);
        SqlSessionFactoryBuilder sbuild = new SqlSessionFactoryBuilder();
        SqlSessionFactory sb = sbuild.build(MybatisTestDemo.class.getResourceAsStream("/mybatis-config.xml"));
        SqlSession session = sb.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User newUser = new User();
        newUser.setName("31 insert");
        newUser.setAge(31);
        newUser.setMoney(1000.0);
//        log.info("mybatis insert->"+userMapper.insert(newUser));
        User user5 = userMapper.selectByPrimaryKey(5);
        user5.setName("55555555555555");
        userMapper.updateByPrimaryKey(user5);
        for (int i = 0; i < 1; i++) {
            log.info("mybatis查询结果" + i + "->" + userMapper.selectByPrimaryKey(5).getName());
        }
//        log.info("mybatis查询结果6->"+userMapper.selectByPrimaryKey(6).getName());
//        log.info("mybatis查询结果7->"+userMapper.selectByPrimaryKey(7).getName());
//        log.info("mybatis查询结果8->"+userMapper.selectByPrimaryKey(8).getName());
//        log.info("mybatis查询结果9->"+userMapper.selectByPrimaryKey(9).getName());
        // 地理
        System.out.println(getMapper(BeanFactory.class).getClass().getName());
        // 代理再代理（IllegalArgumentException: com.sun.proxy.$Proxy21 is not an interface）
        System.out.println(getMapper(getMapper(BeanFactory.class).getClass()).getClass().getName());
    }


    public static <T> T getMapper(Class<T> clazz) throws IllegalAccessException, InstantiationException {
//        return (T)clazz.newInstance();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                    log.info("invoke Object class method->" + method.getName());
                    return method.invoke(this,args);
                }
                log.info("invoke  method->" +method.getName());
                return null;
            }
        });
    }
}
