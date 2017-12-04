package com.epam.service;

import com.epam.domain.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public void insert(User user) {
        String sql = "INSERT INTO public.person VALUES (?, ?, ?, ?)";
        getJdbcTemplate().update(sql, user.getId(), user.getName(), user.getBirthday().toString(), user.getEmail());
    }

    public void remove(String id) {
        String sql = "DELETE FROM public.person WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }

    public User getById(String id) {
        String sql = "select * from public.person where id = ?";
        return getJdbcTemplate().query(sql, new Object[]{id}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            //user.setBirthday(LocalDateTime.parse(rs.getString("birhtday")));
            return user;
        }).get(0);
    }

    public List<User> getAll() {
        String sql = "select * from public.person";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
        List<User> users = Lists.newArrayList();
        for (Map row : rows) {
            User user = new User();
            user.setId((String) row.get("id"));
            user.setName((String) row.get("name"));
            user.setEmail((String) row.get("email"));
            users.add(user);
        }
        return users;
    }
}
