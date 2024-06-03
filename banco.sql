-- Criar tabela customers
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Inserir 10 clientes de exemplo
INSERT INTO customer (name) VALUES
('Cliente 1'), ('Cliente 2'), ('Cliente 3'), ('Cliente 4'), ('Cliente 5'),
('Cliente 6'), ('Cliente 7'), ('Cliente 8'), ('Cliente 9'), ('Cliente 10');

-- Criar tabela orders
CREATE TABLE order_table (
    id INT AUTO_INCREMENT PRIMARY KEY,
    controlNumber VARCHAR(50) NOT NULL UNIQUE,
    registrationDate DATETIME NOT NULL,
    productName VARCHAR(100) NOT NULL,
    unitPrice DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    totalPrice DECIMAL(10, 2) NOT NULL,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);