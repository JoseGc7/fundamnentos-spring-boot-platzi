package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

    private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

    private ComponentDependency componentDependency;
    private MyBean myBean;
    private MyBeanWithDependency myBeanWithDependency;
    private MyBeanWithProperties myBeanWithProperties;
    private UserPojo userPojo;
    private UserRepository userRepository;

    public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency,
                                  MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties,
                                  UserPojo userPojo, UserRepository userRepository) {

        this.componentDependency = componentDependency;
        this.myBean = myBean;
        this.myBeanWithDependency = myBeanWithDependency;
        this.myBeanWithProperties = myBeanWithProperties;
        this.userPojo = userPojo;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {

        SpringApplication.run(FundamentosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //ejemplosAnteriores();
        saveUsersInDatabase();
        getInformationJpqlFromUser();
    }

    private void getInformationJpqlFromUser(){
        LOGGER.info("Usuario con el metodo findByUserEmail: "+
                userRepository.findByUserEmail("marco@domain.com")
                        .orElseThrow(() -> new RuntimeException("No se encontro el usuario")));

        userRepository.findAndSort("Ma", Sort.by("id").descending())
                .stream()
                .forEach(user -> LOGGER.info("Usuario con metodo sort "+ user));

        userRepository.findByName("John")
                .stream()
                .forEach(user -> LOGGER.info("Usuario con query method " + user));

       LOGGER.info("Usuario con query method findByEmailAndName "+ userRepository.findByEmailAndName("marco@domain.com","Marco")
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        /*this.userRepository.findByName("braian").ifPresentOrElse(
                user -> System.out.println("User: "+user.getName())
                ,()-> System.out.println("No existe el usuario")
        );*/


    }

    private void saveUsersInDatabase(){
        User user1 = new User("John", "john@domain.com", LocalDate.of(2021, 3, 13));
        User user2 = new User("Marco", "marco@domain.com", LocalDate.of(2021, 12, 8));
        User user3 = new User("Daniela", "daniela@domain.com", LocalDate.of(2021, 9, 8));
        User user4 = new User("Marisol", "marisol@domain.com", LocalDate.of(2021, 6, 18));
        User user5 = new User("Karen", "karen@domain.com", LocalDate.of(2021, 1, 1));
        User user6 = new User("Carlos", "carlos@domain.com", LocalDate.of(2021, 7, 7));
        User user7 = new User("Enrique", "enrique@domain.com", LocalDate.of(2021, 11, 12));
        User user8 = new User("Luis", "luis@domain.com", LocalDate.of(2021, 2, 27));
        User user9 = new User("Paola", "paola@domain.com", LocalDate.of(2021, 4, 10));

        List<User> list = Arrays.asList(user1,user2,user3,user4,user5,user6,user7,user8,user9);
        list.forEach(userRepository::save);

    }


    private void ejemplosAnteriores(){
        componentDependency.saludar();
        myBean.print();
        myBeanWithDependency.printWithDependency();
        System.out.println(myBeanWithProperties.function());
        System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword());
        System.out.println("Edad: " + userPojo.getAge());
        try {
            //error
            int value = 10 / 0;
            LOGGER.debug("Mi valor: " + value);
        } catch (Exception e) {
            LOGGER.debug("Esto es un error al dividir por cero: " + e.getMessage());
        }
    }
}
