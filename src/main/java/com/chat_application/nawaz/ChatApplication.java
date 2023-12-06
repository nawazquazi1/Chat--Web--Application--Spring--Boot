package com.chat_application.nawaz;

import com.chat_application.nawaz.config.AppConstants;
import com.chat_application.nawaz.config.ApplicationConfiguration;
import com.chat_application.nawaz.model.Role;
import com.chat_application.nawaz.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ChatApplication implements CommandLineRunner {


    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private ApplicationConfiguration configuration;

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        try {
            Role role1 = new Role();
            role1.setId(AppConstants.ADMIN_USER);
            role1.setName("ROLE_ADMIN");

            Role role2 = new Role();
            role2.setId(AppConstants.NORMAL_USER);
            role2.setName("ROLE_NORMAL");
            System.out.println(configuration.passwordEncoder().encode("nawaz"));
            List<Role> result = this.roleRepository.saveAll(List.of(role1, role2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
