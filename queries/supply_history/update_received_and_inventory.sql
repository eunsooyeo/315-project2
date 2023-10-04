UPDATE supply_history
SET 
    received_date = '2023-10-01', 
    inventory_update_date = '2023-10-01'
WHERE 
    order_id = 1;

CREATE OR REPLACE FUNCTION update_inventory_on_supply_history_update()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the received_date was modified from NULL to a date
    IF NEW.received_date IS NOT NULL AND OLD.received_date IS NULL THEN
        -- Loop through the supplies and quantities arrays
        FOR i IN 1..array_length(NEW.supplies, 1) LOOP
            -- Update the inventory table based on the supply name and quantity
            UPDATE inventory
            SET amount = amount + NEW.quantities[i]
            WHERE name = NEW.supplies[i];
        END LOOP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_inventory_trigger
AFTER UPDATE ON supply_history
FOR EACH ROW
WHEN ((NEW.received_date IS NOT NULL) AND (OLD.received_date IS NULL))
EXECUTE FUNCTION update_inventory_on_supply_history_update();

