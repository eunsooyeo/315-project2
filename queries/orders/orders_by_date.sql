SELECT 
    o.order_id, 
    o.date,
    o.time,
    array_agg(r.drinkName) AS recipe_name,
    SUM(unnested_costs) AS total_cost
FROM 
    orders o
JOIN 
    recipes r ON r.recipeid = ANY(o.drink_id)
CROSS JOIN LATERAL
    unnest(o.cost) AS unnested_costs
WHERE 
    o.date >= DATE '2023-01-01' AND o.date <= DATE '2023-01-31'
ORDER BY 
    o.date;
