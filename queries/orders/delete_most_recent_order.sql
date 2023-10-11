DELETE FROM orders

WHERE order_id = (
	SELECT MAX(order_id)
	FROM orders
);
