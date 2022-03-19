# Demo
該Demo作品共有使用SpringMVC、HTML、Bootstrap、jQuery等框架、技術撰寫。

- 後端: 使用SpringMVC框架，使用JdbcTmplate連接MySQL資料庫。

  [entity](src/main/java/demo/entity) ：entity裡的fund.java跟fundstock.java則是對應到MySQL的Table。

  [repository](src/main/java/demo/repository) ：repository則是透過JdbcTmplate連接MySQL資料庫，提供查詢、新增、修改、刪除等功能。並根據前端提供其他的查詢方法。

  [Controller](src/main/java/demo/controller) ：根據前端回傳不同的method跟路徑，來分配執行的方法，並將結果回傳給前端。

- 前端: 共有fund.html、fundstock.html、fundshareholding.html三個網頁，放置在webapp底下。

  [webapp](src/main/webapp)

  fund.html ：顯示所有基金，透過$.get()、$.ajax等技術從後端撈取資料並顯示在網頁上。

  fundstock.html ：顯示所有股票及所屬基金。新增股票時會透過YahooFinance驗證股票代號是否有效。並使用Google Chart顯示所有股票的數量。

  fundshareholding.html ：可下拉選取每個基金所持有的股票代號及數量。並利用Google Chart顯示持股的百分比。
