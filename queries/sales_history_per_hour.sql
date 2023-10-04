SELECT 
    EXTRACT(HOUR FROM date) AS hour, 
    COUNT(*) AS order_count, 
    SUM(cost) AS total_sales 

FROM orders 

GROUP BY 
    hour 

ORDER BY 
    hour;