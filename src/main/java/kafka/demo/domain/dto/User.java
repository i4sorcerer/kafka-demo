package kafka.demo.domain.dto;

import lombok.Data;

/**
 * @author : sk
 */
@Data
public class User {
    private String name;
    private String pass;
    private String userId;
    private String phone;
    private double height;
    private double weight;
    private int age;
    private String gender;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
