select f.fid,f.fname,s.sid,s.symbol,sum(s.share),count(s.symbol)
from finweb.fund f left join finweb.fundstock s on f.fid=s.fid
group by s.symbol,f.fid


select f.fid,f.fname,s.sid,s.symbol,sum(s.share),count(s.symbol)
from finweb.fund f left join finweb.fundstock s on f.fid=s.fid
where f.fid=2
group by s.symbol,f.fid