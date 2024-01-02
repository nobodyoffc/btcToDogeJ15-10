package tx;


import org.bitcoinj.core.*;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import params.DogecoinMainNetParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;



public class DogeTxMaker {
    public static final long TO_SATOSHI=100000000;
    private static final long DOGE_DUST = (long)(0.1*TO_SATOSHI);

    public static void main(String[] args) {

        String url = "http://127.0.0.1:22555";
        String username = "username";
        String password = "password";

        String addr = "DS8M937nHLtmeNef6hnu17ZXAwmVpM6TXY";
        String minConf = "1";

        Utxo[] utxos = new ListUnspent().listUnspent(addr,minConf,url,username,password);
        if(utxos==null||utxos.length==0){
            System.out.println("No UTXOs");
            return;
        }

        List<TxInput> txInputList = new ArrayList<>();
        for(Utxo utxo1:utxos) {
            TxInput txInput = new TxInput();
            txInput.setTxId(utxo1.getTxid());
            txInput.setAmount((long) (utxo1.getAmount() * 100000000));
            txInput.setIndex(utxo1.getVout());

            String priKey = "L2w6HHF352YhuLsX33YgGDL9r9Uv3auyHz5StzarvGasZWwsP83E";
            byte[] priKeyBytes = getPriKey32(priKey);
            ECKey ecKey = ECKey.fromPrivate(priKeyBytes);

            txInput.setPriKey32(ecKey.getPrivKeyBytes());
            txInputList.add(txInput);
        }
        TxOutput txOutput = new TxOutput();
        txOutput.setAddress(addr);
        txOutput.setAmount(10000000);
        List<TxOutput> txOutputs = new ArrayList<>();
        txOutputs.add(txOutput);

        EstimateFee.ResultSmart fee = new EstimateFee().estimatesmartfee(3, url, username, password);
        String opReturn = null;//"freecash.org";
        String signedTx = createTransactionSignDoge(txInputList, txOutputs, opReturn, addr,fee.getFeerate());
        System.out.println(signedTx);
        String txid = "2787f21df6657be6fd216b8919b26ad726227bcd65c0cd76b1252aa66dfb8cb6";
        System.out.println(getFirstSenderDoge(txid,url,username,password));
    }

