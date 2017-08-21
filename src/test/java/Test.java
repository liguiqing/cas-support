import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/

/**
 * 
 * @author liguiqing
 * @Date 2017年6月13日
 * @Version
 */
public class Test {

	public static void main(String[] args) {
		DruidDataSource ds = new DruidDataSource();
//		ds.setUrl(				"jdbc:mysql://rm-wz9cwern4k7l056r4o.mysql.rds.aliyuncs.com:3306/easytntv2?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true");
		ds.setUrl(				"jdbc:mysql://192.168.1.251:3306/easytntv2_dev?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true");
		if (args.length > 0)
			ds.setUrl("jdbc:mysql://" + args[0]
					+ ":3306/easytntv2?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true");
//		ds.setUsername("easytnt");
//		ds.setPassword("Easytnt001@#");

		ds.setUsername("root");
		ds.setPassword("newa_newc");
		
		Connection cn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			System.out.println("Connection to db : " + ds.getUrl());
			cn = ds.getConnection();
			cn.setAutoCommit(false);
			// st = cn.prepareStatement("select userName from idm_user");
			// rs = st.executeQuery();
			// while(rs.next()) {
			// System.out.println(rs.getString("userName"));
			// }
			String sql = "CREATE  TEMPORARY TABLE HT_REGISTRATION " + "(id INTEGER not NULL AUTO_INCREMENT, "
					+ " first VARCHAR(255), " + " last VARCHAR(255), " + " age INTEGER, " + " PRIMARY KEY ( id ))";
			st = cn.prepareStatement(sql);
			st.execute(sql);
			sql = " insert into HT_REGISTRATION values (1,'first','last',8)";
			st.execute(sql);
			String sql2 = "select * from HT_REGISTRATION";
			close(st);
			st = cn.prepareStatement(sql2);
			rs = st.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("first"));
			}
			//st = cn.prepareStatement("drop table HT_REGISTRATION");
			st.execute();
			cn.commit();
			cn.setAutoCommit(true);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			close(st);
			close(cn);
		}
		System.out.println("+++++++++++++++++==");
	}

	private static void close(AutoCloseable c) {
		try {
			c.close();
		} catch (Exception e) {

		}
	}
}
