package club.guoshizhan.service;

import club.guoshizhan.PO.User;

public interface IUserService {

    User checkUser(String username, String password);

}
