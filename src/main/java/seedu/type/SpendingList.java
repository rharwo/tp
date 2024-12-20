package seedu.type;

import seedu.classes.Parser;
import seedu.classes.Ui;
import seedu.recurrence.Recurrence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a list of spendings with budget settings.
 */
public class SpendingList extends ArrayList<Spending> {
    private double dailyBudget;
    private double monthlyBudget;
    private double yearlyBudget;

    /**
     * Constructs an empty SpendingList with default budget values.
     */
    public SpendingList() {
        super();
        dailyBudget = 0;
        monthlyBudget = 0;
        yearlyBudget = 0;
    }

    /**
     * Constructs a SpendingList initialized with the data from another SpendingList.
     *
     * @param spendings The SpendingList to copy data from.
     */
    public SpendingList(SpendingList spendings) {
        super(spendings);  // Initialise with data in storage
        dailyBudget = 0;
        monthlyBudget = 0;
        yearlyBudget = 0;
    }

    /**
     * Returns the daily budget.
     *
     * @return The daily budget.
     */
    public double getDailyBudget() {
        return dailyBudget;
    }

    /**
     * Returns the monthly budget.
     *
     * @return The monthly budget.
     */
    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    /**
     * Returns the yearly budget.
     *
     * @return The yearly budget.
     */
    public double getYearlyBudget() {
        return yearlyBudget;
    }

    /**
     * Sets the daily budget.
     *
     * @param dailyBudget The daily budget to set.
     */
    public void setDailyBudget(double dailyBudget) {
        this.dailyBudget = Math.round(dailyBudget * 100.0) / 100.0;
    }

    /**
     * Sets the monthly budget.
     *
     * @param monthlyBudget The monthly budget to set.
     */
    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = Math.round(monthlyBudget * 100.0) / 100.0;
    }

    /**
     * Sets the yearly budget.
     *
     * @param yearlyBudget The yearly budget to set.
     */
    public void setYearlyBudget(double yearlyBudget) {
        this.yearlyBudget = Math.round(yearlyBudget * 100.0) / 100.0;
    }

    /**
     * Calculates the total spending for the current calendar month.
     *
     * @return The total spending for the current month.
     */
    public double getMonthlySpending() {
        return getMonthlySpending(LocalDate.now());
    }

    /**
     * Calculates the total spending for the specified month of the given date.
     *
     * @param currentDate The date used to specify the month to calculate spending.
     * @return The total spending for the specified month.
     */
    public double getMonthlySpending(LocalDate currentDate){
        double spendingTotal = 0;
        for (Spending spending : this) {
            if (isThisMonth(spending.getDate(), currentDate)) {
                spendingTotal = spendingTotal + spending.getAmount();
            }
        }
        return spendingTotal;
    }

    /**
     * Calculates the total spending for the current day.
     *
     * @return The total spending for the current day.
     */
    public double getDailySpending() {
        return getDailySpending(LocalDate.now());
    }

    /**
     * Calculates the total spending for the specified day.
     *
     * @param currentDate The date used to specify the day to calculate spending.
     * @return The total spending for the specified day.
     */
    public double getDailySpending(LocalDate currentDate){
        double spendingTotal = 0;
        for (Spending spending : this) {
            if (spending.getDate().isEqual(currentDate)) {
                spendingTotal = spendingTotal + spending.getAmount();
            }
        }
        return spendingTotal;
    }

    /**
     * Calculates the total spending for the current calendar year.
     *
     * @return The total spending for the current year.
     */
    public double getYearlySpending() {
        return getYearlySpending(LocalDate.now());
    }

    /**
     * Calculates the total spending for the specified calendar year.
     *
     * @param currentDate The date used to specify the year to calculate spending.
     * @return The total spending for the specified year.
     */
    public double getYearlySpending(LocalDate currentDate){
        double spendingTotal = 0;
        for (Spending spending : this) {
            if (isThisYear(spending.getDate(), currentDate)) {
                spendingTotal = spendingTotal + spending.getAmount();
            }
        }
        return spendingTotal;
    }

    private boolean isThisYear(LocalDate date, LocalDate currentDate) {
        return date.getYear() == currentDate.getYear();
    }

    private boolean isThisMonth(LocalDate date, LocalDate currentDate) {
        return date.getMonth().equals(currentDate.getMonth()) && isThisYear(date, currentDate);
    }

    /**
     * Updates all recurring spendings in the list based on their recurrence rules.
     * After updating, sorts the list by spending dates.
     */
    public void updateRecurrence() {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            Spending spending = this.get(i);
            Recurrence recurrence = Parser.parseRecurrence(spending);
            if (recurrence != null) {
                recurrence.checkSpendingRecurrence(spending, this, true);
            }
        }
        this.sort(Comparator.comparing(EntryType::getDate));
    }

    public void checkOverspend() {
        double dailyBudgetLeft = getDailyBudget() - getDailySpending();
        double monthlyBudgetLeft = getMonthlyBudget() - getMonthlySpending();
        double yearlyBudgetLeft = getYearlyBudget() - getYearlySpending();
        if (dailyBudgetLeft  < 0) {
            Ui.printOverspendMessage("daily", dailyBudgetLeft);
        }
        if (monthlyBudgetLeft  < 0) {
            Ui.printOverspendMessage("monthly", monthlyBudgetLeft);
        }
        if (yearlyBudgetLeft  < 0) {
            Ui.printOverspendMessage("yearly", yearlyBudgetLeft);
        }
    }
}


