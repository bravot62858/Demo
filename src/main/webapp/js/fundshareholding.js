/**
 * 
 */
 $(document).ready(function() {
	
 var num=1;
 	
	getFundOption();
 	getNum();
	getfundDatas();
	
	
});

function getfundDatas(){
	getNum();
	if(num==null||num==0){
		return
	}
	$.get('./mvc/demo/Fundshareholding/'+num, function(datas){

		drawChart();
		
		$('#fundshare tbody> tr').remove();
		$.each(datas,function(symbol,share){
			//console.log(datas);
			//console.log(symbol); //symbol
			//console.log(share); //share
			var html = '<tr><td>{0}</td><td>{1}</td></tr>';
			//console.log(html);
			//
			$('#fundshare').append(String.format(html,symbol,share));
			
			drawChart();
		});
	});
}

function getFundOption(){
	$.get('./mvc/demo/fund/', function(datas,status){
		//console.log(datas);
		$('#fid>option').remove();
		$.each(datas, function(i, item){
			var html = '<option id={0} value={0}>{1}</option>';
			$('#fid').append(String.format(html,item.fid,item.fname));
		});
		$('#fid').prepend('<option selected id="0" value="0">請選擇基金</option>');
		getNum();
	});
	
}



function getNum(){
	 num = $('#fid').val();
	//console.log(num);
}