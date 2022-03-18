# Demo
該作品共有fund.html、fundstock.html、fundshareholding.html

後端: 使用SpringMVC框架，使用JdbcTmplate連接MySQL資料庫。

[entity](src/main/java/demo/entity) entity裡的fund.java跟fundstock.java則是對應到MySQL的Table。

[repository](src/main/java/demo/repository) repository則是透過JdbcTmplate連接MySQL資料庫，提供查詢、新增、修改、刪除等功能。並根據前端提供其他的查詢方法。

[Controller](src/main/java/demo/controller) 根據前端回傳不同的method跟路徑，來分配執行的方法，並將結果回傳給前端。

前端: 共有fund.html、fundstock.html、fundshareholding.html三個網頁。
fund.html 
fundstock.html
fundshareholding.html
