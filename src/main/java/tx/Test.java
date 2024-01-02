package tx;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import params.DogecoinMainNetParams;

public class Test {
    public static void main(String[] args) {
        ECKey ecKey = new ECKey();
        System.out.println(Address.fromKey(MainNetParams.get(),ecKey, Script.ScriptType.P2PKH));
        System.out.println(Address.fromKey(DogecoinMainNetParams.get(),ecKey, Script.ScriptType.P2PKH));
    }
}
