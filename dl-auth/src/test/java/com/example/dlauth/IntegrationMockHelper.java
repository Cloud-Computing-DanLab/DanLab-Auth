package com.example.dlauth;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class IntegrationMockHelper extends AbstractTestExecutionListener {
    public static final String NON_ASCII = "NonAsciiCharacters";

    // 무작위로 선택한 포트를 주입
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        // 현재 실행된 포트 지정
        RestAssured.port = this.port;

        // 외래키 제약 조건 해제
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0"); // MySQL의 모든 외래 키 제약 조건 해제

        // public 스키마에 있는 모든 테이블 조회 후 'TRUNCATE TABLE (TABLE_NAME);' 형식의 쿼리로 생성
        List<String> truncateAllTablesQuery = jdbcTemplate.queryForList(
                "SELECT CONCAT('TRUNCATE TABLE ', table_name, ';') AS q " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema = 'danlab'"
                , String.class);

        // 데이터베이스의 모든 테이블 초기화
        truncateAllTables(truncateAllTablesQuery);

        // 외래키 제약 조건 재설정
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1"); // MySQL의 모든 외래 키 제약 조건 재설정
    }

    private void truncateAllTables(List<String> truncateAllTablesQuery) {
        // truncate 쿼리 실행
        for (String truncateQuery : truncateAllTablesQuery) {
            jdbcTemplate.execute(truncateQuery);
        }
    }
}
