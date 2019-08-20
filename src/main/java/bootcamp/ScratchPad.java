package bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.List;

public class ScratchPad {
    public static void main(String[] args){
        StateAndRef<ContractState> inputState = null;
        HouseState outputState = new HouseState("123 Moorfield", null);
        PublicKey  requiredSigner = outputState.getOwner().getOwningKey();
        List<PublicKey> requiredSigners = ImmutableList.of(requiredSigner);
        Party notary = null;

        TransactionBuilder builder = new TransactionBuilder();

        builder.setNotary(notary);


        builder
                .addInputState(inputState)
                .addOutputState(outputState, "bootcamp.HouseContract")
                .addCommand(new HouseContract.Register(), requiredSigners);

    }
}
