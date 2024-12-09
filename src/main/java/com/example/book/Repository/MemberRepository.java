package com.example.book.Repository;

import com.example.book.Domain.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerMember(Member member) { //회원가입
        String sql = "{ call RegisterMember(?, ?, ?, ?, ?) }";  // 저장 프로시저 호출 SQL

        jdbcTemplate.execute((Connection con) -> {
            try (CallableStatement statement = con.prepareCall(sql)) {
                // 저장 프로시저의 매개변수 설정
                statement.setString(1, member.getMemberId());
                statement.setString(2, member.getMemberPassword());
                statement.setString(3, member.getMemberName());
                statement.setString(4, member.getPhoneNumber());
                statement.setString(5, member.getAddress());

                // 저장 프로시저 실행
                statement.execute();
                return null; // 반환 값 없음
            } catch (SQLException e) {
                throw new RuntimeException("회원 등록 중 오류 발생", e);
            }
        });
    }
    // 회원가입 처리
    public void save(Member member) {
        String sql = "INSERT INTO member (memberid, memberpassword, membername, phonenumber, address, membership, joindate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                member.getMemberId(),
                member.getMemberPassword(),
                member.getMemberName(),
                member.getPhoneNumber(),
                member.getAddress(),
                member.getMembership(),
                new java.util.Date()  // 가입일
        );
    }
    // 회원 ID로 조회
    public Member findByMemberId(String memberId) {     //회원 가입 시, 사용자 ID 중복 검증
        String sql = "SELECT * FROM MEMBER WHERE memberid = ?";
        List<Member> members = jdbcTemplate.query(sql, (rs, rowNum) -> {
            // ResultSet에서 값을 직접 가져와서 Member 객체에 설정
            Member member = new Member();
            member.setMemberId(rs.getString("id"));
            member.setMemberId(rs.getString("member_id"));
            member.setMemberPassword(rs.getString("member_password"));
            member.setMemberName(rs.getString("member_name"));
            member.setPhoneNumber(rs.getString("phone_number"));
            member.setAddress(rs.getString("address"));
            member.setMembership(rs.getString("membership"));
            member.setJoinDate(rs.getTimestamp("join_date"));
            System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzz" + member);
            return member;
        }, memberId);

        return members.isEmpty() ? null : members.get(0);
    }
    public Member findByMemberId1(String memberId) {    //회원 정보 리스트
        // SQL 쿼리에서 member_id를 사용하는 경우로 수정
        String sql = "SELECT * FROM MEMBER WHERE memberid = ?";
        System.out.println("repository1");
        // 쿼리 실행, memberId를 쿼리 파라미터로 전달
        List<Member> members = jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            // ResultSet에서 값을 직접 가져와서 Member 객체에 설정
            Member member = new Member();
            member.setMemberId(rs.getString("MEMBERID"));  // 'member_id' 컬럼을 정확히 사용
            member.setMemberPassword(rs.getString("memberpassword"));
            member.setMemberName(rs.getString("membername"));
            member.setPhoneNumber(rs.getString("phonenumber"));
            member.setAddress(rs.getString("address"));
            member.setMembership(rs.getString("membership"));
            member.setJoinDate(rs.getTimestamp("joindate"));
            return member;
        });

        // 결과가 없으면 null을 반환
        return members.isEmpty() ? null : members.get(0);
    }

    // 로그인 처리
    public Member loginMember(String memberId, String memberPassword) {
        String sql = "SELECT * FROM Member WHERE memberId = ? AND memberPassword = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{memberId, memberPassword}, new MemberRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;  // 로그인 실패
        }
    }
    // 회원 탈퇴 프로시저 호출
    public void removeMember(String memberId) {
        String sql = "{call RemoveMember(?)}";  // 저장 프로시저 호출

        // CallableStatement를 사용하여 프로시저 호출
        jdbcTemplate.update(sql, memberId);
    }
    // RowMapper 정의
    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getString("memberId"),
                    rs.getString("memberPassword"),
                    rs.getString("memberName"),
                    rs.getString("phoneNumber"),
                    rs.getString("address"),
                    rs.getString("membership"),
                    rs.getDate("joinDate")
            );
        }
    }
    // 회원 탈퇴 프로시저 호출

}

