package TestRunners;

import Pages.Transaction;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;

public class TransactionTestRunner {
    Transaction transaction;

    @Test(priority = 0)
    public void doLogin() throws ConfigurationException, IOException {
        transaction = new Transaction();
        transaction.callingLoginApi();
    }

    @Test(priority = 1)
    public void getAgentBalance() throws IOException {
        transaction = new Transaction();
        transaction.callingGetAgentBalanceApi();
    }

    @Test(priority = 2)
    public void getUserBalance() throws IOException {
        transaction = new Transaction();
        transaction.callingGetUserBalanceApi();
    }

    @Test(priority = 3)
    public void depositToUserAccount() throws IOException {
        transaction = new Transaction();
        transaction.callingDepositApi();
    }

    @Test(priority = 4)
    public void depositToUserAccountWithInsufficientBalance() throws IOException {
        transaction = new Transaction();
        transaction.callingDepositInsufficientBalanceApi();
    }

    @Test(priority = 5)
    public void sendMoneyUserToUser() throws IOException {
        transaction = new Transaction();
        transaction.callingSendMoneyApi();
    }

    @Test(priority = 6)
    public void sendMoneyWithInsufficientBalance() throws IOException {
        transaction = new Transaction();
        transaction.callingSendMoneyWithInsufficientBalanceApi();
    }

    @Test(priority = 7)
    public void sendMoneyFeeVerify() throws IOException {
        transaction = new Transaction();
        transaction.callingSendMoneyFeeVerificationApi();
    }

    @Test(priority = 8)
    public void cashOut() throws IOException {
        transaction = new Transaction();
        transaction.callingCashOutApi();
    }

    @Test(priority = 9, description = "Cashout charge is fixed 10 taka upto 1000 taka")
    public void cashOutMinimumFee() throws IOException {
        transaction = new Transaction();
        transaction.callingCashOutMinimumFeeApi();
    }

    @Test(priority = 10, description = "Cashout charge is fixed 10 taka upto 1000 taka & After 1000 CAshout charge is 1%")
    public void cashOutFee() throws IOException {
        transaction = new Transaction();
        transaction.callingCashOutFeeApi();
    }

    @Test(priority = 11)
    public void cashOutWithInsufficientBalance() throws IOException {
        transaction = new Transaction();
        transaction.callingCashInsufficientBalanceApi();
    }

    @Test(priority = 12)
    public void getStatementByAccountNumber() throws IOException {
        transaction = new Transaction();
        transaction.callingStatementByAccApi();
    }

    @Test(priority = 13)
    public void getStatementByTRNXID() throws IOException {
        transaction = new Transaction();
        transaction.callingStatementByTRNXApi();
    }

    @Test(priority = 14)
    public void getAllUserTransactionList() throws IOException {
        transaction = new Transaction();
        transaction.callingAllTransactionListApi();
    }

}
