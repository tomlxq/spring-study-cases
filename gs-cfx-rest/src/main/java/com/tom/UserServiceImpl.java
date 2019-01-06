package com.tom;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getUsers() {
        return Storage.list;
    }

    @Override
    public Response delete(int id) {
        List<User> result = Storage.list.stream()
                .filter((User user) -> id != user.getId())
                .collect(Collectors.toList());
        Storage.list = result;

        return new Response(0, "success:" + JSON.toJSONString(Storage.list));
    }

    @Override
    public Response insert(User user) {
        Storage.list.add(user);
        return new Response(0, "success:" + JSON.toJSONString(Storage.list));
    }

    @Override
    public Response update(User user) {
        Storage.list.forEach(vo -> {
            if (vo.getId() == user.getId()) {
                vo = user;
                return;
            }
        });
        return new Response(0, "success:" + JSON.toJSONString(Storage.list));
    }

    @Override
    public User getUser(int id) {
        List<User> result = Storage.list.stream()
                .filter((User user) -> id == user.getId())
                .collect(Collectors.toList());
        return result.get(0);
    }
}
