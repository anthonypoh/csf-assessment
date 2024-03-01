package vttp.batch4.csf.ecommerce.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;
import vttp.batch4.csf.ecommerce.models.OrderException;

@Repository
public class PurchaseOrderRepository {

  @Autowired
  private JdbcTemplate template;

  public static final String SQL_INSERT_ORDER =
    "INSERT INTO orders values (?,?,?,?,?,?)";

  public static final String SQL_INSERT_LINE_ITEM =
    "INSERT INTO line_items (orderId, productId, name, quantity, price) values (?,?,?,?,?)";

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  // You may only add Exception to the method's signature
  public void create(Order order) throws OrderException {
    // TODO Task 3
    // if (template.update(SQL_INSERT_USER, user.email(), user.name()) <= 0) {
    //   throw new BookingException("Error inserting user.");
    // }
    String oid = order.getOrderId();

    if (
      template.update(
        SQL_INSERT_ORDER,
        oid,
        order.getDate(),
        order.getName(),
        order.getAddress(),
        order.getPriority(),
        order.getComments()
      ) <=
      0
    ) throw new OrderException("Error inserting into order table.");

    for (LineItem lineItem : order.getCart().getLineItems()) {
      if (
        template.update(
          SQL_INSERT_LINE_ITEM,
          oid,
          lineItem.getProductId(),
          lineItem.getName(),
          lineItem.getQuantity(),
          lineItem.getPrice()
        ) <=
        0
      ) throw new OrderException("Error inserting line_items table.");
    }
  }
}
