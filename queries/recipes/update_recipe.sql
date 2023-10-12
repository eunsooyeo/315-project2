UPDATE recipes
SET
    ingredient_names = ARRAY_APPEND(ingredient_names, 'new_ingredient'),
    ingredient_values = ARRAY_APPEND(ingredient_values, 42.5)
WHERE drinkname = 'Mountain Dew';