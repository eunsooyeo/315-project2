SELECT 
    EXTRACT(YEAR FROM orders.date) AS year, 
    EXTRACT(WEEK FROM orders.date) AS week, 
    COUNT(*) AS order_count 

FROM orders 

GROUP BY 
    year, week 

ORDER BY 
    year, week;