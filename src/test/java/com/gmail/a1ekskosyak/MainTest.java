package com.gmail.a1ekskosyak;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.InputMismatchException;

public class MainTest {

    @Test
    public void printGreeting_ThrowsInputMismatchException_IfInputNotInteger() {
        // given
        String input = "saveUser";

        // when
        try {
            Main.printGreeting();
            Assertions.fail("no exception thrown");
        } catch (InputMismatchException e) {
            // then
//            Assertions.assertThat(e.getMessage()).

        }
    }
}