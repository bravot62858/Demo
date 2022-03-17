package demo.repository;

import java.util.List;

import demo.entity.Fundstock;

public interface FundstockDao {
	//每頁5筆
	int LIMIT = 5;
	//全部查詢
	List<Fundstock> queryAll();
	//分頁查詢
	List<Fundstock> queryPage(int offset);
	//取得單筆
	Fundstock get(Integer sid);
	//查詢所有筆數
	int count();
	//新增
	int add(Fundstock fundstock);
	//修改
	int update(Fundstock fundstock);
	//刪除
	int delete(Integer sid);
	
}
