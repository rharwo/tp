package seedu.commands;

import seedu.classes.Ui;
import seedu.exception.WiagiInvalidIndexException;
import seedu.exception.WiagiInvalidInputException;
import seedu.exception.WiagiMissingParamsException;
import seedu.type.IncomeList;
import seedu.type.SpendingList;
import seedu.type.EntryType;

import java.util.ArrayList;

import static seedu.classes.Constants.EDIT_COMMAND_FORMAT;
import static seedu.classes.Constants.INCORRECT_PARAMS_NUMBER;
import static seedu.classes.Constants.INDEX_NOT_INTEGER;
import static seedu.classes.Constants.INDEX_OUT_OF_BOUNDS;
import static seedu.classes.Constants.INVALID_CATEGORY;
import static seedu.classes.Constants.INVALID_FIELD;
import static seedu.classes.Constants.MAX_LIST_AMOUNT_EXCEEDED_FOR_EDIT;
import static seedu.classes.Constants.MAX_LIST_TOTAL_AMOUNT;
import static seedu.classes.Constants.WHITESPACE;
import static seedu.classes.Constants.INCOME;
import static seedu.classes.Constants.SPENDING;

public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    private static final int TYPE_INDEX = 1;
    private static final int INDEX_OF_ENTRY_INDEX = 2;
    private static final int FIELD_INDEX = 3;
    private static final int NEW_VALUE_INDEX = 4;
    private static final int EDIT_COMPULSORY_ARGUMENTS_LENGTH = 5;
    private static final String AMOUNT_FIELD = "amount";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String DATE_FIELD = "date";
    private static final String TAG_FIELD = "tag";

    private final String fullCommand;

    public EditCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Execute editing with the given arguments
     *
     * @param incomes   list of incomes in the application
     * @param spendings list of spendings in the application
     */
    @Override
    public void execute(IncomeList incomes, SpendingList spendings) {
        assert incomes != null;
        assert spendings != null;
        try {
            handleCommand(incomes, spendings);
        } catch (WiagiMissingParamsException | WiagiInvalidInputException | WiagiInvalidIndexException e) {
            Ui.printWithTab(e.getMessage());
        }
    }

    private void handleCommand(IncomeList incomes, SpendingList spendings)
            throws WiagiMissingParamsException, WiagiInvalidIndexException {
        String[] arguments = extractArguments();
        String typeOfList = arguments[TYPE_INDEX];
        switch (typeOfList) {
        case INCOME:
            editList(arguments, incomes);
            break;
        case SPENDING:
            editList(arguments, spendings);
            break;
        default:
            throw new WiagiInvalidInputException(INVALID_CATEGORY + EDIT_COMMAND_FORMAT);
        }
    }

    private String[] extractArguments() throws WiagiMissingParamsException {
        String[] arguments = fullCommand.split(WHITESPACE, EDIT_COMPULSORY_ARGUMENTS_LENGTH);
        if (arguments.length < EDIT_COMPULSORY_ARGUMENTS_LENGTH) {
            throw new WiagiMissingParamsException(INCORRECT_PARAMS_NUMBER + EDIT_COMMAND_FORMAT);
        }
        return arguments;
    }

    private <T extends EntryType> void editList(String[] arguments, ArrayList<T> list)
            throws WiagiInvalidIndexException {
        String index = arguments[INDEX_OF_ENTRY_INDEX];
        EntryType entryToEdit = extractEntry(list, index);
        String newValue = arguments[NEW_VALUE_INDEX];
        String field = arguments[FIELD_INDEX];
        switch (field) {
        case AMOUNT_FIELD:
            throwExceptionIfTotalExceeded(newValue, entryToEdit.getAmount(), list);
            entryToEdit.editAmount(newValue);
            break;
        case DESCRIPTION_FIELD:
            entryToEdit.editDescription(newValue);
            break;
        case DATE_FIELD:
            entryToEdit.editDate(newValue);
            break;
        case TAG_FIELD:
            entryToEdit.editTag(newValue);
            break;
        default:
            throw new WiagiInvalidInputException(INVALID_FIELD + EDIT_COMMAND_FORMAT);
        }
        Ui.printWithTab("Edit Successful!");
    }

    private <T extends EntryType> void throwExceptionIfTotalExceeded(String newValue, double oldAmount,
                                                                     ArrayList<T> list) {
        double newAmount = CommandUtils.formatAmount(newValue, EDIT_COMMAND_FORMAT);
        double currTotal;
        if (list instanceof IncomeList) {
            currTotal = ((IncomeList) list).getTotal();
        } else {
            currTotal = ((SpendingList) list).getTotal();
        }

        double totalAmountAfterRecur = currTotal + newAmount - oldAmount;
        if (totalAmountAfterRecur > MAX_LIST_TOTAL_AMOUNT) {
            throw new WiagiInvalidInputException(MAX_LIST_AMOUNT_EXCEEDED_FOR_EDIT);
        }

        if (list instanceof IncomeList) {
            ((IncomeList) list).setTotal(totalAmountAfterRecur);
        } else {
            ((SpendingList) list).setTotal(totalAmountAfterRecur);
        }
    }

    private <T extends EntryType> EntryType extractEntry(ArrayList<T> list, String stringIndex)
            throws WiagiInvalidIndexException {
        try {
            int index = Integer.parseInt(stringIndex) - 1;
            return list.get(index);
        } catch (NumberFormatException e) {
            throw new WiagiInvalidIndexException(INDEX_NOT_INTEGER);
        } catch (IndexOutOfBoundsException e) {
            throw new WiagiInvalidIndexException(INDEX_OUT_OF_BOUNDS);
        }
    }
}
