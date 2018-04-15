package ru.atom.matchmaker;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import ru.atom.controllers.MatchMakerController;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    MatchMakerController matchMakerController;

    @Test
    public void contextLoad()
    {
        assertThat(matchMakerController).isNotNull();
    }

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void TestController() throws Exception
    {
        MvcResult result = mockMvc.perform(
                post("/game/create/").param("playerCount", "4"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        assertEquals(result.getResponse().getContentAsString(), "0");
    }

    @Test
    public void testJoin() throws Exception
    {
        String[] names = {"Kolya", "Sonya", "Masha", "Dima"};
        for (String name : names)
        {
            MvcResult result = mockMvc.perform(
                    post("/matchmaker/join/").param("name", name))
                    .andDo(print())
                    .andExpect(status().isOk()).andReturn();
            assertEquals(result.getResponse().getContentAsString(), "0");
        }
    }
}