    public static String createTransactionSignDoge(List<TxInput> inputs, List<TxOutput> outputs, String opReturn, String returnAddr, double feeRateDouble) {

        long txSize = opReturn==null? calcTxSize(inputs.size(),outputs.size(),0): calcTxSize(inputs.size(),outputs.size(),opReturn.getBytes().length);
        long fee;
        if(feeRateDouble!=0){
            fee = (long)((feeRateDouble/1000)*TO_SATOSHI*txSize);
        }else fee =(long) (0.1*TO_SATOSHI);

        NetworkParameters networkParameters = DogecoinMainNetParams.get();;
        Transaction transaction = new Transaction(networkParameters);

        long totalMoney = 0;
        long totalOutput = 0;

        List<ECKey> ecKeys = new ArrayList<>();
        for (TxOutput output : outputs) {
            totalOutput += output.getAmount();

            transaction.addOutput(Coin.valueOf(output.getAmount()), Address.fromString(networkParameters, output.getAddress()));
        }

        if (opReturn != null && !"".equals(opReturn)) {
            try {
                // Assuming opReturn is a String containing your data
                byte[] data = opReturn.getBytes();

                // Convert data to hexadecimal format
                String hexData = HexFormat.of().formatHex(data);

                // Convert the hexadecimal string back to a byte array
                byte[] hexAsBytes = HexFormat.of().parseHex(hexData);

                Script opreturnScript = ScriptBuilder.createOpReturnScript(hexAsBytes);
                transaction.addOutput(Coin.ZERO,opreturnScript);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for (TxInput input : inputs) {
            totalMoney += input.getAmount();

            ECKey eckey = ECKey.fromPrivate(input.getPriKey32());

            ecKeys.add(eckey);
            UTXO utxo = new UTXO(Sha256Hash.wrap(input.getTxId()), input.getIndex(), Coin.valueOf(input.getAmount()), 0, false, ScriptBuilder.createP2PKHOutputScript(eckey));
            TransactionOutPoint outPoint = new TransactionOutPoint(networkParameters, utxo.getIndex(), utxo.getHash());
            TransactionInput unsignedInput = new TransactionInput(networkParameters, transaction, new byte[0], outPoint);
            transaction.addInput(unsignedInput);
        }
        if ((totalOutput + fee) > totalMoney) {
            throw new RuntimeException("input is not enough");
        }
        long change = totalMoney - totalOutput - fee;


        if (change > DOGE_DUST) {
            Address addr;
            if(returnAddr!=null){
                addr=Address.fromString(networkParameters,returnAddr);
            }else {
                ECKey ecKey = ECKey.fromPrivate(inputs.get(0).getPriKey32());
                addr = Address.fromKey(networkParameters, ecKey, Script.ScriptType.P2PKH);
            }
            transaction.addOutput(Coin.valueOf(change), addr);
        }

        for (int i = 0; i < inputs.size(); ++i) {

            ECKey eckey = ecKeys.get(i);
            Script lockScript = ScriptBuilder.createP2PKHOutputScript(eckey);
            TransactionSignature signature = transaction.calculateSignature(i, eckey, lockScript.getProgram(), Transaction.SigHash.ALL, false);
            Script unlockScript = ScriptBuilder.createInputScript(signature, eckey);
            transaction.getInput(i).setScriptSig(unlockScript);
        }

        byte[] signResult = transaction.bitcoinSerialize();

        return Utils.HEX.encode(signResult);
    }

    public static String getFirstSenderDoge(String txId, String apiUrl, String userName, String apiPassword){
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
            addr = hash160ToDogeAddr(script.getPubKeyHash());
        }catch (Exception e){e.printStackTrace();}
        return addr;
    }
    public static String hash160ToDogeAddr(byte[] hash160Bytes) {
        byte[] d = {0x1e};
        byte[] e = new byte[21];
        System.arraycopy(d, 0, e, 0, 1);
        System.arraycopy(hash160Bytes, 0, e, 1, 20);

        byte[] c = Sha256Hash.hashTwice(e);
        byte[] f = new byte[4];
        System.arraycopy(c, 0, f, 0, 4);
        byte[] addrRaw = BytesTools.bytesMerger(e, f);

        return Base58.encode(addrRaw);
    }

    public static long calcTxSize(int inputNum, int outputNum, int opReturnBytesLen) {
        long priceInSatoshi = 1;
        long baseLength = 10;
        long inputLength = 148 * (long) inputNum;
        long outputLength = 34 * (long) (outputNum + 1); // Include change output

        long opReturnLength = 0;
        if (opReturnBytesLen > 0) {
            opReturnLength = 1+opReturnBytesLen+VarInt.sizeOf(opReturnBytesLen)+VarInt.sizeOf((opReturnBytesLen)+VarInt.sizeOf(opReturnBytesLen)+1)+8;//8+1 + VarInt.sizeOf(opReturnBytesLen*2L) + opReturnBytesLen*2L;
        }
        long totalLength = baseLength + inputLength + outputLength + opReturnLength;
        return priceInSatoshi * totalLength;
    }


    public static byte[] getPriKey32(String priKey) {
        byte[] priKey32Bytes;
        byte[] priKeyBytes;
        byte[] suffix;
        byte[] priKeyForHash;
        byte[] hash;
        byte[] hash4;
        int len = priKey.length();

        switch (len) {
            case 64:
                priKey32Bytes = HexFormat.of().parseHex(priKey);
                break;
            case 52:
                if (!(priKey.substring(0, 1).equals("L") || priKey.substring(0, 1).equals("K"))) {
                    System.out.println("It's not a private key.");
                    return null;
                }
                priKeyBytes = Base58.decode(priKey);

                suffix = new byte[4];
                priKeyForHash = new byte[34];

                System.arraycopy(priKeyBytes, 0, priKeyForHash, 0, 34);
                System.arraycopy(priKeyBytes, 34, suffix, 0, 4);

                hash = Sha256Hash.hashTwice(priKeyForHash);

                hash4 = new byte[4];
                System.arraycopy(hash, 0, hash4, 0, 4);

                if (!Arrays.equals(suffix, hash4)) {
                    return null;
                }
                if (priKeyForHash[0] != (byte) 0x80) {
                    return null;
                }
                priKey32Bytes = new byte[32];
                System.arraycopy(priKeyForHash, 1, priKey32Bytes, 0, 32);
                break;
            case 51:
                if (!priKey.substring(0, 1).equals("5")) {
                    System.out.println("It's not a private key.");
                    return null;
                }

                priKeyBytes = Base58.decode(priKey);

                suffix = new byte[4];
                priKeyForHash = new byte[33];

                System.arraycopy(priKeyBytes, 0, priKeyForHash, 0, 33);
                System.arraycopy(priKeyBytes, 33, suffix, 0, 4);

                hash = Sha256Hash.hashTwice(priKeyForHash);

                hash4 = new byte[4];
                System.arraycopy(hash, 0, hash4, 0, 4);

                if (!Arrays.equals(suffix, hash4)) {
                    return null;
                }
                if (priKeyForHash[0] != (byte) 0x80) {
                    return null;
                }
                priKey32Bytes = new byte[32];
                System.arraycopy(priKeyForHash, 1, priKey32Bytes, 0, 32);
                break;
            default:
                System.out.println("It's not a private key.");
                return null;
        }

        return priKey32Bytes;
    }
}
