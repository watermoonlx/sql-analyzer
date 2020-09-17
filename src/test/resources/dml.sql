SELECT *
FROM `order`
         INNER JOIN order_item
                    ON `order`.order_no = order_item.order_no;
SELECT *
FROM `order` AS o
         INNER JOIN order_item ON o.order_no = order_item.order_no
         INNER JOIN `user` AS u ON o.user_id = u.id;

SELECT *
FROM `order` AS o
         INNER JOIN order_item AS i ON o.order_no = i.order_no
         INNER JOIN `user` AS u ON o.user_id = u.id
         INNER JOIN `product` AS p ON p.id = i.product_id;

SELECT *
FROM `order` AS o
         INNER JOIN order_item AS i ON o.order_no = i.order_no
         INNER JOIN `user` AS u ON o.user_id = u.id
         INNER JOIN `product` AS p ON p.id = i.product_id
         LEFT JOIN `category` AS c ON p.category_id = c.id;

SELECT *
FROM `order` AS o
         INNER JOIN order_item AS i ON o.order_no = i.order_no
         INNER JOIN `user` AS u ON o.user_id = u.id
         INNER JOIN `product` AS p ON p.id = i.product_id
         LEFT JOIN `category` AS c ON p.category_id = c.id
         RIGHT JOIN `pay_info` AS pay ON o.order_no = pay.order_no;

SELECT *
FROM `order` AS o
         INNER JOIN order_item AS i ON o.order_no = i.order_no
         INNER JOIN `user` AS u ON o.user_id = u.id
         INNER JOIN `product` AS p ON p.id = i.product_id
         LEFT JOIN `category` AS c ON p.category_id = c.id
         RIGHT JOIN `pay_info` AS pay ON o.order_no = pay.order_no
         INNER JOIN `shipping` AS s ON o.`shipping_id` = s.id;

SELECT *
FROM `cart` AS c
         LEFT JOIN `user` AS u ON c.user_id = u.id
         LEFT JOIN `product` AS p ON p.id = c.product_id;