package seedu.commands;

import seedu.classes.Parser;
import seedu.type.IncomeList;
import seedu.type.SpendingList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.classes.Constants.BUDGET_COMMAND_FORMAT;
import static seedu.classes.Constants.INCORRECT_PARAMS_NUMBER;
import static seedu.classes.Constants.INVALID_CATEGORY;
import static seedu.classes.Constants.TAB;

class BudgetCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private final IncomeList incomes = new IncomeList();
    private final SpendingList spendings = new SpendingList();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restore() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void execute_setDailyBudget_success() {
        int budget = 1321;

        String userInput = "budget daily " + budget;
        Command command = Parser.parseUserInput(userInput);
        command.execute(incomes, spendings);

        assertEquals("\tSuccessfully set daily budget of: " + budget + System.lineSeparator()
                , outContent.toString());
        assertEquals(budget, spendings.getDailyBudget());
    }

    @Test
    public void execute_setMonthlyBudget_success() {
        int budget = 1321;

        String userInput = "budget monthly " + budget;
        Command command = Parser.parseUserInput(userInput);
        command.execute(incomes, spendings);

        assertEquals("\tSuccessfully set monthly budget of: " + budget + System.lineSeparator()
                , outContent.toString());
        assertEquals(budget, spendings.getMonthlyBudget());
    }

    @Test
    public void execute_setYearlyBudget_success() {
        int budget = 1321;

        String userInput = "budget yearly " + budget;
        Command command = Parser.parseUserInput(userInput);
        command.execute(incomes, spendings);

        assertEquals("\tSuccessfully set yearly budget of: " + budget + System.lineSeparator()
                , outContent.toString());
        assertEquals(budget, spendings.getYearlyBudget());
    }

    @Test
    public void execute_invalidAmount_exceptionThrown() {
        String userInput = "budget yearly abc";
        Command command = Parser.parseUserInput(userInput);
        command.execute(incomes, spendings);
        assertEquals("\tInvalid amount! Please enter in the form: budget {$PERIOD} {$AMOUNT}" + System.lineSeparator()
                , outContent.toString());
    }

    @Test
    public void execute_invalidTimeFrame_exceptionThrown() {
        String userInput = "budget notatimeframe 1";
        Command command = Parser.parseUserInput(userInput);
        command.execute(incomes, spendings);

        assertEquals(TAB + INVALID_CATEGORY + BUDGET_COMMAND_FORMAT
                + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void execute_tooFewInputs_exceptionThrown() {
        String userInput = "budget notenoughinputs";
        Command command = Parser.parseUserInput(userInput);
        command.execute(incomes, spendings);

        assertEquals(TAB + INCORRECT_PARAMS_NUMBER + BUDGET_COMMAND_FORMAT
                + System.lineSeparator(), outContent.toString());
    }
}
