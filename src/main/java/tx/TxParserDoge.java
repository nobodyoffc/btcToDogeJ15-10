package tx;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.script.Script;
import params.DogecoinMainNetParams;


import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

public class TxParserDoge {
    public static Transaction parseRawTransaction(String rawTransactionHex) {
        NetworkParameters params = DogecoinMainNetParams.get(); // Use MainNet parameters

        try {
            byte[] rawTxBytes = HexFormat.of().parseHex(rawTransactionHex);
            Transaction transaction = new Transaction(params, rawTxBytes);

            // Iterate through inputs
            List<TransactionInput> inputs = transaction.getInputs();
            for (TransactionInput input : inputs) {
                Script scriptSig = input.getScriptSig();
                // You can access input details here as needed
            }

            // Iterate through outputs
            List<TransactionOutput> outputs = transaction.getOutputs();
            for (TransactionOutput output : outputs) {
                // You can access output details here as needed
            }

            return transaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFirstSenderDoge(String txId, NetworkParameters netParams, String apiUrl, String userName, String apiPassword){
        String rawTxHex = new GetRawTx().getRawTx(txId,apiUrl,userName,apiPassword);
        byte[] rawTxBytes = HexFormat.of().parseHex(rawTxHex);
        byte[] firstTxIdBytes = BytesTools.invertArray(Arrays.copyOfRange(rawTxBytes,5,37));

        String firstTxId = HexFormat.of().formatHex(firstTxIdBytes);
        byte[] indexBytes = Arrays.copyOfRange(rawTxBytes,37,41);
        long index =BytesTools.bytes4ToLongLE(indexBytes);

        String rawFirstTx = new GetRawTx().getRawTx(firstTxId,apiUrl,userName,apiPassword);
        Transaction tx = TxParserDoge.parseRawTransaction(rawFirstTx);
        List<TransactionOutput> outputs = tx.getOutputs();

        TransactionOutput output = outputs.get((int) index);
        Script script = output.getScriptPubKey();
        String addr = null;
        try{

            addr = script.getToAddress(netParams).toString();
        }catch (Exception ignore){}
        return addr;
    }

    private static void testParseRawTx() {
        String rawTransactionHex = "02000000010718a3814824cc42b5cc2202e6de9201c5f59a6bfce2de9850faf7830936f54400000000644182862a641e799cf92346ee9e1b3d3a79ddd0531ed1531f356118da88cbe0ebacca1ec4d221e90a3b25c3144b5c8f74d036694961912eeb45de97bd406be90fac4121030be1d7e633feb2338a74a860e76d893bac525f35a5813cb7b21e27ba1bc8312affffffff0200943577000000001976a914363f03fe7692952d0891ea70ede97cbecce5889488ac440a14f0030000001976a91461c42abb6e3435e63bd88862f3746a3f8b86354288ac00000000";

        Transaction parsedTransaction = parseRawTransaction(rawTransactionHex);

        if (parsedTransaction != null) {
            System.out.println("Parsed Transaction: " + parsedTransaction.toString());
        } else {
            System.out.println("Failed to parse the transaction.");
        }
    }
}
