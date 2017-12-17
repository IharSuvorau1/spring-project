package com.epam.service;

import com.epam.domain.CinemaEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventService extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public void insert(CinemaEvent event) {
        String sql = "INSERT INTO public.event VALUES (?, ?, ?, ?, ?)";
        event.getDates().forEach(date -> getJdbcTemplate().update(sql, event.getId(), event.getName(),
                event.getPrice(), event.getRating(), date));
    }

    public void remove(String id) {
        String sql = "DELETE FROM public.event WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }

    public CinemaEvent getById(String id) {
        String sql = "select * from public.event where id = ?";
        List<CinemaEvent> events = getJdbcTemplate().query(sql, new Object[]{id}, new EventMapper());
        return !events.isEmpty() ? events.get(0) : null;
    }

    public CinemaEvent getByName(String name) {
        String sql = "select * from public.event where name = ?";
        List<CinemaEvent> events = getJdbcTemplate().query(sql, new Object[]{name}, new EventMapper());
        return !events.isEmpty() ? events.get(0) : null;
    }

    public List<CinemaEvent> getAll() {
        String sql = "select * from public.event";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
        List<CinemaEvent> events = Lists.newArrayList();
        rows.forEach(row -> events.add(getEvent(row)));
        return events;
    }

    public List<CinemaEvent> getForDateRange(LocalDateTime from, LocalDateTime to) {
        List<CinemaEvent> events = getAll();
        return events.stream().filter(event -> event.getDates().stream().filter(from::isBefore).count() > 0 &&
                event.getDates().stream().filter(to::isAfter).count() > 0).collect(Collectors.toList());
    }

    public List<CinemaEvent> getNextEvents(LocalDateTime to) {
        List<CinemaEvent> events = getAll();
        return events.stream().filter(event ->
                event.getDates().stream().filter(LocalDateTime.now()::isBefore).count() > 0 &&
                        event.getDates().stream().filter(to::isAfter).count() > 0).collect(Collectors.toList());
    }

    private CinemaEvent getEvent(Map<String, Object> row) {
        CinemaEvent event = new CinemaEvent();
        event.setId((String) row.get("id"));
        event.setName((String) row.get("name"));
        event.setPrice(BigDecimal.valueOf((long) row.get("price")));
        event.setRating(CinemaEvent.EventRatingEnum.valueOf((String) row.get("rating")));
        event.setDates(Sets.newHashSet(LocalDateTime.now()));
        return event;
    }

    private class EventMapper implements RowMapper {
        @Override
        public CinemaEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            CinemaEvent event = new CinemaEvent();
            event.setId(rs.getString("id"));
            event.setName(rs.getString("name"));
            event.setPrice(BigDecimal.valueOf(rs.getInt("price")));
            event.setRating(CinemaEvent.EventRatingEnum.valueOf(rs.getString("rating")));
            event.setDates(Sets.newHashSet(LocalDateTime.now()));
            return event;
        }
    }
}
