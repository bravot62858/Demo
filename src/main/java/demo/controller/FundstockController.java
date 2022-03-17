package demo.controller;

import java.util.List;
import java.util.Map;
import  static java.util.stream.Collectors.groupingBy;
import  static java.util.stream.Collectors.summingInt;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import demo.entity.Fundstock;
import demo.repository.FundstockDao;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/demo/fundstock")
public class FundstockController {
	private Integer pageNumber;
	@Autowired
	private FundstockDao fundstockDao;
	private YahooFinance yahooFinance;
	
	@GetMapping("/")
	public List<Fundstock> index() {
		List<Fundstock> fundstocks = fundstockDao.queryAll();
		//將沒有成分股的基金其 fundstocks屬性置空
		fundstocks.stream().filter(s->s.getFund().getFid()==null && s.getFund()==null).forEach(s->s.setFund(null));
		return fundstocks;
	}
	//取單筆資料
	@GetMapping("/{sid}")
	public Fundstock get(@PathVariable("sid") Integer sid) {
		return fundstockDao.get(sid);
	}
	//計算股票數量
	@GetMapping("/groupMap")
	public Map<String, Integer> getGroupMap(){
		List<Fundstock> fundstocks = fundstockDao.queryAll();
		return fundstocks.stream().collect(groupingBy(Fundstock::getSymbol, summingInt(Fundstock::getShare)));
	}
	//計算頁數
	@GetMapping("/totalPagecount")
	public int pagecount() {
		if(fundstockDao.count() % fundstockDao.LIMIT!=0) {
			return (fundstockDao.count() / fundstockDao.LIMIT)+1;
		}
		return fundstockDao.count() / fundstockDao.LIMIT;
	}
	//分頁顯示
	@GetMapping("/page/{pageNumber}")
	public List<Fundstock> page(@PathVariable("pageNumber") int pageNumber) {
		this.pageNumber = pageNumber;
		int offset = (pageNumber-1) * FundstockDao.LIMIT;
		return fundstockDao.queryPage(offset);
	}
	//新增
	@PostMapping("/")
	public ResponseEntity<Object> add(@RequestBody Fundstock fundstock) {
		try {
			if (yahooFinance.get(fundstock.getSymbol()).isValid()) {
				int result = fundstockDao.add(fundstock);
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} catch (IOException e) {
			
		}
		return new ResponseEntity<>("stockNotFund", HttpStatus.BAD_REQUEST);	
	}
	//修改
	@PutMapping("/")
	public ResponseEntity<Object> update(@RequestBody Fundstock fundstock) {
		try {
			System.out.println(yahooFinance.get(fundstock.getSymbol()).isValid());
			if (yahooFinance.get(fundstock.getSymbol()).isValid()) {
				int result = fundstockDao.update(fundstock);
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} catch (Exception e) {
			
		}
		return new ResponseEntity<>("stockNotFund", HttpStatus.BAD_REQUEST);	
	}
	//刪除
	@DeleteMapping("/{sid}")
	public int delete(@PathVariable("sid") Integer sid) {
		return fundstockDao.delete(sid);
	}
	
}
