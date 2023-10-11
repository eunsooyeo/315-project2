SELECT 
    orders.date,
    EXTRACT(HOUR FROM time) AS hour, 
    COUNT(*) AS order_count, 
    SUM(cost) AS total_sales 

FROM orders 

GROUP BY 
    orders.date,
    hour 

ORDER BY 
    orders.date, hour;