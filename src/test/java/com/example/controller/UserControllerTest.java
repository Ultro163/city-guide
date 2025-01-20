package com.example.controller;

import com.example.dto.mappers.UserMapper;
import com.example.dto.user.NewUserDto;
import com.example.dto.user.UpdateUserDto;
import com.example.dto.user.UserDto;
import com.example.model.User;
import com.example.service.CrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CrudService<User, Long> userServiceImpl;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private final NewUserDto newUserDto = new NewUserDto("Василий", "vasya777@example.com");
    private final UserDto userDto = new UserDto(1L, "Василий", "vasya777@example.com");
    private final User user = new User();

    @BeforeEach
    public void setUp() {
        user.setId(1L);
        user.setName("Василий");
        user.setEmail("vasya777@example.com");

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void createUserWithValidData() throws Exception {
        Mockito.when(userMapper.toEntity(newUserDto)).thenReturn(user);
        Mockito.when(userMapper.toUserDto(Mockito.any(User.class))).thenReturn(userDto);
        Mockito.when(userServiceImpl.create(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Василий\",\"email\":\"vasya777@example.com\"}"))
                .andExpect(status().isCreated());

        verify(userServiceImpl, times(1)).create(Mockito.any(User.class));
    }

    @Test
    public void getUserByIdWithValidData() throws Exception {
        Mockito.when(userServiceImpl.getById(1L)).thenReturn(user);
        Mockito.when(userMapper.toUserDto(Mockito.any(User.class))).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk());

        verify(userServiceImpl, times(1)).getById(1L);
    }

    @Test
    public void updateUserWithValidData() throws Exception {
        Mockito.when(userMapper.toEntity(Mockito.any(UpdateUserDto.class))).thenReturn(user);
        Mockito.when(userMapper.toUserDto(user)).thenReturn(userDto);
        Mockito.when(userServiceImpl.update(1L, user)).thenReturn(user);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Василий\",\"email\":\"vasya777@example.com\"}"))
                .andExpect(status().isOk());

        verify(userServiceImpl, times(1)).update(1L, user);
    }

    @Test
    public void deleteUserWithValidData() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isNoContent());

        verify(userServiceImpl, times(1)).delete(1L);
    }

    @Test
    public void createUserWithoutRequiredField() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userServiceImpl);
    }
}
