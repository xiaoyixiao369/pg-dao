package io.igordonxiao.pgdao;

import io.igordonxiao.model.User;
import junit.framework.Assert;
import org.junit.Test;

/**
 * 用户实体类测试
 */
public class UserTest {

    private Dao dao = new Dao();

    @Test
    public void testSave() {
        User user = new User();
        user.setName("Gordon");
        user.setAge(20);
        User userDb = dao.save(user, User.class);
        Assert.assertEquals("Gordon", userDb.getName());
    }

    @Test
    public void testFindUserById() {
        Long id = 1L;
        User user = dao.findById(id, User.class);
        Assert.assertEquals(id, user.getId());
    }

    @Test
    public void testUpdateUser() {
        User user = dao.findById(1L, User.class);
        user.setName("Gordon Xiao");
        User updatedUser = dao.update(user, User.class);
        Assert.assertEquals("Gordon Xiao", updatedUser.getName());
    }

    @Test
    public void testDeleteUserById() {
        Long id = 1L;
        dao.deleteById(id, User.class);
        Assert.assertEquals(null, dao.findById(id, User.class));
    }

    @Test
    public void testFindPageUsers() {
        PageResult<User> page = dao.findPage(User.class, 15, 1, "id > 0 and createdDate > '2018-03-01 08:12:00'");
        Assert.assertTrue(page.getCount() > 0);
        Assert.assertTrue(page.getData().size() > 0);
    }
}
