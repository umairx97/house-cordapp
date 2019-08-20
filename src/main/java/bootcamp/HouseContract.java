package bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

import java.security.PublicKey;
import java.util.List;


public class HouseContract implements Contract {

    public void verify(LedgerTransaction tx) throws IllegalArgumentException {

        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException(("Transaction must have one command"));

        // Getting the commands from command pool
        Command command = tx.getCommand(0);

        // Getting the list of required signers on the command
        List<PublicKey> requiredSigners = command.getSigners();

        // getting the command type
        CommandData commandType = command.getValue();

        // Registration transaction logic
        if (commandType instanceof Register) {
            // Shape Constraints
            if (tx.getInputStates().size() != 0)
                throw new IllegalArgumentException("Registration transaction must have no inputs");

            if (tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("Registration transaction must have one output");
            // Content Constraints
            ContractState outputState = tx.getOutput(0);
            if (!(outputState instanceof HouseState))
                throw new IllegalArgumentException("Output must be a house state");

            // Getting the output house state
            HouseState houseState = (HouseState) outputState;

            if (houseState.getAddress().length() <= 3)
                throw new IllegalArgumentException("Address must be longer than 3 characters");

            if (houseState.getOwner().getName().getCountry().equals("Brazil"))
                throw new IllegalArgumentException("Registrations not allowed for Brazilian owners");

            // Getting the required signers from the house state
            Party owner = houseState.getOwner();
            PublicKey ownersKey = owner.getOwningKey();

            if (!(requiredSigners.contains(ownersKey)))
                throw new IllegalArgumentException("Owner of the house must sign the registration");


        } else if (commandType instanceof Transfer) {
            if (tx.getInputStates().size() != 1)
                throw new IllegalArgumentException("must have one input");


            if (tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("Must have one output state");

            ContractState input = tx.getInput(0);
            ContractState output = tx.getOutput(0);


            if (!(input instanceof HouseState))
                throw new IllegalArgumentException("Input must be a house state");
            if (!(output instanceof HouseState))
                throw new IllegalArgumentException("output must be a house state");


            HouseState inputHouse = (HouseState) input;
            HouseState outputHouse = (HouseState) output;

            if (!(inputHouse.getAddress().equals(outputHouse.getAddress())))
                throw new IllegalArgumentException("House address cannot be changed on transfer");


            if (inputHouse.getOwner().equals(outputHouse.getOwner()))
                throw new IllegalArgumentException("The owner should change on a transfer");


            Party inputOwner = inputHouse.getOwner();
            Party outputOwner = outputHouse.getOwner();

            if (!(requiredSigners.contains(inputOwner.getOwningKey())))
                throw new IllegalArgumentException("Current owner should sign the transfer");

            if (!(requiredSigners.contains(outputOwner.getOwningKey())))
                throw new IllegalArgumentException("New owner should sign the transfer");


        } else {
            throw new IllegalArgumentException("Command type not recognised");
        }


    }


    // Creating commands
    public static class Register implements CommandData {}

    public static class Transfer implements CommandData {}
}
