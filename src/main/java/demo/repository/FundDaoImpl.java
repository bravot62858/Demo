package demo.repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import demo.entity.Fund;
import demo.entity.Fundstock;

@Repository
public class FundDaoImpl implements FundDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Fund> queryAll() {
		String sql = "select f.fid , f.fname , f.createtime , "
				+ "s.sid as fundstocks_sid , s.fid as fundstocks_fid , s.symbol as fundstocks_symbol , s.share as fundstocks_share  "
				+ "from fund f left join fundstock s "
				+ "on f.fid = s.fid";
		ResultSetExtractor<List<Fund>> resultSetExtractor = 
				JdbcTemplateMapperFactory.newInstance()
				.addKeys("fid") // Fund 的主鍵
				.newResultSetExtractor(Fund.class);
		
		return jdbcTemplate.query(sql, resultSetExtractor);
	}

	@Override
	public List<Fund> queryPage(int offset) {
		if(offset < 0) {
			return queryAll();
		}
		
		String sql = "select f.fid, f.fname, f.createtime from fund f order by f.fid";
		sql += String.format(" limit %d offset %d ", FundDao.LIMIT, offset);
		RowMapper<Fund> rm = (ResultSet rs, int rowNum) -> {
			Fund fund = new Fund();
			fund.setFid(rs.getInt("fid"));
			fund.setFname(rs.getString("fname"));
			fund.setCreatetime(rs.getDate("createtime"));
			// 根據 fid 查詢 fundstock 列表
			String sql2 = "select s.sid, s.fid, s.symbol, s.share "
					+ "from fundstock s "
					+ "where s.fid = ? "
					+ "order by s.sid";
			Object[] args = {fund.getFid()};
			List<Fundstock> fundstocks = jdbcTemplate.query(
					sql2, 
					args, 
					new BeanPropertyRowMapper<Fundstock>(Fundstock.class));
			fund.setFundstocks(fundstocks);
			return fund;
		};
		return jdbcTemplate.query(sql, rm);
	}

	@Override
	public Fund get(Integer fid) {
		String sql = "select f.fid, f.fname, f.createtime from fund f where f.fid=?";
		Fund fund = jdbcTemplate.queryForObject(sql, new Object[] {fid}, new BeanPropertyRowMapper<Fund>(Fund.class));
		sql = "select s.sid, s.fid, s.symbol, s.share from fundstock s where s.fid = ?";
		List<Fundstock> fundstocks = jdbcTemplate.query(
				sql, 
				new Object[] {fund.getFid()}, 
				new BeanPropertyRowMapper<Fundstock>(Fundstock.class));
		fund.setFundstocks(fundstocks);
		return fund;
	}
	
	@Override
	public int count() {
		String sql = "select count(*) from fund";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public int add(Fund fund) {
		String sql = "insert into fund(fname) values (?)";
		return jdbcTemplate.update(sql, fund.getFname());
	}

	@Override
	public int update(Fund fund) {
		String sql = "update fund set fname=? where fid=?";
		return jdbcTemplate.update(sql, fund.getFname(), fund.getFid());
	}

	@Override
	public int delete(Integer fid) {
		String sql = "delete from fund where fid=?";
		return jdbcTemplate.update(sql, fid);
	}

	//queryForObject只能回傳1筆資料
	public List<Map<String, Object>> queryFundshareholding(Integer fid) {
		String sql = "select s.symbol,sum(s.share) as total "
				+ "from finweb.fund f left join finweb.fundstock s on f.fid=s.fid "
				+ "where f.fid=? "
				+ "group by s.symbol,f.fid";
		RowMapper<Map<String, Integer>> rm = (ResultSet rs, int rowNum)->{
			Map<String, Integer> tmp = new HashMap<>();
			tmp.put(rs.getString("symbol"), rs.getInt("total"));
			System.out.println(tmp);
			return tmp;	
		};
		
		return jdbcTemplate.queryForList(sql,fid);
	}
}