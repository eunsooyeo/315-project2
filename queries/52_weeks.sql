SELECT 
    EXTRACT(YEAR FROM date) AS year, 
    EXTRACT(WEEK FROM date) AS week, 
    COUNT(*) AS order_count 

FROM orders 

GROUP BY 
    year, week 

ORDER BY 
    year, week;