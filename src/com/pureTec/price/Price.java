package com.pureTec.price;



import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pureTec.admin.Admin;



@SuppressWarnings("serial")
public class Price extends Model<Price> {
	public static final Price me = new Price();
	
	
	public Page<Price> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "SELECT  * ", "from pricelist order by month ");

	}


	public boolean existMonth(int month) {
		// TODO Auto-generated method stub
		if (Db.queryLong("select count(*) from pricelist where month='"+month+"'")>0)
			return true;	
		return false;
	}

	
}
