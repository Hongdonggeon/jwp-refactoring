package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class MenusResponse {

    private final List<MenuResponse> menuResponses;

    public MenusResponse(final List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }

    public static MenusResponse from(final List<Menu> menus) {
        final List<MenuResponse> menuResponses = menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        return new MenusResponse(menuResponses);
    }
}
