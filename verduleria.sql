-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-09-2023 a las 18:27:55
-- Versión del servidor: 10.4.24-MariaDB
-- Versión de PHP: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `verduleria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `batches`
--

CREATE DATABASE IF NOT EXISTS verduleria;

USE verduleria;

CREATE TABLE `batches` (
  `id` bigint(20) NOT NULL,
  `entry_date` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `purchase_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `batch_product`
--

CREATE TABLE `batch_product` (
  `product_id` bigint(20) NOT NULL,
  `batch_id` bigint(20) NOT NULL,
  `expire_date` varchar(255) DEFAULT NULL,
  `out_date` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `out_quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `name` varchar(75) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clients`
--

CREATE TABLE `clients` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `invoices`
--

CREATE TABLE `invoices` (
  `id` bigint(20) NOT NULL,
  `name` varchar(75) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `nit` varchar(255) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `sale_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products`
--

CREATE TABLE `products` (
  `id` bigint(20) NOT NULL,
  `name` varchar(75) NOT NULL,
  `stock` int(11) NOT NULL,
  `unit_price` double NOT NULL,
  `unit_measurement` varchar(255) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `purchases`
--

CREATE TABLE `purchases` (
  `id` bigint(20) NOT NULL,
  `date` varchar(255) DEFAULT NULL,
  `total` double NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `supplier_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sales`
--

CREATE TABLE `sales` (
  `id` bigint(20) NOT NULL,
  `content` JSON DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `total` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `suppliers`
--

CREATE TABLE `suppliers` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `name`, `username`, `password`) VALUES
(1, 'test name', 'test', '$2a$10$cEa266p.U8YuqcdD.AyWzesiUevdyhhu2r2aTYdQF01G8dFLl4Rv2');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `batches`
--
ALTER TABLE `batches`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_batches_purchase_idx` (`purchase_id`);

--
-- Indices de la tabla `batch_product`
--
ALTER TABLE `batch_product`
  ADD KEY `fk_batch_product_product_idx` (`product_id`),
  ADD KEY `fk_batch_product_batch_idx` (`batch_id`);

--
-- Indices de la tabla `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_sale_id` (`sale_id`);

--
-- Indices de la tabla `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name_unique` (`name`),
  ADD KEY `fk_products_category_idx` (`category_id`);

--
-- Indices de la tabla `purchases`
--
ALTER TABLE `purchases`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_purchases_supplier_idx` (`supplier_id`);

--
-- Indices de la tabla `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `batches`
--
ALTER TABLE `batches`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clients`
--
ALTER TABLE `clients`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `invoices`
--
ALTER TABLE `invoices`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `purchases`
--
ALTER TABLE `purchases`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `sales`
--
ALTER TABLE `sales`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `batches`
--
ALTER TABLE `batches`
  ADD CONSTRAINT `fk_batches_purchase` FOREIGN KEY (`purchase_id`) REFERENCES `purchases` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `batch_product`
--
ALTER TABLE `batch_product`
  ADD CONSTRAINT `fk_batch_product_batch` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_batch_product_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `FK_sale_id` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`id`);

--
-- Filtros para la tabla `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `purchases`
--
ALTER TABLE `purchases`
  ADD CONSTRAINT `fk_purchases_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- Categories test data 
INSERT INTO categories (name) VALUES
('Frutas'),
('Verduras'),
('Legumbres'),
('Hierbas y Especias');

-- Productos de prueba
INSERT INTO products (name, stock, unit_price, unit_measurement, category_id) VALUES
('Manzana', 100, 1.5, 'Kilogramo', 1),
('Plátano', 75, 2.0, 'Kilogramo', 1),
('Naranja', 80, 1.2, 'Kilogramo', 1),
('Peras', 60, 1.8, 'Kilogramo', 1),
('Uvas', 40, 3.0, 'Kilogramo', 1),
('Papaya', 40, 2.5, 'Kilogramo', 1),
('Sandía', 30, 2.0, 'Kilogramo', 1),
('Mango', 50, 1.8, 'Kilogramo', 1),
('Cerezas', 45, 3.2, 'Kilogramo', 1),
('Kiwi', 35, 1.4, 'Pieza', 1);

INSERT INTO products (name, stock, unit_price, unit_measurement, category_id) VALUES
('Zanahoria', 50, 0.8, 'Kilogramo', 2),
('Lechuga', 60, 1.0, 'Pieza', 2),
('Tomate', 70, 1.2, 'Kilogramo', 2),
('Pepino', 40, 1.0, 'Kilogramo', 2),
('Pimiento', 30, 1.5, 'Kilogramo', 2),
('Espinacas', 30, 1.2, 'Kilogramo', 2),
('Cebolla', 55, 1.0, 'Kilogramo', 2),
('Calabacín', 40, 1.5, 'Kilogramo', 2),
('Col Rizada', 20, 1.8, 'Kilogramo', 2),
('Brócoli', 25, 1.7, 'Kilogramo', 2);

INSERT INTO products (name, stock, unit_price, unit_measurement, category_id) VALUES
('Lentejas', 25, 2.5, 'Kilogramo', 3),
('Garbanzos', 20, 2.8, 'Kilogramo', 3),
('Frijoles', 30, 2.0, 'Kilogramo', 3),
('Arvejas', 40, 2.2, 'Kilogramo', 3),
('Habas', 35, 2.4, 'Kilogramo', 3),
('Maíz', 35, 2.0, 'Kilogramo', 3),
('Arroz', 60, 2.2, 'Kilogramo', 3),
('Couscous', 30, 2.5, 'Kilogramo', 3),
('Quinua', 45, 2.8, 'Kilogramo', 3),
('Frijoles Negros', 20, 2.3, 'Kilogramo', 3);

INSERT INTO products (name, stock, unit_price, unit_measurement, category_id) VALUES
('Albahaca', 15, 1.2, 'Pieza', 4),
('Orégano', 18, 0.8, 'Pieza', 4),
('Cilantro', 22, 0.9, 'Pieza', 4),
('Romero', 20, 1.0, 'Pieza', 4),
('Pimienta Negra', 25, 1.5, 'Gramo', 4),
('Perejil', 22, 0.7, 'Pieza', 4),
('Comino', 28, 0.9, 'Gramo', 4),
('Tomillo', 15, 0.8, 'Gramo', 4),
('Nuez Moscada', 18, 1.1, 'Gramo', 4),
('Jengibre', 25, 1.2, 'Gramo', 4);

