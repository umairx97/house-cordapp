package bootcamp;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@BelongsToContract(TokenContract.class)
public class TokenState implements ContractState {
   private  Party issuer;
   private  Party owner;
   private int amount;

    public TokenState(Party issuer, Party owner, int amount){
        this.issuer = issuer;
        this.owner = owner;
        this.amount = amount;
    }

    public Party getIssuer() {
        return issuer;
    }

    public Party getOwner() {
        return owner;
    }

    public int getAmount() {
        return amount;
    }

    public List<AbstractParty> getParticipants() {
        List<AbstractParty> participants = new ArrayList<>();
        participants.add(owner);
        participants.add(issuer);
        return participants;
    }
}
