package bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getInputStates().size() != 0)
            throw new IllegalArgumentException("A transaction must have zero inputs");
        if (tx.getOutputStates().size() != 1)
            throw new IllegalArgumentException("Transaction must have one output state");
        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException("Transaction must have one command");


        ContractState output = tx.getOutput(0);
        Command command = tx.getCommand(0);


        if (!(output instanceof TokenState))
            throw new IllegalArgumentException("Outputs should be instance of Token state");

        if(!(command.getValue() instanceof Issue))
            throw new IllegalArgumentException("Command must be a issue command");

        TokenState token = (TokenState) output;
        if(token.getAmount() < 0)
            throw new IllegalArgumentException("Token must be a positive amount");

        List<PublicKey> requiredSigners = command.getSigners();
        Party issuer = token.getIssuer();
        PublicKey issuersKey = issuer.getOwningKey();
        if(!(requiredSigners.contains(issuersKey)))
            throw new IllegalArgumentException("Issuer must be required signer.");
    }

    public static class Issue implements CommandData {}
}