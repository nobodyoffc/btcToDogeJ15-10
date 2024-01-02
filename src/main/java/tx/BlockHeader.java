package tx;
public class BlockHeader {

    private String hash;


    private int confirmations;

    private int height;


    private int version;


    private String versionHex;

    private String merkleroot;


    private long time;


    private long mediantime;


    private long nonce;


    private String bits;


    private double difficulty;


    private String chainwork;


    private String nextblockhash;

    // Add getters and setters for each field

    // You can also override toString() for debugging purposes

    @Override
    public String toString() {
        return "BlockInfo{" +
                "hash='" + hash + '\'' +
                ", confirmations=" + confirmations +
                ", height=" + height +
                ", version=" + version +
                ", versionHex='" + versionHex + '\'' +
                ", merkleroot='" + merkleroot + '\'' +
                ", time=" + time +
                ", mediantime=" + mediantime +
                ", nonce=" + nonce +
                ", bits='" + bits + '\'' +
                ", difficulty=" + difficulty +
                ", chainwork='" + chainwork + '\'' +
                ", nextblockhash='" + nextblockhash + '\'' +
                '}';
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionHex() {
        return versionHex;
    }

    public void setVersionHex(String versionHex) {
        this.versionHex = versionHex;
    }

    public String getMerkleroot() {
        return merkleroot;
    }

    public void setMerkleroot(String merkleroot) {
        this.merkleroot = merkleroot;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMediantime() {
        return mediantime;
    }

    public void setMediantime(long mediantime) {
        this.mediantime = mediantime;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public String getBits() {
        return bits;
    }

    public void setBits(String bits) {
        this.bits = bits;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public String getChainwork() {
        return chainwork;
    }

    public void setChainwork(String chainwork) {
        this.chainwork = chainwork;
    }

    public String getNextblockhash() {
        return nextblockhash;
    }

    public void setNextblockhash(String nextblockhash) {
        this.nextblockhash = nextblockhash;
    }
}

