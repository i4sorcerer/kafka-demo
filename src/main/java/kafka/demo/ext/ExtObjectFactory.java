package kafka.demo.ext;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.util.List;
import java.util.Properties;

/**
 * ObjectFactory的自定义实现
 *
 * @author : sk
 */
@Slf4j
public class ExtObjectFactory extends DefaultObjectFactory {
    public ExtObjectFactory() {
        super();
    }

    @Override
    public <T> T create(Class<T> type) {
        log.info("自定义对象生成工厂 create->"+type);
        return super.create(type);
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        log.info("自定义对象生成工厂 create2->"+type);
        return super.create(type, constructorArgTypes, constructorArgs);
    }

    @Override
    protected Class<?> resolveInterface(Class<?> type) {
        return super.resolveInterface(type);
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
        return super.isCollection(type);
    }

    @Override
    public void setProperties(Properties properties) {
        log.info("自定义ObjectFactory的setProperties->"+properties.toString());
        super.setProperties(properties);
    }
}
