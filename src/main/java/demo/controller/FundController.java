package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import demo.entity.Fund;
import demo.repository.FundDao;


@RestController
@RequestMapping("/demo/fund")
public class FundController {
	private Integer pageNumber;
	@Autowired
	private FundDao fundDao;
	
	@GetMapping("/")
	public List<Fund> index() {
		List<Fund> funds = fundDao.queryAll();
		//將沒有成分股的基金其 fundstocks屬性置空
		funds.stream().filter(f->f.getFundstocks().size()>0 && f.getFundstocks().get(0).getFid()==null).forEach(f->f.setFundstocks(null));
		return funds;
	}
	//取得單筆資料
	@GetMapping("/{fid}")
	public Fund get(@PathVariable("fid") Integer fid) {
		return fundDao.get(fid);
	}
	//計算頁數
	@GetMapping("/totalPagecount")
	public int pagecount() {
		if(fundDao.count()%fundDao.LIMIT!=0) {
			return (fundDao.count() / fundDao.LIMIT)+1;
		}
		return fundDao.count() / fundDao.LIMIT;
	}
	//分頁查詢
	@GetMapping("/page/{pageNumber}")
	public List<Fund> page(@PathVariable("pageNumber") int pageNumber) {
		this.pageNumber = pageNumber;
		int offset = (pageNumber-1) * FundDao.LIMIT;
		List<Fund> funds = fundDao.queryPage(offset);
		return funds;
	}
	
	//新增
	@PostMapping("/")
	public int add(@RequestBody Fund fund) {
		return fundDao.add(fund);
	}
	//修改
	@PutMapping("/")
	public int update(@RequestBody Fund fund) {
		return fundDao.update(fund);
	}
	//刪除
	@DeleteMapping("/{fid}")
	public int delete(@PathVariable("fid") Integer fid) {
		return fundDao.delete(fid);
	}
	
}
