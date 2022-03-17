	//頁面載入完成後要執行的方法
	$(document).ready(function() {
		//驗證註冊
		$('#myForm').validate({
			onsubmit: false,
			onkeyup: false,
			rules: {
				fname: {	
					rangelength: [2, 50]
				}
			},
			messages: { 
				fname: { 
					required: "請輸入基金名稱",
					rangelength: "基金名稱長度必須介於{0}~{1}之間"
				}
			}
		});
		
		table_list();
		//註冊相關事件
		$('#add').on('click', function() {
			addOrUpdate('POST');
		});
		
		$('#myTable').on('click', 'tr', function(){
			getItem(this);
		});
		
		$('#upt').on('click', function() {
			addOrUpdate('PUT');
		});
		$('#del').on('click', function() {
			del();
		});
		$('#rst').on('click', function() {
			btnAttr(0);
			document.getElementById('myForm').reset();
			
		});
	});
	
	function queryPage(pageNumber) {
		var path = "./mvc/demo/fund/";
		if(pageNumber>0){
			path = "./mvc/demo/fund/page/"+pageNumber;
		}
		//取得所有 fund 資料
		$.get(path, function(datas,status){
			console.log(datas);
			console.log(status);
			//清除目前mtTable舊有資料
			$('#myTable tbody > tr').remove();
			//將資料datas放入myTable中
			$.each(datas, function(i, item) {
				var html = '<tr><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td></tr>';
				var fundstocksString = '';
				console.log(item.fundstocks);
				if(item.fundstocks==null||item.fundstocks=='') {
					fundstocksString = '無成分股';
				} else {
					$.each(item.fundstocks, function(f, fundstock){
					fundstocksString +='[股票序號:'+fundstock.sid +'，股票代號:'+ fundstock.symbol +'，股票數量:'+fundstock.share+']<br> ';
					});
				};
				
				$('#myTable').append(String.format(html, item.fid, item.fname, item.createtime, fundstocksString));
			});
		});
		//取得頁數
		$.get('./mvc/demo/fund/totalPagecount', function(data, status){
			console.log(data);	
			$('#page >span').remove();
			for(i=1; i<=data; i++){
				var html = '<span class="mylink" onclick="queryPage({0});">{0}</span><span>&nbsp;</span>';
				$('#page').append(String.format(html, i));
			}
		});
	}
	
	
	function getItem(elem) {
		var fid =$(elem).find('td').eq(0).text().trim();
		console.log(fid);
		var path = './mvc/demo/fund/' + fid;
		var func = function(fund, status) {
			console.log(fund);
			//將資料配置到myForm表單中
			$('#myForm').find('#fid').val(fund.fid);
			$('#myForm').find('#fname').val(fund.fname);
			//修改 btn狀態
			btnAttr(1);
			//該筆資料是否能刪除，取決於fund物件下面是否有fundstack陣列物件
			console.log(fund.fundstocks.length);
			if(fund.fundstocks.length>0){
				$('#myForm').find('#del').attr('disabled',true);
			}
		};
		$.get(path, func);
	}
	
	function addOrUpdate(method) {
		//驗證
		console.log($('#myForm').valid());
		if(!$('#myForm').valid()){
			return;
		}
		//將表單欄位資料物件序列化
		var jsonObject = $('#myForm').serializeObject();
		var jsonString = JSON.stringify(jsonObject);
		console.log(jsonObject);
		//將資料傳遞到後端
		$.ajax({
			url: "./mvc/demo/fund/",
			type: method,
			contentType: 'application/json;charset=utf-8',
			data: jsonString,
			success: function(respData) {
				console.log(respData);
				//資料更新
				table_list();
				//rst狀態
				btnAttr(0);
				//form reset
				$('#myForm').trigger('reset');
				if(method=='POST'){
					alert('新增成功');	
				}else{
					alert('修改成功');	
				}
			}
		});
		
	}
	
	function del() {
		//將表單欄位資料物件序列化
		var jsonObject = $('#myForm').serializeObject();
		var jsonString = JSON.stringify(jsonObject);
		var fid = $('#myForm').find('#fid').val();
		console.log(jsonObject);
		//將資料傳遞到後端
		$.ajax({
			url: "./mvc/demo/fund/" + fid,
			type: 'DELETE',
			contentType: 'application/json;charset=utf-8',
			data: jsonString,
			success: function(respData) {
				console.log(respData);
				//資料更新
				table_list();
				//rst狀態
				btnAttr(0);
				$('#myForm').trigger('reset');
				alert('刪除成功');	
			},
			error: function(http, textStatus, errorThrown) {
				console.log("http" + http);
				console.log("textStatus"+textStatus);
				console.log("errorThrown"+errorThrown);
				var errorInfoText = JSON.stringify(http);
				if(errorInfoText.includes('REFERENCES')){
					alert('該筆資料無法刪除，原因: 此基金下有成分股的參照');
				}else{
					alert('該筆資料無法刪除，原因: ' + textStatus);
				};

			}
		});
		
	}
	//Form List的資料列表
	function table_list() {
		queryPage(0);
	}
	
	function btnAttr(status) {
		//修改，刪除狀態
		$('#myForm').find('#add').attr('disabled',status != 0);
		$('#myForm').find('#upt').attr('disabled',status == 0);
		$('#myForm').find('#del').attr('disabled',status == 0);
		
	}
