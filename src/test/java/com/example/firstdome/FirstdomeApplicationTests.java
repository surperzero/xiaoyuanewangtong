package com.example.firstdome;


import com.example.firstdome.entitys.User;
import com.example.firstdome.mapper.UserMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FirstdomeApplicationTests {


		@Autowired
		private UserMapper userMapper;

		@Test
		public void test(){
			User user=User.builder().name("xiaogu1").password("123456").phone("17723299232").remark("dsgahdsah").build();
			userMapper.insert(user);
		}


}
