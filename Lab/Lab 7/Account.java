package lab7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private final boolean isLock;

	public Account(boolean isLock) {
		this.isLock = isLock;
	}

	public boolean isLock() {
		return isLock;
	}

	private static final Lock lock = new ReentrantLock();

    private double balance;

    /**
     *
     * @param money
     */
    public void depositLock(double money) {
		lock.lock();
        try {
			double newBalance = balance + money;
			try {
			    Thread.sleep(10);   // Simulating this service takes some processing time
			}
			catch(InterruptedException ex) {
			    ex.printStackTrace();
			}
			balance = newBalance;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			lock.unlock();
		}

    }

	public synchronized void depositSynchronized(double money) {
		try {
			double newBalance = balance + money;
			try {
				Thread.sleep(10);   // Simulating this service takes some processing time
			}
			catch(InterruptedException ex) {
				ex.printStackTrace();
			}
			balance = newBalance;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    public double getBalance() {
        return balance;
    }
}