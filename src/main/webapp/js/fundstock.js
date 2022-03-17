$(document).ready(function(){
	
	$('#myForm').validate({
		onsubmit: false,
		onkeyup: false,
		rules:{
			symbol:{
				required: true
			},
			share:{
				required: true,
				min:1000
			},
			fid:{
				required: true
			}
		},
		messages: {
			symbol:{
				required: "請輸入股票代號",
			},
			share:{
				required: "請輸入股票數量",
				min: "數量必須大於等於1000"
			},
			fid:{
				required: "請選擇基金"
			}
		}
	});
	
	table_list();	
	//新增
	$('#add').on('click', function(){
		addOrUpdate('POST');
	});
	//取得欲修改物件
	$('#myTable').on('click', 'tr', function(){
		getItem(this);
	});
	//修改
	$('#upt').on('click', function(){
		addOrUpdate('PUT');
		drawChart();
	});
	//刪除
	$('#del').on('click', function(){
		dele();
	});
	//重置
	$('#rst').on('click', function(){
		btnAttr(0);
		document.getElementById('myForm').reset();
		
	});
});

function table_list(){
	queryPage(0);	
};

function queryPage(pageNumber){
	var path = './mvc/demo/fundstock/';
	if(pageNumber > 0){
		path = './mvc/demo/fundstock/page/' + pageNumber;
	}
	//取得fundstock
	$.get(path, function(datas){
		console.log(datas);
		$('#myTable tbody > tr').remove();
		$.each(datas, function(f,fundstock){
			var html = '<tr><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td></tr>';
			
		$('#myTable').append(String.format(html, fundstock.sid, fundstock.symbol, fundstock.share, fundstock.fund.fname));
		});
	});
	//取得頁數
	$.get('./mvc/demo/fundstock/totalPagecount', function(datas){
		console.log(datas);
		$('#page > span').remove();
		for(i=1; i<=datas; i++){
			var html = '<span class="mylink" onclick="queryPage({0});">{0}</span><span>&nbsp;</span>';
			$('#page').append(String.format(html, i));
		}
	});
	//加入基金選單
		
		
	$.get('./mvc/demo/fund/', function(datas,status){
		console.log(datas);
		$('#fid>option').remove();
		$.each(datas, function(i, item){
			var html = '<option id={0} value={0}>{1}</option>';
			$('#fid').append(String.format(html,item.fid,item.fname));
		});
			$('#fid').prepend('<option selected id="" value=""> </option>');
	});
};

function getItem(elem){
	var sid = $(elem).find('td').eq(0).text().trim();
	console.log(sid);
	$.get('./mvc/demo/fundstock/'+sid, function(s, status){
		console.log(s);
		btnAttr(1);
		$('#myForm').find('#sid').val(s.sid);
		$('#myForm').find('#symbol').val(s.symbol);
		$('#myForm').find('#share').val(s.share);
		$('#myForm').find('#fid').val(s.fid);
	});
}

function btnAttr(status){
	$('#myForm').find('#add').attr('disabled',status!=0);
	$('#myForm').find('#upt').attr('disabled',status==0);
	$('#myForm').find('#del').attr('disabled',status==0);
};

function addOrUpdate(method){
	
	if(!$('#myForm').valid()){
		return;
	}
	
	var jsonObject = $('#myForm').serializeObject();
	var jsonString = JSON.stringify(jsonObject);
	console.log(jsonObject);
	$.ajax({
		url: "./mvc/demo/fundstock/",
		type: method,
		contentType: 'application/json;charset=utf-8',
		data: jsonString,
		success: function(respData){
			console.log(respData);
			table_list();
			btnAttr(0);
			document.getElementById('myForm').reset();
			drawChart();
			if(method=='POST'){
				alert('新增成功');	
			}else{
				alert('修改成功');	
			}
		},
		error: function(http, textStatus, errorThrown){
			console.log("http" + http);
			console.log("textStatus"+textStatus);
			console.log("errorThrown"+errorThrown);
			var errorInfoText = JSON.stringify(http);
			console.log(errorInfoText);
			if(errorInfoText.includes('stockNotFund')){
					alert('股票代號不存在');
			}
		}
	});	
};

function dele(){
	var jsonObject = $('#myForm').serializeObject();
	var jsonString = JSON.stringify(jsonObject);
	var sid = $('#myForm').find('#sid').val();
	$.ajax({
		url: "./mvc/demo/fundstock/"+sid,
		type: 'DELETE',
		data: jsonString,
		success: function(res){
			table_list();
			btnAttr(0);
			document.getElementById('myForm').reset();
			drawChart();
			alert('刪除成功');	
		}
	});
	
};

