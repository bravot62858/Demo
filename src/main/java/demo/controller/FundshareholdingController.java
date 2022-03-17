package demo.controller;


import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import demo.repository.FundDao;

@RestController
@RequestMapping("/demo/Fundshareholding")
public class FundshareholdingController {
	@Autowired
	private FundDao fundDao;
	//取得持股資訊
	@GetMapping("/{fid}")
	public Map<String, Integer> queryFundshareholding(@PathVariable("fid") Integer fid){
		 Map<String, Integer> resultMap = new HashMap<String, Integer>();
		fundDao.queryFundshareholding(fid).stream().forEach(map->{
			if(map.get("symbol")!=null&&map.get("total")!=null) {
				String symbol = (String)map.get("symbol");
				Integer total = Integer.parseInt(String.valueOf(map.get("total")))  ;
				resultMap.put(symbol, total);
			}
		}); 
		return resultMap;
	}
}
