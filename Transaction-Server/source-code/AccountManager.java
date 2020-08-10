import java.util.*;

public class AccountManager {
	ArrayList<Integer> accounts = new ArrayList<Integer>();
	
	public AccountManager(int numAccounts, int balance) {
		for(int i = 0; i < numAccounts; i++) {
			accounts.add(balance);
		}
	}
	
	public int readBalance(int accountNo) {
		return accounts.get(accountNo - 1);
	}
	
	public void writeBalance(int accountNo, int amount) {
		accounts.set(accountNo - 1, amount);
	}
}
