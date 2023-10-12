SELECT 
    o.order_id,
    o.date,
    o.time,
    array_agg(r.drinkName) AS recipe_name,
    o.cost
FROM 
    orders o
JOIN 
    recipes r ON r.recipeid = ANY(o.drink_id)
WHERE 
    o.date >= DATE '2023-01-01' AND o.date <= DATE '2023-01-31'
GROUP BY
    o.order_id
ORDER BY 
    o.date;
