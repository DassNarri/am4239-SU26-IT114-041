public class BankAccount {
    private double balance;

    public double getBalance() { return this.balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            System.out.println("Error: Deposit must be positive.");
        }
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        account.deposit(-50.0); // Should print error message
        System.out.println("Current Balance: " + account.getBalance());
    }
}
