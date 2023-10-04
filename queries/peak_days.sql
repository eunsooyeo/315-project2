SELECT 
    date, 
    SUM(order_total) AS total_sales 
    
FROM orders 

GROUP BY 
    date 

ORDER BY    
    total_sales 
    
DESC LIMIT 10;