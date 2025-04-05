package dayp308.chatroom.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import org.springframework.data.domain.Sort;

public class QueryUtil {
    public static Order getOrder(Sort.Order order, CriteriaBuilder cb, Expression<?> column) {
        return order.isAscending() ? cb.asc(column) : cb.desc(column);
    }
}