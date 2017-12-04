package com.epam.service;

import com.epam.domain.Auditorium;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class AuditoriumService extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public Auditorium getByName(String name) {
        String sql = "select * from public.auditorium where name = ?";
        return getJdbcTemplate().query(sql, new Object[]{name}, (rs, rowNum) ->
                new Auditorium(rs.getString("id"), rs.getInt("seats_number"), rs.getInt("vip_seats"))).get(0);
    }

    public List<Auditorium> getAll() {
        String sql = "select * from public.auditorium";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
        List<Auditorium> auditoriums = Lists.newArrayList();
        for (Map row : rows) {
            auditoriums.add(new Auditorium((String) row.get("id"), (int) row.get("seats_number"), (int) row.get("vip_seats")));
        }
        return auditoriums;
    }
}
