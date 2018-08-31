package com.gmail.a1ekskosyak;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileUserServiceTest {

    private static final String PATH_TO_USER_FILE = "C:\\Users\\A1eks\\IdeaProjects\\MessengerSDA\\Files\\Users\\";

    IOUtils ioUtils;

    FileUserService fileUserService;

    @Before
    public void setUp() throws Exception {
        ioUtils = mock(IOUtils.class);
        fileUserService = new FileUserService(ioUtils);
    }

    @Test
    public void createNewUser_Returns_IfUserExists() {
        // given
        when(ioUtils.readNextLine()).thenReturn("userEmail");
        when(ioUtils.fileExist(anyString())).thenReturn(true);

        // when
        fileUserService.createNewUser();

        //then
        verify(ioUtils).writeMessage(eq("Input your email"));
        verify(ioUtils).writeMessage(eq("User with email userEmail already exists in the system."));
    }

    @Test
    public void createNewUser_CreateUser_IfUserDoesNotExist() {
        // given
        when(ioUtils.readNextLine()).thenReturn("userEmail", "userName", "userPassword");
        when(ioUtils.fileExist(anyString())).thenReturn(false);


        // when
        fileUserService.createNewUser();

        //then
        verify(ioUtils).writeMessage(eq("Input your email"));
        verify(ioUtils).writeMessage(eq("Input your name"));
        verify(ioUtils).writeMessage(eq("Input your password"));
        try {
            verify(ioUtils).saveUser(new User(
                    "userName",
                    "userEmail",
                    "userPassword"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(ioUtils).writeMessage(eq("User successfully created.\n Please login."));

    }

}