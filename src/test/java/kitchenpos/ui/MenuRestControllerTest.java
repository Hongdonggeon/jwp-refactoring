package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
@ExtendWith(MockitoExtension.class)
public class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("menu를 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(1L, 3);
        final MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 3);
        final List<MenuProductRequest> menuProductRequests = Arrays.asList(menuProduct1, menuProduct2);
        final MenuRequest menuRequest = new MenuRequest("메뉴1", BigDecimal.valueOf(3000), 1L, menuProductRequests);

        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        final Menu savedMenu = new Menu(1L, "메뉴1", BigDecimal.valueOf(3000), 1L, menuProducts);

        given(menuService.create(any(MenuRequest.class))).willReturn(savedMenu);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("menu들을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 3);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 3);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("메뉴1", BigDecimal.valueOf(3000), 1L, menuProducts);
        final List<Menu> menus = Arrays.asList(menu);

        given(menuService.list()).willReturn(menus);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/menus")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }
}
