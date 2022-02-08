package TestRunners;

import Pages.User;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UserTestRunner {
    User user;

    @Test(priority = 0)
    public void loginWithValidCredentials() throws IOException, ConfigurationException {
        user = new User();
        user.callingLoginApi();
    }

    @Test(priority = 1)
    public void loginWithInvalidEmail() throws IOException {
        user = new User();
        user.callingLoginApiUsingInvEmail();
    }

    @Test(priority = 2)
    public void loginWithIncorrectPass() throws IOException {
        user = new User();
        user.callingLoginApiUsingIncPass();
    }

    @Test(priority = 3)
    public void getUserList() throws IOException {
        user = new User();
        user.callingGetUserListApi();
    }

    @Test(priority = 4)
    public void getUserListWithoutAuthKey() throws IOException {
        user = new User();
        user.callingGetUserListApiWithoutAuth();
    }

    @Test(priority = 5)
    public void searchUserByPhoneNumber() throws IOException {
        user = new User();
        user.callingSearchUserApi();
    }

    @Test(priority = 6)
    public void searchUserByWrongPhoneNumber() throws IOException {
        user = new User();
        user.callingSearchUserApiIncPhone();
    }

    @Test(priority = 7)
    public void searchUserByRole() throws IOException {
        user = new User();
        user.callingSearchUserApiByRole();
    }

    @Test(priority = 8)
    public void generateUser() throws ConfigurationException, IOException {
        user = new User();
        user.generateUser();
    }

    @Test(priority = 9)
    public void createNewUser() throws IOException {
        user = new User();
        user.callingCreateUserApi();
    }

    @Test(priority = 10)
    public void createExistingCustomer() throws IOException {
        user = new User();
        user.callingCreateExistingUserApi();
    }

    @Test(priority = 11)
    public void updateUser() throws IOException {
        user = new User();
        user.callingUpdateUserApi();
    }

    @Test(priority = 12)
    public void updateUserByProps() throws IOException {
        user = new User();
        user.callingUpdateUserByPropsApi();
    }

    @Test(priority = 13)
    public void deleteUserWithInvalidID() throws IOException {
        user = new User();
        user.callingDeleteUSerInvIDApi();
    }


}
