package club.guoshizhan.service.impl;

import club.guoshizhan.PO.User;
import club.guoshizhan.mapper.UserRepository;
import club.guoshizhan.service.IUserService;
import club.guoshizhan.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        // 使用了 MD5 对密码加密
        return userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
    }

}
