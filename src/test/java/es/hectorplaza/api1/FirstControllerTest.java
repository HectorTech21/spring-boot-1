package es.hectorplaza.api1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FirstControllerTest {

    private static final String PATH = "/name";
    private static final MediaType TEXT_UTF8 = MediaType.parseMediaType("text/plain;charset=UTF-8");

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void clearStoredName() throws Exception {
        mvc.perform(delete(PATH));
    }

    @Test
    void returnsNotFoundWhenNoNameIsStored() throws Exception {
        mvc.perform(get(PATH))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay ningún nombre guardado."));
        mvc.perform(put(PATH).contentType(TEXT_UTF8).content("Héctor"))
                .andExpect(status().isNotFound());
        mvc.perform(delete(PATH))
                .andExpect(status().isNotFound());
    }

    @Test
    void createsAndReadsName() throws Exception {
        mvc.perform(post(PATH).contentType(TEXT_UTF8).content("  Héctor  "))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", PATH))
                .andExpect(content().string(""));
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_UTF8))
                .andExpect(content().string("Hola Héctor"));
    }

    @Test
    void updatesStoredName() throws Exception {
        mvc.perform(post(PATH).contentType(TEXT_UTF8).content("Héctor"))
                .andExpect(status().isCreated());
        mvc.perform(put(PATH).contentType(TEXT_UTF8).content("Lucía"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola Lucía"));
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola Lucía"));
    }

    @Test
    void deletesStoredName() throws Exception {
        mvc.perform(post(PATH).contentType(TEXT_UTF8).content("Héctor"))
                .andExpect(status().isCreated());
        mvc.perform(delete(PATH).param("name", "Héctor"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        mvc.perform(get(PATH))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsSecondPostAndKeepsStoredName() throws Exception {
        mvc.perform(post(PATH).contentType(TEXT_UTF8).content("Héctor"))
                .andExpect(status().isCreated());
        mvc.perform(post(PATH).contentType(TEXT_UTF8).content("Lucía"))
                .andExpect(status().isConflict());
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola Héctor"));
    }

    @Test
    void rejectsMissingAndBlankNamesWithoutChangingState() throws Exception {
        mvc.perform(post(PATH).contentType(TEXT_UTF8))
                .andExpect(status().isBadRequest());
        mvc.perform(post(PATH).contentType(TEXT_UTF8).content(" \t "))
                .andExpect(status().isBadRequest());
        mvc.perform(get(PATH))
                .andExpect(status().isNotFound());

        mvc.perform(post(PATH).contentType(TEXT_UTF8).content("Héctor"))
                .andExpect(status().isCreated());
        mvc.perform(put(PATH).contentType(TEXT_UTF8))
                .andExpect(status().isBadRequest());
        mvc.perform(put(PATH).contentType(TEXT_UTF8).content(" \t "))
                .andExpect(status().isBadRequest());
        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola Héctor"));
    }
}
