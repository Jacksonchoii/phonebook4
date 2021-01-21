package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PersonVo;

@Repository /* 설정값 어노테이션 확인 */
public class PhoneDao {

	// 필드
	@Autowired
	private DataSource dataSource;
	
	
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 생성자
	// 메소드g/s
	// 메소드일반

	// db접속
	private void getConnection() {
		try {
			conn = dataSource.getConnection();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 5. 자원정리
	private void close() {
		
	    try {
	        if (rs != null) {
	            rs.close();
	        }                
	        if (pstmt != null) {
	            pstmt.close();
	        }
	        if (conn != null) {
	            conn.close();
	        }
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    }

	}

	// phone 저장
	public int personInsert(PersonVo personvo) {
		
		int count = 0;
		
		getConnection();
		
		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			//INSERT INTO PERSON VALUES (seq_person_id.nextval, '이효리', '010-1111-1111', '02-1111-1111');
		    String query ="";
		    query += " INSERT INTO PERSON ";
		    query += " VALUES (seq_person_id.nextval, ?, ?, ?)";
		    
		    pstmt = conn.prepareStatement(query);
		    pstmt.setString(1, personvo.getName());
		    pstmt.setString(2, personvo.getHp());
		    pstmt.setString(3, personvo.getCompany());
		    
		    count = pstmt.executeUpdate();
			
			// 4.결과처리
		    System.out.println("[Dao]" + count + "건이 저장되었습니다.");

		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		   
		close();
		return count;
	}
	
	// phone 삭제
	public int personDelete(int personId) {
		
		int count = 0;
		getConnection();
		
		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			//DELETE FROM PERSON WHERE person_id = 5;
			String query = "";
			query += " DELETE FROM PERSON ";
			query += " WHERE person_id = ? ";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, personId);
			
			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			 System.out.println("[Dao]" + count + "건이 삭제되었습니다.");
		
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 

		close();
		return count;
	}
	
	// phone 수정
	public int personUpdate(PersonVo personVo) {
		
		int count = 0;
		getConnection();

		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			//UPDATE PERSON SET name = '유정재', hp = '010-9999-9999', company = '02-9999-9999' WHERE person_id = 4;
			String query = "";
			query += " UPDATE PERSON ";
			query += " SET 	  name = ?, ";
			query += "  	  hp = ?,";
			query += " 		  company = ? ";
			query += " WHERE person_id = ? ";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, personVo.getName());
			pstmt.setString(2, personVo.getHp());
			pstmt.setString(3, personVo.getCompany());
			pstmt.setInt(4, personVo.getPersonId());
			
			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			 System.out.println("[Dao]" + count + "건이 수정되었습니다.");
		
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	// phone 리스트
	public List<PersonVo> getPersonList() {
		
		List<PersonVo> phoneList = new ArrayList<PersonVo>();
		
		getConnection();
		
		try {
		    // 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " SELECT person_id, ";
			query += " 		  name, ";
			query += " 		  hp, ";
			query += " 		  company ";
			query += " FROM person ";
			
			pstmt = conn.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
		    // 4.결과처리
			while(rs.next()) {
				
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getNString("hp");
				String company = rs.getNString("company");
				
				PersonVo pvo = new PersonVo(personId, name, hp, company);
				
				phoneList.add(pvo);
				
			}

		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
		close();
		return phoneList;
	}
	
	
	// phone 검색
	
	//검색(like문을 이용한 select) --> 실패 더 공부하기 
	public List<PersonVo> personSearch(String str) {
		List<PersonVo> personList = new ArrayList<PersonVo>();                                                
	        
		//DB접속                                                                                                  
		getConnection();                                                                                        
			                                                                                                        
		try {                                                                                                   
			   // 3. SQL문 준비 / 바인딩 / 실행  
				
			String query = "";
			query += " select  person_id,  ";
			query += "         name,       ";
			query += "         hp,         ";
			query += "         company     ";
			query += " from person         ";
			query += " where name like ?   ";
			query += " or hp like ?        ";
			query += " or company like ?   ";
			query += " order by person_id  ";
				
			pstmt = conn.prepareStatement(query);
				
			pstmt.setString(1, "%" + str + "%");
			pstmt.setString(2, "%" + str + "%");
			pstmt.setString(3, "%" + str + "%");
				
			rs = pstmt.executeQuery();
				
			while(rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
					
				PersonVo pvo = new PersonVo(personId, name, hp, company);
				personList.add(pvo);
			}
				
			// 4. 결과 처리                                                                                                   
		    System.out.println("[검색어 ' " + str + " ' 이(가) 포함된 리스트 입니다.]");                                                                                                    
			} catch (SQLException e) {                                                                              
		    System.out.println("error:" + e);                                                                   
			}                                                                                                       
			                                                                                                        
			//자원정리                                                                                                  
			close();                                                                                                
		
			return personList;
		}
	
		//사람 1명 정보 가져오기
	public PersonVo getPerson(int personId) {
		PersonVo personVo = null;
		
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query= "";
			query += " select person_id, ";
			query += " 		  name, ";
			query += " 		  hp, ";
			query += " 		  company ";
			query += " from person ";
			query += " where person_id = ? ";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, personId);
			
			rs = pstmt.executeQuery();
			
			
			// 4. 결과 처리 
			while(rs.next()) {
				int personID = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				personVo = new PersonVo(personID, name, hp, company);
			}
			
			
			
		}catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		
		close();
		return personVo;
	}

}
