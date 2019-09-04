package com.eeit109team6.memberDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IMemberDao {

	public int add(Member m, Connection conn) throws SQLException;

	public void update(Member m, Connection conn) throws SQLException;

	public void delete(Member m, Connection conn) throws SQLException;

	public ArrayList<Member> fintAll(Connection conn) throws SQLException;

	public Member fintById(Member m, Connection conn) throws SQLException;

	public Member login(Member m, Connection conn) throws SQLException;

	public boolean openActive(Member m, Connection conn) throws SQLException;

	public void forgetPwd(Member m, Connection conn, String token) throws SQLException;
	public void changePwd(Member m, Connection conn, String oldpassowrd) throws SQLException;



}
