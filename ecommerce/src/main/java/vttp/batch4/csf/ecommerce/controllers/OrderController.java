package vttp.batch4.csf.ecommerce.controllers;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.io.StringReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vttp.batch4.csf.ecommerce.models.Cart;
import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;
import vttp.batch4.csf.ecommerce.models.OrderException;
import vttp.batch4.csf.ecommerce.services.PurchaseOrderService;

@Controller
@RequestMapping(path = "/api")
public class OrderController {

  @Autowired
  private PurchaseOrderService poSvc;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  @PostMapping(path = "/order")
  @ResponseBody
  public ResponseEntity<String> postOrder(@RequestBody String req) {
    // TODO Task 3
    Order order = new Order();

    try (JsonReader jsonReader = Json.createReader(new StringReader(req));) {
      JsonObject jsonObject = jsonReader.readObject();

      order.setName(jsonObject.getString("name"));
      order.setAddress(jsonObject.getString("address"));
      order.setPriority(jsonObject.getBoolean("priority"));
      order.setComments(jsonObject.getString("comments"));

      Cart cart = new Cart();

      JsonArray json_cart = jsonObject
        .getJsonObject("cart")
        .getJsonArray("lineItems");
      for (JsonValue jsonValue : json_cart) {
        JsonObject line_item_object = jsonValue.asJsonObject();
        LineItem lineItem = new LineItem();
        lineItem.setProductId(line_item_object.getString("prodId"));
        lineItem.setName(line_item_object.getString("name"));
        lineItem.setPrice(line_item_object.getInt("price"));
        lineItem.setQuantity(line_item_object.getInt("quantity"));
        cart.addLineItem(lineItem);
      }

      order.setCart(cart);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    try {
      poSvc.createNewPurchaseOrder(order);
    } catch (OrderException e) {
      return ResponseEntity
        .badRequest()
        .body(String.format("{\"message\": \"%s\"}", e.getMessage()));
    }

    return ResponseEntity.ok(
      String.format("{\"OrderId\": \"%s\"}", order.getOrderId())
    );
  }
}
