package project.boot.lodeur.dto.request.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequestDTO {
    private String productId;
    private int quantity;
}
